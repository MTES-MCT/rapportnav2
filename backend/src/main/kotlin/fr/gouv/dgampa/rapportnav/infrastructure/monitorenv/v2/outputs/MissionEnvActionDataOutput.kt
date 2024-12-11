package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import java.time.ZonedDateTime
import java.util.*

abstract class MissionEnvActionDataOutput(
    open val id: UUID,
    open val actionStartDateTimeUtc: ZonedDateTime? = null,
    open val actionType: ActionTypeEnum,
    open val observationsByUnit: String? = null,
)
