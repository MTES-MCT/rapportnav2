package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

import java.time.Instant
import java.util.*

data class EnvActionNoteEntity(
    override val id: UUID,
    override val actionStartDateTimeUtc: Instant? = null,
    override val actionEndDateTimeUtc: Instant? = null,
    val observations: String? = null
) : EnvActionEntity(
    actionType = ActionTypeEnum.NOTE,
    id = id,
)
