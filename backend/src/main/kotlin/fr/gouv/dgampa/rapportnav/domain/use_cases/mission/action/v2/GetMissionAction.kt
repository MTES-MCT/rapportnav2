package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.v2.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2

open class GetMissionAction(
    private val getStatusForAction: GetStatusForAction,
    private val getControlByActionId: GetControlByActionId2
) {
    fun getControls(action: MissionActionEntity): ActionControlEntity? {
        if (!action.isControl()) return null
        return getControlByActionId.getAllControl(action.getActionId())
    }

    fun getStatus(action: MissionActionEntity): ActionStatusType? {
        if (action.status != null) return action.status
        return getStatusForAction.execute(
            missionId = action.missionId,
            actionStartDateTimeUtc = action.startDateTimeUtc
        )
    }
}
