package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.MapEnvActionControlPlans
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.GetInfractionsByActionId
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.FormattedEnvActionControlPlan
import org.slf4j.LoggerFactory

@UseCase
class GetEnvActionById(
    private val getEnvMissionById2: GetEnvMissionById2,
    getStatusForAction: GetStatusForAction,
    getControlByActionId: GetControlByActionId2,
    private val mapControlPlans: MapEnvActionControlPlans,
    private val getInfractionsByActionId: GetInfractionsByActionId,
): AbstractGetMissionAction(getStatusForAction, getControlByActionId)  {
    private val logger = LoggerFactory.getLogger(GetFishActionListByMissionId::class.java)

    fun execute(missionId: Int?, actionId: String): MissionEnvActionEntity? {
        if (!isValidUUID(actionId)) return null
        if (missionId == null) {
            logger.error("GetEnvActionById received a null missionId")
            throw IllegalArgumentException("GetEnvActionById should not receive null missionId")
        }
        return try {
            val envAction = getEnvAction(missionId = missionId, actionId = actionId) ?: return null
            val entity = MissionEnvActionEntity.fromEnvAction(missionId, envAction)
            entity.status = this.getStatus(entity)
            entity.computeControls(controls = this.getControls(entity))
            entity.formattedControlPlans = getFormattedControlPlanList(entity.controlPlans)
            entity.navInfractions = getInfractionsByActionId.execute(entity.id.toString())
            entity.computeCompleteness()
            entity
        } catch (e: Exception) {
            logger.error("GetEnvActionById failed loading Action", e)
            return null
        }
    }

    private fun getFormattedControlPlanList(controlPlans: List<EnvActionControlPlanEntity>?): List<FormattedEnvActionControlPlan>? {
        val filteredControlPlans: ControlPlansEntity? = mapControlPlans.execute(controlPlans)
        return FormattedEnvActionControlPlan.fromControlPlansEntity(filteredControlPlans)
    }

    private fun getEnvAction(missionId: Int, actionId: String): EnvActionEntity? {
        return getEnvMissionById2.execute(missionId = missionId)?.envActions?.find { it.id.toString() == actionId }
    }

}
