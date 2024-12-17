package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.InfractionsByVessel
import java.time.Instant
import java.util.*

class MissionEnvActionDataInput(
    override val startDateTimeUtc: Instant,
    override val endDateTimeUtc: Instant? = null,
    override val observations: String? = null,
    override val controlSecurity: ControlSecurityEntity? = null,
    override val controlGensDeMer: ControlGensDeMerEntity? = null,
    override val controlNavigation: ControlNavigationEntity? = null,
    override val controlAdministrative: ControlAdministrativeEntity? = null,
    val infractions: List<InfractionsByVessel>? = null
) : MissionActionDataInput(
    startDateTimeUtc = startDateTimeUtc,
    endDateTimeUtc = endDateTimeUtc,
    controlSecurity = controlSecurity,
    controlGensDeMer = controlGensDeMer,
    controlNavigation = controlNavigation,
    controlAdministrative = controlAdministrative
) {
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

            action.controlSecurity = data?.controlSecurity
            action.controlGensDeMer = data?.controlGensDeMer
            action.controlNavigation = data?.controlNavigation
            action.controlAdministrative = data?.controlAdministrative
            return action
        }
    }


}
