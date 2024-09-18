package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionFreeNoteEntity
import java.time.Instant
import java.util.*

class ActionFreeNoteInput(
    val id: UUID? = null,
    val missionId: Int,
    val startDateTimeUtc: Instant,
    val observations: String? = null

) {
    fun toActionFreeNoteEntity(): ActionFreeNoteEntity {
        return ActionFreeNoteEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            observations = observations
        )
    }
}
