package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionFreeNoteEntity
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

object NavActionFreeNoteMock {
    fun create(
        startDateTimeUtc: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 12, 6), ZoneOffset.UTC),
        observations: String = "Largué, appareillé"
    ): ActionFreeNoteEntity {
        return ActionFreeNoteEntity(
            id = UUID.randomUUID(),
            missionId = 1,
            startDateTimeUtc = startDateTimeUtc,
            observations = observations,
        )
    }
}
