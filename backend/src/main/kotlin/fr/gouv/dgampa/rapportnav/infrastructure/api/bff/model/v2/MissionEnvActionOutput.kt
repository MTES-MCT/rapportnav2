package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity

class MissionEnvActionOutput(
    override val id: String,
    override val missionId: Int,
    override val actionType: ActionType,
    override val isCompleteForStats: Boolean?,
    override val status: ActionStatusType? = null,
    override val summaryTags: List<String>? = null,
    override val completenessForStats: CompletenessForStatsEntity? = null,
    override val sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
    override val data: MissionEnvActionDataOutput
) : MissionActionOutput(
    id = id,
    missionId = missionId,
    status = status,
    actionType = actionType,
    summaryTags = summaryTags,
    data = data
) {

    companion object {
        fun fromMissionActionEntity(action: MissionActionEntity): MissionEnvActionOutput {
            val envAction = action as MissionEnvActionEntity
            return MissionEnvActionOutput(
                id = envAction.id.toString(),
                missionId = envAction.missionId,
                status = envAction.status,
                summaryTags = envAction.summaryTags,
                actionType = envAction.actionType.toString().let { ActionType.valueOf(it) },
                isCompleteForStats = envAction.isCompleteForStats,
                data = MissionEnvActionDataOutput(
                    startDateTimeUtc = envAction.actionStartDateTimeUtc,
                    endDateTimeUtc = envAction.actionEndDateTimeUtc,
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
                    controlsToComplete = envAction.controlsToComplete,
                    availableControlTypesForInfraction = envAction.availableControlTypesForInfraction,
                    coverMissionZone = envAction.coverMissionZone,
                    controlSecurity = envAction.controlSecurity,
                    controlGensDeMer = envAction.controlGensDeMer,
                    controlNavigation = envAction.controlNavigation,
                    controlAdministrative = envAction.controlAdministrative,
                )
            )
        }

        private fun getInfractionGroupBy(action: MissionEnvActionEntity): List<InfractionEnvDataByOutput> {
            val infractions = action.envInfractions?.map { InfractionEnvDataOutput.fromInfractionEnvEntity(it) }?: listOf()
            val infractions1 = action.navInfractions?.map{ InfractionEnvDataOutput.fromInfractionNavEntity(it)}?: listOf()

            return (infractions+ infractions1)
                .groupBy { it.target?.vesselIdentifier ?: it.target?.identityControlledPerson }
                .map { (key, values) ->  InfractionEnvDataByOutput(key = key?:"", data = values)}
        }
    }
}
