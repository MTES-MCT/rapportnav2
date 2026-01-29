package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import org.springframework.cache.annotation.Cacheable

@UseCase
class GetFishActionListByMissionId(
    private val fishActionRepo: IFishActionRepository
) {
    @Cacheable(value = ["fishActionList"], key = "#missionId")
    fun execute(missionId: Int?): List<MissionAction> {
        if (missionId == null) return listOf()
        return try {
            fishActionRepo.findFishActions(missionId = missionId)
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "GetFishActionListByMissionId failed for missionId=$missionId",
                originalException = e
            )
        }
    }
}
