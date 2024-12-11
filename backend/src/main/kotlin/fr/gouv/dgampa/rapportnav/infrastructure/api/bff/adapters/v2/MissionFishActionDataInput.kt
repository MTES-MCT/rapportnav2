package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import java.time.Instant

class MissionFishActionDataInput(
    override val startDateTimeUtc: Instant,
    override val endDateTimeUtc: Instant? = null,
    val observationsByUnit: String? = null,
    override val controlSecurity: ControlSecurityEntity? = null,
    override val controlGensDeMer: ControlGensDeMerEntity? = null,
    override val controlNavigation: ControlNavigationEntity? = null,
    override val controlAdministrative: ControlAdministrativeEntity? = null,
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
            val data = input.fish
            val action = MissionFishActionEntity(
                id = Integer.parseInt(input.id),
                missionId = input.missionId,
                fishActionType = MissionActionType.AIR_CONTROL,
                observationsByUnit = data?.observationsByUnit,
                actionDatetimeUtc = data?.startDateTimeUtc,
                actionEndDatetimeUtc = data?.endDateTimeUtc
            )

            action.controlSecurity = data?.controlSecurity
            action.controlGensDeMer = data?.controlGensDeMer
            action.controlNavigation = data?.controlNavigation
            action.controlAdministrative = data?.controlAdministrative
            return action
        }

    }
}
