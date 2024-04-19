package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionFreeNoteEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime
import java.util.*

@Entity
@Table(name = "mission_action_free_note")
class ActionFreeNoteModel(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "is_complete_for_stats", nullable = true)
    var isCompleteForStats: Boolean? = null,

    @Column(name = "start_datetime_utc", nullable = false)
    var startDateTimeUtc: ZonedDateTime,

    @Column(name = "observations", nullable = true, columnDefinition = "LONGTEXT")
    var observations: String? = null,
) {

    fun toActionFreeNoteEntity(): ActionFreeNoteEntity {
        return ActionFreeNoteEntity(
            id = id,
            missionId = missionId,
            isCompleteForStats = isCompleteForStats,
            startDateTimeUtc = startDateTimeUtc,
            observations = observations,
        )
    }

    companion object {
        fun fromActionFreeNote(freeNoteAction: ActionFreeNoteEntity, mapper: ObjectMapper) = ActionFreeNoteModel(
            id = freeNoteAction.id,
            missionId = freeNoteAction.missionId,
            isCompleteForStats = freeNoteAction.isCompleteForStats,
            startDateTimeUtc = freeNoteAction.startDateTimeUtc,
            observations = freeNoteAction.observations
        )
    }
}
