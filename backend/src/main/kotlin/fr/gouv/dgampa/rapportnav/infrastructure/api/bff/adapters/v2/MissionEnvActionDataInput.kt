package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import java.time.Instant
import java.util.*

class MissionEnvActionDataInput(
    override val startDateTimeUtc: Instant,
    override val endDateTimeUtc: Instant? = null,
    override val observations: String? = null,
    override val controlSecurity: ControlSecurityInput2? = null,
    override val controlGensDeMer: ControlGensDeMerInput2? = null,
    override val controlNavigation: ControlNavigationInput2? = null,
    override val controlAdministrative: ControlAdministrativeInput2? = null,
    val infractions: List<InfractionByTargetInput2>? = null
) : MissionActionDataInput(
    startDateTimeUtc = startDateTimeUtc,
    endDateTimeUtc = endDateTimeUtc,
    controlSecurity = controlSecurity,
    controlGensDeMer = controlGensDeMer,
    controlNavigation = controlNavigation,
    controlAdministrative = controlAdministrative
) {

    fun getInfractions(missionId: Int, actionId: String): List<InfractionInput2>? {
        val infractions = infractions?.flatMap { it.infractions }
            //?.filter { it.controlId != null || it.controlType != null }
        infractions?.forEach { it.setMissionIdAndActionId(missionId, actionId) }
        return infractions
    }

    companion object {
        fun toMissionEnvActionEntity(input: MissionActionInput): MissionEnvActionEntity {
            val data = input.env
            val action = MissionEnvActionEntity(
                id = UUID.fromString(input.id),
                missionId = input.missionId,
                endDateTimeUtc = data?.endDateTimeUtc,
                startDateTimeUtc = data?.startDateTimeUtc,
                observationsByUnit = data?.observations,
                envActionType = ActionTypeEnum.valueOf(input.actionType.toString())
            )
            return action
        }
    }


}
