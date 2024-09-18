package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action

import java.time.Instant
import java.util.*

data class NavActionFreeNote(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: Instant,
    val observations: String? = null
) : ActionData()
