package fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorEnv.envActions

import java.time.ZonedDateTime
import java.util.*

data class EnvActionNoteEntity(
    override val id: UUID,
    override val actionStartDateTimeUtc: ZonedDateTime? = null,
    override val actionEndDateTimeUtc: ZonedDateTime? = null,
    val observations: String? = null
) : EnvActionEntity(
    actionType = ActionTypeEnum.NOTE,
    id = id,
)
