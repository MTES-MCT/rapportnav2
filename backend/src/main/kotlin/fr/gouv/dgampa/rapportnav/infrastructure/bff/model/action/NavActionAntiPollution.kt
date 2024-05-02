package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action

import java.time.ZonedDateTime
import java.util.*

data class NavActionAntiPollution(
    var id: UUID,
    var missionId: Int,
    var startDateTimeUtc: ZonedDateTime,
    var endDateTimeUtc: ZonedDateTime,
    var observations: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val detectedPollution: Boolean? = null,
    val pollutionObservedByAuthorizedAgent: Boolean? = null,
    val diversionCarriedOut: Boolean? = null,
) : ActionData()
