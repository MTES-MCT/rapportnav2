package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.export.ActionDocumentTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.export.ExportAuditEntity
import fr.gouv.dgampa.rapportnav.domain.entities.export.ExportedDocumentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.ports.ISatiPdfGenerator
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.export.IExportAuditRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.EnableSati
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken

/**
 * Generates the SATI pdf of a fish control action that has a SATI module (sea or land control)
 * and is complete for stats, for services where SATI is enabled.
 * The download is audited (who/when) through [markAsDownloaded], once the pdf has been delivered.
 */
@UseCase
class ExportSatiPdf(
    private val enableSati: EnableSati,
    private val getUserFromToken: GetUserFromToken,
    private val getFishActionById: GetFishActionById,
    private val satiPdfGenerator: ISatiPdfGenerator,
    private val exportAuditRepository: IExportAuditRepository,
) {
    fun execute(ownerId: String, actionId: String): ExportedDocumentEntity {
        if (!enableSati.execute()) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION,
                message = "ExportSatiPdf: SATI is not enabled for the user's service"
            )
        }

        // checked upfront: the audit records the user (exported_by), so an export that cannot be
        // attributed to a user must not be generated
        if (getUserFromToken.execute()?.id == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION,
                message = "ExportSatiPdf: the export cannot be attributed to a user"
            )
        }

        val action = ownerId.toIntOrNull()
            ?.let { missionId -> getFishActionById.execute(missionId = missionId, actionId = actionId) }
            ?.takeIf { SatiModuleType.fromMissionActionType(it.fishActionType) != null }
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "ExportSatiPdf: no fish action with a SATI '$actionId' for owner '$ownerId'"
            )

        if (action.isCompleteForStats != true) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "ExportSatiPdf: action '$actionId' is not complete for stats"
            )
        }

        return ExportedDocumentEntity(
            content = satiPdfGenerator.generate(action),
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
}
