package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable

@UseCase
class GetNavActionListByMissionId(
    private val navMissionActionRepository: INavMissionActionRepository,
    private val attachControlToAction: AttachControlToAction,
) {
    private val logger = LoggerFactory.getLogger(GetNavActionListByMissionId::class.java)

    @Cacheable(value = ["navActionList"], key = "#missionId")
    fun execute(missionId: Int?): List<MissionNavActionEntity> {
        if (missionId == null) {
            logger.error("GetNavActionListByMissionId received a null missionId")
            throw IllegalArgumentException("GetNavActionListByMissionId should not receive null missionId")
        }
        return try {
            getNavActionList(missionId = missionId)
                .map {
                    var action = MissionNavActionEntity.fromMissionActionModel(it)
                    action = attachControlToAction.execute(action) as MissionNavActionEntity
                    action.computeCompleteness()
                    action
                }
        } catch (e: Exception) {
            logger.error("GetFishActionsByMissionId failed loading Actions", e)
            return listOf()
        }
    }

    private fun getNavActionList(missionId: Int): List<MissionActionModel> {
        return navMissionActionRepository.findByMissionId(missionId = missionId)
    }
}
