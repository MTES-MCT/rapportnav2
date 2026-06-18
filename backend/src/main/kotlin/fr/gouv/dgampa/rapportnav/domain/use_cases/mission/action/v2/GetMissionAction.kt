package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import java.time.Instant
import java.util.*

@UseCase
class GetMissionAction(
    private val getEnvActionByMissionId: GetComputeEnvActionListByMissionId,
    private val getNavActionByMissionId: GetComputeNavActionListByMissionId,
    private val getFIshListActionByMissionId: GetComputeFishActionListByMissionId,
    private val getComputeNavActionListByMissionId: GetComputeNavActionListByMissionId,
) {
    fun execute(missionId: Int): List<MissionActionEntity> {
        val envActions = getEnvActionByMissionId.execute(missionId = missionId)
        val navActions = getNavActionByMissionId.execute(missionId = missionId)
        val fishActions = getFIshListActionByMissionId.execute(missionId = missionId)

        // extract STATUS actions from already-fetched nav actions to avoid re-querying the DB per action
        val statusActions = navActions
            .filterIsInstance<MissionNavActionEntity>()
            .filter { it.actionType == ActionType.STATUS && it.startDateTimeUtc != null }

        return (envActions + navActions + fishActions)
            .sortedByDescending { it.startDateTimeUtc }
            .map { action ->
                action.status = computeStatus(action.startDateTimeUtc, statusActions)
                action
            }
    }

    fun execute(missionIdUUID: UUID): List<MissionActionEntity> {
        return getComputeNavActionListByMissionId.execute(ownerId = missionIdUUID)
            .sortedByDescending { action -> action.startDateTimeUtc }
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
