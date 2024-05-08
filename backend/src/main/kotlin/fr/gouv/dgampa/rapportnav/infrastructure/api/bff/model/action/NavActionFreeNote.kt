package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action

import java.time.ZonedDateTime
import java.util.*

data class NavActionFreeNote(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val observations: String? = null
) : ActionData()
