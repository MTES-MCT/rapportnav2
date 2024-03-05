package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

object NavActionStatusMock {
    fun create(): NavActionEntity {
        return NavActionEntity(
            id = UUID.randomUUID(),
            missionId = 1,
            startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 12, 0), ZoneOffset.UTC),
            endDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 14, 0), ZoneOffset.UTC),
            actionType = ActionType.STATUS,
        )
    }
}
