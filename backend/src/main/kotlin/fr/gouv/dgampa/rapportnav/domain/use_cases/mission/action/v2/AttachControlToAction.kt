package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import org.springframework.stereotype.Component

@UseCase
@Component
class AttachControlToAction(
    private val getControlByActionId: GetControlByActionId2
) {
    fun execute(action: MissionActionEntity): MissionActionEntity {
        if (!action.isControl()) return action
        val controls = getControlByActionId.getAllControl(action.getActionId())
        action.controlSecurity = controls?.controlSecurity
        action.controlGensDeMer = controls?.controlGensDeMer
        action.controlNavigation = controls?.controlNavigation
        action.controlAdministrative = controls?.controlAdministrative
        return action;
    }
}
