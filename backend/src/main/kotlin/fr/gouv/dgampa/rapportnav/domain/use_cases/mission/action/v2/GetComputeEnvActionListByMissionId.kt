package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.MapEnvActionControlPlans
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.GetInfractionsByActionId
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.FormattedEnvActionControlPlan
import org.slf4j.LoggerFactory

@UseCase
class GetComputeEnvActionListByMissionId(
    private val getEnvMissionById2: GetEnvMissionById2,
    private val mapControlPlans: MapEnvActionControlPlans,
    private val getInfractionsByActionId: GetInfractionsByActionId,
    getStatusForAction: GetStatusForAction,
    getControlByActionId: GetControlByActionId2
) : AbstractGetMissionAction(getStatusForAction, getControlByActionId) {
    private val logger = LoggerFactory.getLogger(GetComputeFishActionListByMissionId::class.java)

    fun execute(missionId: Int?): List<MissionEnvActionEntity> {
        if (missionId == null) {
            logger.error("GetComputeEnvActionListByMissionId received a null missionId")
            throw IllegalArgumentException("GetComputeEnvActionListByMissionId should not receive null missionId")
        }
        return try {
            val actions = getEnvActionList(missionId = missionId)
            processActions(missionId = missionId, actions = actions)
        } catch (e: Exception) {
            logger.error("GetFishActionsByMissionId failed loading Actions", e)
            return listOf()
        }
    }

    private fun getEnvActionList(missionId: Int): List<EnvActionEntity> {
        return getEnvMissionById2.execute(missionId = missionId)?.envActions ?: listOf()
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
}
