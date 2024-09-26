package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action

import java.time.Instant
import java.util.*

data class NavActionIllegalImmigration(
    var id: UUID,
    var missionId: Int,
    var startDateTimeUtc: Instant,
    var endDateTimeUtc: Instant? = null,
    var observations: String? = null,
    var nbOfInterceptedVessels: Int? = null,
    var nbOfInterceptedMigrants: Int? = null,
    var nbOfSuspectedSmugglers: Int? = null,
    var latitude: Float? = null,
    var longitude: Float? = null
) : ActionData()
