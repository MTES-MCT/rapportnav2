package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity

class MissionEnvAction(
    override val id: String,
    override val missionId: Int,
    override val actionType: ActionType,
    override val source: MissionSourceEnum,
    override val isCompleteForStats: Boolean? = null,
    override val status: ActionStatusType? = null,
    override val summaryTags: List<String>? = null,
    override val controlsToComplete: List<ControlType>? = null,
    override val completenessForStats: CompletenessForStatsEntity? = null,
    override val sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
    override val data: MissionEnvActionData
) : MissionAction(
    id = id,
    missionId = missionId,
    status = status,
    actionType = actionType,
    summaryTags = summaryTags,
    source = source,
    controlsToComplete = controlsToComplete,
    data = data
) {

    companion object {
        fun fromMissionActionEntity(action: MissionActionEntity): MissionEnvAction {
            val envAction = action as MissionEnvActionEntity
            return MissionEnvAction(
                id = envAction.id.toString(),
                missionId = envAction.missionId,
                status = envAction.status,
                source = envAction.source,
                summaryTags = envAction.summaryTags,
                controlsToComplete = envAction.controlsToComplete,
                actionType = envAction.actionType.toString().let { ActionType.valueOf(it) },
                isCompleteForStats = envAction.isCompleteForStats,
                completenessForStats = envAction.completenessForStats,
                data = MissionEnvActionData(
                    startDateTimeUtc = envAction.startDateTimeUtc,
                    endDateTimeUtc = envAction.endDateTimeUtc,
                    completedBy = envAction.completedBy,
                    formattedControlPlans = envAction.formattedControlPlans,
                    geom = envAction.geom,
                    facade = envAction.facade,
                    department = envAction.department,
                    isAdministrativeControl = envAction.isAdministrativeControl,
                    isComplianceWithWaterRegulationsControl = envAction.isComplianceWithWaterRegulationsControl,
                    isSafetyEquipmentAndStandardsComplianceControl = envAction.isSafetyEquipmentAndStandardsComplianceControl,
                    isSeafarersControl = envAction.isSeafarersControl,
                    openBy = envAction.openBy,
                    observations = envAction.observations,
                    observationsByUnit = envAction.observationsByUnit,
                    actionNumberOfControls = envAction.actionNumberOfControls,
                    actionTargetType = envAction.actionTargetType,
                    vehicleType = envAction.vehicleType,
                    infractions = getInfractionGroupBy(envAction),
                    targets = envAction.targets?.map { Target2.fromTargetEntity(it) }?.sortedBy { it.startDateTimeUtc },
                    availableControlTypesForInfraction = envAction.availableControlTypesForInfraction,
                    coverMissionZone = envAction.coverMissionZone,
                    controlSecurity = ControlSecurity.fromControlSecurityEntity(envAction.controlSecurity),
                    controlGensDeMer = ControlGensDeMer.fromControlGensDeMerEntity(envAction.controlGensDeMer),
                    controlNavigation = ControlNavigation.fromControlNavigationEntity(envAction.controlNavigation),
                    controlAdministrative = ControlAdministrative.fromControlAdministrativeEntity(envAction.controlAdministrative)
                )
            )
        }

        private fun getInfractionGroupBy(action: MissionEnvActionEntity): List<InfractionByTarget> {
            val envInfractions = action.envInfractions?.map { Infraction.fromEnvInfractionEntity(it) } ?: listOf()
            val navInfractions = action.navInfractions?.map { Infraction.fromInfractionEntity(it) } ?: listOf()

            return (envInfractions + navInfractions)
                .filter { it.target?.vesselIdentifier != null || it.target?.identityControlledPerson != null }
                .groupBy { it.target?.vesselIdentifier ?: it.target?.identityControlledPerson }
                .map { (vesselIdentifier, infractions) ->
                    InfractionByTarget(
                        vesselIdentifier = vesselIdentifier,
                        vesselType = infractions.firstOrNull()?.target?.vesselType,
                        infractions = infractions,
                        identityControlledPerson = infractions.firstOrNull()?.target?.identityControlledPerson
                    )
                }
        }
    }
}
