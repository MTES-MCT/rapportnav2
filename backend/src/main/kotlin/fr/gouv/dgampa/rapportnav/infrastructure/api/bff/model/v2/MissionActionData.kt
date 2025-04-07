package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import java.time.Instant

open class MissionActionData(
    open val startDateTimeUtc: Instant? = null,
    open val endDateTimeUtc: Instant? = null,
    open val observations: String? = null,
    open val controlSecurity: ControlSecurity? = null,
    open val controlGensDeMer: ControlGensDeMer? = null,
    open val controlNavigation: ControlNavigation? = null,
    open val controlAdministrative: ControlAdministrative? = null,
    open val targets: List<Target2>? = null
){

    fun getControls(missionId: Int, actionId: String): ActionControl {
        this.computeMissionIdActionId(missionId, actionId)
        return ActionControl(
            controlSecurity = controlSecurity,
            controlGensDeMer = controlGensDeMer,
            controlNavigation = controlNavigation,
            controlAdministrative = controlAdministrative
        )
    }

    private fun computeMissionIdActionId(missionId: Int, actionId: String) {
        this.controlSecurity?.setMissionIdAndActionId(missionId = missionId, actionId = actionId)
        this.controlGensDeMer?.setMissionIdAndActionId(missionId = missionId, actionId = actionId)
        this.controlNavigation?.setMissionIdAndActionId(missionId = missionId, actionId = actionId)
        this.controlAdministrative?.setMissionIdAndActionId(missionId = missionId, actionId = actionId)
    }
}
