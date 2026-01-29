package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import java.util.*

@UseCase
class GetMissionAction(
    private val getEnvActionByMissionId: GetComputeEnvActionListByMissionId,
    private val getNavActionByMissionId: GetComputeNavActionListByMissionId,
    private val getFIshListActionByMissionId: GetComputeFishActionListByMissionId,
    private val getComputeNavActionListByMissionId: GetComputeNavActionListByMissionId,
    private val getStatusForAction: GetStatusForAction
) {
    fun execute(missionId: Int): List<MissionActionEntity> {
        return try {
            val envActions = getEnvActionByMissionId.execute(missionId = missionId)
            val navActions = getNavActionByMissionId.execute(missionId = missionId)
            val fishActions = getFIshListActionByMissionId.execute(missionId = missionId)
            (envActions + navActions + fishActions).sortedByDescending { action -> action.startDateTimeUtc }
                .map { action ->
                    // compute action status
                    action.status = getStatusForAction.execute(missionId = missionId, actionStartDateTimeUtc = action.startDateTimeUtc)
                    action
                }
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "GetMissionAction failed for missionId=$missionId",
                originalException = e
            )
        }
    }

    fun execute(missionIdUUID: UUID): List<MissionActionEntity> {
        return try {
            getComputeNavActionListByMissionId.execute(ownerId = missionIdUUID)
                .sortedByDescending { action -> action.startDateTimeUtc }
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "GetMissionAction failed for missionIdUUID=$missionIdUUID",
                originalException = e
            )
        }
    }
}
