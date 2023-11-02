package fr.gouv.dgampa.rapportnav.domain.use_cases.missions

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.NavMissionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionControlRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlAdministrativeByActionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlGensDeMerByActionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlNavigationByActionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlSecurityByActionId
import org.slf4j.LoggerFactory

@UseCase
class GetNavMissionById(
    private val navActionControlRepository: INavActionControlRepository,
    private val navStatusRepository: INavActionStatusRepository,
    private val getControlAdministrativeByActionId: GetControlAdministrativeByActionId,
    private val getControlSecurityByActionId: GetControlSecurityByActionId,
    private val getControlNavigationByActionId: GetControlNavigationByActionId,
    private val getControlGensDeMerByActionId: GetControlGensDeMerByActionId,
    ) {
    private val logger = LoggerFactory.getLogger(GetNavMissionById::class.java)

    fun execute(missionId: Int): NavMissionEntity {
        val controls = navActionControlRepository
            .findAllByMissionId(missionId=missionId)
            .filter { it.deletedAt == null }
            .map { control ->
                control.controlAdministrative = getControlAdministrativeByActionId.execute(actionControlId = control.id.toString())
                control.controlSecurity = getControlSecurityByActionId.execute(actionControlId = control.id.toString())
                control.controlNavigation = getControlNavigationByActionId.execute(actionControlId = control.id.toString())
                control.controlGensDeMer = getControlGensDeMerByActionId.execute(actionControlId = control.id.toString())
                control.toNavAction()
            }


        val statuses = navStatusRepository.findAllByMissionId(missionId=missionId)
            .map { it.toActionStatusEntity() }
            .filter { it.deletedAt == null }
            .map { it.toNavAction() }
        val actions = controls + statuses
        val mission = NavMissionEntity(id = missionId, actions = actions)
        return mission
    }

}
