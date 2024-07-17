package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionFreeNoteEntity
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ActionFreeNoteInput(
    val id: UUID? = null,
    val missionId: Int,
    val startDateTimeUtc: String,
    val observations: String? = null

) {
    fun toActionFreeNoteEntity(): ActionFreeNoteEntity {
        return ActionFreeNoteEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc.let {
                ZonedDateTime.parse(it, DateTimeFormatter.ISO_ZONED_DATE_TIME)
            } ?: ZonedDateTime.now(ZoneId.of("UTC")),
            observations = observations
        )
    }
}
