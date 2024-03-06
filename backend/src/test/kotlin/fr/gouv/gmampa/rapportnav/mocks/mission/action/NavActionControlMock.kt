package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

object NavActionControlMock {

    fun createActionControlEntity(
        startDateTimeUtc: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 12, 0), ZoneOffset.UTC),
        observations: String? = "observations"
    ): ActionControlEntity {
        return ActionControlEntity(
            id = UUID.randomUUID(),
            missionId = 1,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = startDateTimeUtc.plusHours(2),
            controlMethod = ControlMethod.SEA,
            observations = observations
        )
    }
}
