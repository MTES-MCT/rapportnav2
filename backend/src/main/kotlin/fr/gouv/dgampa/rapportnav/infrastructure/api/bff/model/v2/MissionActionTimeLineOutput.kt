package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTargetTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.FormattedEnvActionControlPlan
import java.time.Instant

data class MissionActionTimeLineOutput(
    val id: String,
    val actionType: ActionType,
    val missionId: Int,
    val source: MissionSourceEnum,
    var isCompleteForStats: Boolean? = null,
    //val status
    val summaryTags: List<String>? = null,
    val completenessForStats: CompletenessForStatsEntity? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
    var vesselId: Int? = null,
    val vesselName: String? = null,
    val actionNumberOfControls: Int? = null,
    val actionTargetType: ActionTargetTypeEnum? = null,
    val vehicleType: VehicleTypeEnum? = null,
    val controlsToComplete: List<ControlType>? = listOf(),
    val formattedControlPlans: FormattedEnvActionControlPlan? = null,
    val observations: String? = null,
    val isPersonRescue: Boolean? = null,
    val isVesselRescue: Boolean? = null,
    val reason: ActionStatusReason? = null,
    val controlMethod: ControlMethod? = null,
    val vesselIdentifier: String? = null,
    val vesselType: VesselTypeEnum? = null,
    val vesselSize: VesselSizeEnum? = null
) {
    companion object {
        fun fromMissionActionEntity(action: MissionActionEntity): MissionActionTimeLineOutput? {
            return when (action) {
                is MissionNavActionEntity -> fromMissionNavActionEntity(action)
                is MissionEnvActionEntity -> fromMissionEnvActionEntity(action)
                is MissionFishActionEntity -> fromMissionFishActionEntity(action)
                else -> null
            }
        }

        private fun fromMissionEnvActionEntity(action: MissionActionEntity): MissionActionTimeLineOutput {
            val envAction = action as MissionEnvActionEntity
            return MissionActionTimeLineOutput(
                id = envAction.id.toString(),
                source = envAction.source,
                actionType = envAction.actionType,
                missionId = envAction.missionId,
                completenessForStats = envAction.completenessForStats,
                startDateTimeUtc = envAction.startDateTimeUtc,
                endDateTimeUtc = envAction.endDateTimeUtc,
                observations = envAction.observations,
                actionNumberOfControls = envAction.actionNumberOfControls,
                actionTargetType = envAction.actionTargetType,
                vehicleType = envAction.vehicleType,
            )
        }

        private fun fromMissionNavActionEntity(action: MissionActionEntity): MissionActionTimeLineOutput {
            val navAction = action as MissionNavActionEntity
            return MissionActionTimeLineOutput(
                id = navAction.id.toString(),
                source = navAction.source,
                missionId = navAction.missionId,
                actionType = navAction.actionType,
                completenessForStats = navAction.completenessForStats,
                startDateTimeUtc = navAction.startDateTimeUtc,
                endDateTimeUtc = navAction.endDateTimeUtc,
                observations = navAction.observations,
                controlMethod = navAction.controlMethod,
                vesselIdentifier = navAction.vesselIdentifier,
                vesselType = navAction.vesselType,
                vesselSize = navAction.vesselSize,
                isVesselRescue = navAction.isVesselRescue,
                isPersonRescue = navAction.isPersonRescue,
                reason = navAction.reason
            )
        }

        private fun fromMissionFishActionEntity(action: MissionActionEntity): MissionActionTimeLineOutput {
            val fishAction = action as MissionFishActionEntity
            return MissionActionTimeLineOutput(
                id = fishAction.id.toString(),
                source = fishAction.source,
                actionType = fishAction.actionType,
                missionId = fishAction.missionId,
                completenessForStats = fishAction.completenessForStats,
                startDateTimeUtc = fishAction.startDateTimeUtc,
                endDateTimeUtc = fishAction.endDateTimeUtc,
                vesselId = fishAction.vesselId,
                vesselName = fishAction.vesselName,
                observations = fishAction.observationsByUnit,
            )
        }
    }
}
