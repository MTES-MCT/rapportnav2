package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2

import java.time.Instant

open class MissionActionDataInput(
    open val startDateTimeUtc: Instant,
    open val endDateTimeUtc: Instant? = null,
    open val observations: String? = null,
    open val controlSecurity: ControlSecurityInput2? = null,
    open val controlGensDeMer: ControlGensDeMerInput2? = null,
    open val controlNavigation: ControlNavigationInput2? = null,
    open val controlAdministrative: ControlAdministrativeInput2? = null
){

    fun getControls(missionId: Int, actionId: String): ActionControlInput{
        this.computeMissionIdActionId(missionId, actionId)
        return ActionControlInput(
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
