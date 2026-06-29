package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionExternalId
import java.time.Instant
import java.util.*

@UseCase
class GetActionsByOwnerId(
    private val getEnvActionByMissionId: GetComputeEnvActionListByMissionId,
    private val getFIshListActionByMissionId: GetComputeFishActionListByMissionId,
    private val getComputeNavActionListByMissionId: GetComputeNavActionListByMissionId,
    private val getMissionExternalId: GetMissionExternalId
) {
    fun execute(missionId: UUID): List<MissionActionEntity> {
        // Resolve the external id once and thread it into the env/fish sub-use-cases so they
        // don't each re-resolve the same mission (avoids an N+1 on the mission table).
        val externalId = getMissionExternalId.execute(missionId)

        val envAndFishActions = if (externalId != null) {
            val envActions = getEnvActionByMissionId.execute(missionId = missionId, externalId = externalId)
            val fishActions = getFIshListActionByMissionId.execute(missionId = missionId, externalId = externalId)
            envActions + fishActions
        } else {
            emptyList()
        }

        val navActions = getComputeNavActionListByMissionId.execute(ownerId = missionId)
        // extract STATUS actions from already-fetched nav actions to avoid re-querying the DB per action
        val statusActions = navActions
            .filterIsInstance<MissionNavActionEntity>()
            .filter { it.actionType == ActionType.STATUS && it.startDateTimeUtc != null }


        return (envAndFishActions + navActions)
            .sortedByDescending { action -> action.startDateTimeUtc }
            .map { action ->
                action.status = computeStatus(action.startDateTimeUtc, statusActions)
                action
            }
    }

    private fun computeStatus(
        actionStartDateTimeUtc: Instant?,
        statusActions: List<MissionNavActionEntity>
    ): ActionStatusType {
        if (statusActions.isEmpty()) return ActionStatusType.UNKNOWN
        val last = if (actionStartDateTimeUtc == null) {
            statusActions.maxByOrNull { it.startDateTimeUtc!! }
        } else {
            statusActions
                .filter { it.startDateTimeUtc!! <= actionStartDateTimeUtc }
                .maxByOrNull { it.startDateTimeUtc!! }
        }
        return last?.status ?: ActionStatusType.UNKNOWN
    }
}
