package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

object NavActionControlMock {

    fun createActionControlEntity(
        startDateTimeUtc: Instant = Instant.parse("2022-01-02T12:00:00Z"),
        observations: String? = "observations",
        isCompleteForStats: Boolean? = null,
    ): ActionControlEntity {
        return ActionControlEntity(
            id = UUID.randomUUID(),
            missionId = 1,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = startDateTimeUtc.plus(2, ChronoUnit.HOURS),
            controlMethod = ControlMethod.SEA,
            observations = observations,
            isCompleteForStats = isCompleteForStats
        )
    }
}
