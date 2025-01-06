package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import java.time.Instant

class MissionFishActionDataInput(
    override val startDateTimeUtc: Instant,
    override val endDateTimeUtc: Instant? = null,
    override val observations: String? = null,
    override val controlSecurity: ControlSecurityInput2? = null,
    override val controlGensDeMer: ControlGensDeMerInput2? = null,
    override val controlNavigation: ControlNavigationInput2? = null,
    override val controlAdministrative: ControlAdministrativeInput2? = null,
) : MissionActionDataInput(
    startDateTimeUtc = startDateTimeUtc,
    endDateTimeUtc = endDateTimeUtc,
    controlSecurity = controlSecurity,
    controlGensDeMer = controlGensDeMer,
    controlNavigation = controlNavigation,
    controlAdministrative = controlAdministrative
){

    companion object {
        fun toMissionFishActionEntity(input: MissionActionInput): MissionFishActionEntity {
            val data = input.fish as MissionFishActionDataInput

            val action = MissionFishActionEntity(
                id = Integer.parseInt(input.id),
                missionId = input.missionId,
                fishActionType = MissionActionType.AIR_CONTROL,
                observationsByUnit = data.observations,
                actionDatetimeUtc = data.startDateTimeUtc,
                actionEndDatetimeUtc = data.endDateTimeUtc
            )
            return action
        }

    }
}
