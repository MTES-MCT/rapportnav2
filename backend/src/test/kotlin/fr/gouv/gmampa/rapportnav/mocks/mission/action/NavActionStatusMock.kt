package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

object NavActionStatusMock {

    fun createActionStatusEntity(
        status: ActionStatusType = ActionStatusType.NAVIGATING,
        startDateTimeUtc: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 12, 0), ZoneOffset.UTC),
        reason: ActionStatusReason? = null,
        observations: String? = "observations"
    ): ActionStatusEntity {
        return ActionStatusEntity(
            id = UUID.randomUUID(),
            missionId = 1,
            startDateTimeUtc = startDateTimeUtc,
            status = status,
            reason = reason,
            observations = observations
        )
    }
}
