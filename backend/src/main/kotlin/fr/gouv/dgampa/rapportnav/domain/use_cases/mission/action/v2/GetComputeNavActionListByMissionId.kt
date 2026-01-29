package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import java.util.*

@UseCase
class GetComputeNavActionListByMissionId(
    private val processNavAction: ProcessNavAction,
    private val getNavActionListByOwnerId: GetNavActionListByOwnerId
) {
    fun execute(missionId: Int): List<MissionNavActionEntity> {
        return try {
            val actions = getNavActionListByOwnerId.execute(missionId = missionId)
            actions.map { processNavAction.execute(action = it) }
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "GetComputeNavActionListByMissionId failed for missionId=$missionId",
                originalException = e
            )
        }
    }

    fun execute(ownerId: UUID): List<MissionNavActionEntity> {
        return try {
            val actions = getNavActionListByOwnerId.execute(ownerId = ownerId)
            actions.map { processNavAction.execute(action = it) }
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "GetComputeNavActionListByMissionId failed for ownerId=$ownerId",
                originalException = e
            )
        }
    }
}
