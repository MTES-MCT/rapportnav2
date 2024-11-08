package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.FakeActionData
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable

@UseCase
class GetEnvActionListByMissionId(
    private val monitorEnvApiRepo: IEnvMissionRepository,
    private val attachControlToAction: AttachControlToAction,
    private val getFakeActionData: FakeActionData
) {
    private val logger = LoggerFactory.getLogger(GetFishActionListByMissionId::class.java)

    @Cacheable(value = ["envActionList"], key = "#missionId")
    fun execute(missionId: Int?): List<MissionEnvActionEntity> {
        if (missionId == null) {
            logger.error("GetFishListActionByMissionId received a null missionId")
            throw IllegalArgumentException("GetFishListActionByMissionId should not receive null missionId")
        }
        return try {
            val actions = getEnvActionList(missionId = missionId)
            processActions(missionId = missionId, actions = actions)
        } catch (e: Exception) {
            logger.error("GetFishActionsByMissionId failed loading Actions", e)
            return listOf()
            //return fakeActions(missionId = missionId) //TODO remove this way of !!!
        }
    }

    private fun getEnvActionList(missionId: Int): List<EnvActionEntity> {
        return monitorEnvApiRepo.findMissionById(missionId = missionId)?.envActions ?: listOf()
    }

    private fun processActions(missionId: Int, actions:  List<EnvActionEntity>): List<MissionEnvActionEntity> {
        return actions.map {
            var action = MissionEnvActionEntity.fromEnvAction(missionId = missionId, action = it)
            action = attachControlToAction.execute(action) as MissionEnvActionEntity
            action.computeCompleteness()
            action
        }
    }

    private fun fakeActions(missionId: Int): List<MissionEnvActionEntity> {
        val actions = getFakeActionData.getFakeEnvControls() + getFakeActionData.getFakeEnvSurveillance()
        return  processActions(missionId = missionId, actions = actions)
    }
}
