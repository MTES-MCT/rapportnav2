package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.ActionDocumentTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.ExportAuditEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.ExportedDocumentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiConclusionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiDocumentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiGearInspectionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiSpeciesInspectionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiVesselComplianceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiInspectorEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.ports.ISatiPdfGenerator
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.export.IExportAuditRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.EnableSati
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.GetNatinfs
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import org.slf4j.LoggerFactory

/**
 * Generates the SATI pdf of a fish sea control (SATI module M1) that is complete for stats,
 * for services where SATI is enabled.
 * The download is audited (who/when) through [markAsDownloaded], once the pdf has been delivered.
 */
@UseCase
class ExportSatiPdf(
    private val enableSati: EnableSati,
    private val getUserFromToken: GetUserFromToken,
    private val getFishActionById: GetFishActionById,
    private val getServiceById: GetServiceById,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val agentRepository: IAgentRepository,
    private val satiPdfGenerator: ISatiPdfGenerator,
    private val exportAuditRepository: IExportAuditRepository,
    private val getNatinfs: GetNatinfs,
) {
    private val logger = LoggerFactory.getLogger(ExportSatiPdf::class.java)

    fun execute(ownerId: String, actionId: String): ExportedDocumentEntity {
        if (!enableSati.execute()) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION,
                message = "ExportSatiPdf: SATI is not enabled for the user's service"
            )
        }

        // checked upfront: the audit records the user (exported_by), so an export that cannot be
        // attributed to a user must not be generated
        val user = getUserFromToken.execute()
        if (user?.id == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION,
                message = "ExportSatiPdf: the export cannot be attributed to a user"
            )
        }

        val action = ownerId.toIntOrNull()
            ?.let { missionId -> getFishActionById.execute(missionId = missionId, actionId = actionId) }
        // for now the pdf only exists for sea controls (module M1)
        val module = action?.let { SatiModuleType.fromMissionActionType(it.fishActionType) }
            ?.takeIf { it == SatiModuleType.M1 }
        if (action == null || module == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "ExportSatiPdf: no fish sea control '$actionId' for owner '$ownerId'"
            )
        }

        if (action.isCompleteForStats != true) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "ExportSatiPdf: action '$actionId' is not complete for stats"
            )
        }

        return ExportedDocumentEntity(
            content = satiPdfGenerator.generate(toDocument(action = action, module = module, userServiceId = user.serviceId)),
            // audit the resolved ids rather than the raw path values ("012345" and "+12345" both resolve to 12345)
            audit = ExportAuditEntity(
                ownerId = action.missionId.toString(),
                actionId = action.id?.toString() ?: actionId,
                documentType = ActionDocumentTypeEnum.SATI,
            ),
        )
    }

    fun markAsDownloaded(document: ExportedDocumentEntity) {
        exportAuditRepository.save(document.audit)
    }

    private fun toDocument(action: MissionFishActionEntity, module: SatiModuleType, userServiceId: Int?): SatiDocumentEntity {
        val principalInspector = action.sati?.inspectors?.find { it.isPrincipal }
        val (firstName, lastName) = principalInspector?.let { getInspectorNames(it) } ?: (null to null)
        return SatiDocumentEntity(
            module = module,
            vesselName = action.sati?.vessel?.name ?: action.vesselName,
            serviceName = getInspectionServiceName(action, userServiceId = userServiceId),
            inspectorFirstName = firstName,
            inspectorLastName = lastName,
            inspectorAuthorityType = principalInspector?.authorityType,
            startDatetimeUtc = action.actionDatetimeUtc,
            endDatetimeUtc = action.actionEndDatetimeUtc,
            latitude = action.latitude,
            longitude = action.longitude,
            portName = action.portName,
            portLocode = action.portLocode,
            vessel = action.sati?.vessel,
            isUnitBoarded = action.isUnitBoarded,
            compliance = SatiVesselComplianceEntity.fromFishAction(action),
            gearInspection = SatiGearInspectionEntity(gears = action.gearOnboard.orEmpty()),
            speciesInspection = SatiSpeciesInspectionEntity.fromFishAction(action),
            conclusion = SatiConclusionEntity.fromFishAction(action, natinfs = getNatinfs(action)),
        )
    }

    /**
     * The inspection unit is the service of the mission, whoever exports it (e.g. an admin, or an agent of another service).
     * The exporting user's service stands for it only when the mission has none.
     */
    private fun getInspectionServiceName(action: MissionFishActionEntity, userServiceId: Int?): String? =
        getMissionGeneralInfoByMissionId.execute(missionId = action.missionId)?.service?.name
            ?: getServiceById.execute(userServiceId)?.name

    /**
     * The NATINFs categorizing and describing the infractions, fetched only when there are some. Without them,
     * the infractions still show their NATINF codes: the pdf is not worth failing for their categories and descriptions.
     */
    private fun getNatinfs(action: MissionFishActionEntity): List<NatinfEntity> {
        if (!SatiConclusionEntity.hasInfractions(action)) return emptyList()
        return runCatching { getNatinfs.execute() }
            .onFailure { logger.warn("ExportSatiPdf: could not get the NATINF descriptions", it) }
            .getOrDefault(emptyList())
    }

    /**
     * (first name, last name) of an inspector: an agent when from the unit, a contact otherwise.
     * A contact may only have a full name, which then stands for the last name.
     */
    private fun getInspectorNames(inspector: SatiInspectorEntity): Pair<String?, String?> {
        val agent = inspector.agentId?.let { agentRepository.findById(it) }
        if (agent != null) return agent.firstName to agent.lastName
        val contact = inspector.party?.contact
        return contact?.firstName to (contact?.lastName ?: contact?.fullName)
    }
}
