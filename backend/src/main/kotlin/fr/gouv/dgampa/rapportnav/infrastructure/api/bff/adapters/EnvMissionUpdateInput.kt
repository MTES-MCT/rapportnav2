package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters

import java.time.ZonedDateTime

data class EnvMissionUpdateInput(
    val id: Int,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime,
)
