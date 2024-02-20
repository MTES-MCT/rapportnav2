package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.NavActionFreeNote
import java.time.ZonedDateTime
import java.util.*

data class ActionFreeNoteEntity(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val observations: String? = null
) {
    fun toNavAction(): NavActionEntity {
        return NavActionEntity(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = null,
            actionType = ActionType.NOTE,
            freeNoteAction = this
        )
    }

    fun toNavActionFreeNote(): NavActionFreeNote {
        return NavActionFreeNote(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            observations = observations
        )
    }
}
