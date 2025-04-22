package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import java.time.Instant
import java.util.*

object NavActionStatusMock {

    fun create(
        status: ActionStatusType = ActionStatusType.NAVIGATING,
        startDateTimeUtc: Instant = Instant.parse("2022-01-02T12:00:01Z"),
        reason: ActionStatusReason? = null,
        observations: String? = "observations",
        isCompleteForStats: Boolean? = null,
    ): ActionStatusEntity {
        return ActionStatusEntity(
            id = UUID.randomUUID(),
            missionId = "1",
            startDateTimeUtc = startDateTimeUtc,
            status = status,
            reason = reason,
            observations = observations,
            isCompleteForStats = isCompleteForStats
        )
    }
}
