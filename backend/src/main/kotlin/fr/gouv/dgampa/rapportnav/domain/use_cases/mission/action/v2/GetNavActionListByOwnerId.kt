package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import java.util.*

@UseCase
class GetNavActionListByOwnerId(
    private val navMissionActionRepository: INavMissionActionRepository
) {
    fun execute(missionId: Int?): List<MissionNavActionEntity> {
        if (missionId == null) return listOf()
        return try {
            navMissionActionRepository.findByMissionId(missionId = missionId)
                .map { MissionNavActionEntity.fromMissionActionModel(it) }
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "GetNavActionListByOwnerId failed for missionId=$missionId",
                originalException = e
            )
        }
    }

    fun execute(ownerId: UUID?): List<MissionNavActionEntity> {
        if (ownerId == null) return listOf()
        return try {
            navMissionActionRepository.findByOwnerId(ownerId = ownerId)
                .map { MissionNavActionEntity.fromMissionActionModel(it) }
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "GetNavActionListByOwnerId failed for ownerId=$ownerId",
                originalException = e
            )
        }
    }
}
