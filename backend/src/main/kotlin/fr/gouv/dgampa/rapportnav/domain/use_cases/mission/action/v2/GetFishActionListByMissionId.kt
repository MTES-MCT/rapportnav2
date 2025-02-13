package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable

@UseCase
class GetFishActionListByMissionId(
    private val fishActionRepo: IFishActionRepository
) {
    private val logger = LoggerFactory.getLogger(GetFishActionListByMissionId::class.java)

    @Cacheable(value = ["fishActionList"], key = "#missionId")
    fun execute(missionId: Int?): List<MissionAction> {
        require(missionId != null)  {
            logger.error("GetFishActionListByMissionId received a null missionId")
            throw IllegalArgumentException("GetFishActionListByMissionId should not receive null missionId")
        }
        return try {
            fishActionRepo.findFishActions(missionId = missionId)
        } catch (e: Exception) {
            logger.error("GetFishActionListByMissionId failed loading mission", e)
            return listOf()
        }
    }
}
