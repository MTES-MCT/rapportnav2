package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import java.time.Instant
import java.util.*

object EnvActionNoteMock {
    fun create(
        id: UUID = UUID.randomUUID(),
        actionStartDateTimeUtc: Instant? = Instant.parse("2022-01-02T12:00:01Z"),
        actionEndDateTimeUtc: Instant? = Instant.parse("2022-01-02T14:00:01Z"),
    ): EnvActionNoteEntity {
        return EnvActionNoteEntity(
            id = id,
            actionStartDateTimeUtc = actionStartDateTimeUtc,
            actionEndDateTimeUtc = actionEndDateTimeUtc,

        )
    }
}
