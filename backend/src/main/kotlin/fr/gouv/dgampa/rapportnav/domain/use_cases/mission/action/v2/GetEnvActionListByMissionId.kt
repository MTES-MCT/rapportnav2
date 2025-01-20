package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.FakeActionData
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.MapEnvActionControlPlans
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.GetInfractionsByActionId
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.FormattedEnvActionControlPlan
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable

@UseCase
class GetEnvActionListByMissionId(
    private val monitorEnvApiRepo: IEnvMissionRepository,
    private val mapControlPlans: MapEnvActionControlPlans,
    private val getInfractionsByActionId: GetInfractionsByActionId,
    getStatusForAction: GetStatusForAction,
    getControlByActionId: GetControlByActionId2,
    private val getFakeActionData: FakeActionData
) : AbstractGetMissionAction(getStatusForAction, getControlByActionId) {
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
            val action = MissionEnvActionEntity.fromEnvAction(missionId = missionId, action = it)
            action.status = this.getStatus(action)
            action.computeControls(this.getControls(action))
            action.formattedControlPlans = getFormattedControlPlanList(action.controlPlans)
            action.navInfractions = getInfractionsByActionId.execute(action.id.toString())
            action.computeCompleteness()
            action
        }
    }

    private fun getFormattedControlPlanList(controlPlans: List<EnvActionControlPlanEntity>?): List<FormattedEnvActionControlPlan>? {
        val filteredControlPlans: ControlPlansEntity? = mapControlPlans.execute(controlPlans)
        return FormattedEnvActionControlPlan.fromControlPlansEntity(filteredControlPlans)
    }

    private fun fakeActions(missionId: Int): List<MissionEnvActionEntity> {
        val actions = getFakeActionData.getFakeEnvControls() + getFakeActionData.getFakeEnvSurveillance()
        return  processActions(missionId = missionId, actions = actions)
    }
}
