package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

import java.time.Instant
import java.util.*

data class ActionNoteEnvEntity(
    override val id: UUID,
    override val actionStartDateTimeUtc: Instant? = null,
    override val actionEndDateTimeUtc: Instant? = null,
    override val observations: String? = null
) : ActionEnvEntity(
    actionType = ActionTypeEnum.NOTE,
    id = id,
)
