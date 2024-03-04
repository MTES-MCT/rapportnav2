package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.GetControlByActionId

@UseCase
class AttachControlsToActionControl(
    private val getControlByActionId: GetControlByActionId,
) {
    fun toFishAction(
        actionId: String?,
        action: ExtendedFishActionEntity
    ): ExtendedFishActionEntity {
        if (actionId != null) {
            action.controlAction?.controlAdministrative =
                getControlByActionId.getControlAdministrative(actionControlId = actionId)
            action.controlAction?.controlSecurity =
                getControlByActionId.getControlSecurity(actionControlId = actionId)
            action.controlAction?.controlNavigation =
                getControlByActionId.getControlNavigation(
                    actionControlId = actionId
                )
            action.controlAction?.controlGensDeMer =
                getControlByActionId.getControlGensDeMer(actionControlId = actionId)

            return action
        }
        return action
    }

    fun toEnvAction(
        actionId: String?,
        action: ExtendedEnvActionEntity
    ): ExtendedEnvActionEntity {
        if (actionId != null) {
            action.controlAction?.controlAdministrative =
                getControlByActionId.getControlAdministrative(actionControlId = actionId)
            action.controlAction?.controlSecurity =
                getControlByActionId.getControlSecurity(actionControlId = actionId)
            action.controlAction?.controlNavigation =
                getControlByActionId.getControlNavigation(
                    actionControlId = actionId
                )
            action.controlAction?.controlGensDeMer =
                getControlByActionId.getControlGensDeMer(actionControlId = actionId)

            return action
        }
        return action
    }

    fun toNavAction(
        actionId: String?,
        action: ActionControlEntity
    ): ActionControlEntity {
        if (actionId != null) {
            action.controlAdministrative =
                getControlByActionId.getControlAdministrative(actionControlId = actionId)
            action.controlSecurity =
                getControlByActionId.getControlSecurity(actionControlId = actionId)
            action.controlNavigation =
                getControlByActionId.getControlNavigation(
                    actionControlId = actionId
                )
            action.controlGensDeMer =
                getControlByActionId.getControlGensDeMer(actionControlId = actionId)

            return action
        }
        return action
    }
}
