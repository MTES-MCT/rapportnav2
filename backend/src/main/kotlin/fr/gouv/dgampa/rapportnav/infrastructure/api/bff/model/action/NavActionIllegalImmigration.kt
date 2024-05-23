package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action

import java.time.ZonedDateTime
import java.util.*

data class NavActionIllegalImmigration(
    var id: UUID,
    var missionId: Int,
    var startDateTimeUtc: ZonedDateTime,
    var endDateTimeUtc: ZonedDateTime,
    var observations: String? = null,
    var nbOfInterceptedVessels: Int? = null,
    var nbOfInterceptedMigrants: Int? = null,
    var nbOfSuspectedSmugglers: Int? = null,
    var latitude: Float? = null,
    var longitude: Float? = null
) : ActionData()
