package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import java.time.Instant
import java.util.*

data class NavActionStatus(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: Instant,
    val status: ActionStatusType,
    val reason: ActionStatusReason? = null,
    val observations: String? = null
) : ActionData()
