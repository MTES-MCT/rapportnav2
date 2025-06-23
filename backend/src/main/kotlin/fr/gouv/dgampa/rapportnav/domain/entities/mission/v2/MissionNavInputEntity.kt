package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import java.time.Instant

data class MissionNavInputEntity(
    val completedBy: String? = null,
    var startDateTimeUtc: Instant,
    var endDateTimeUtc: Instant? = null,
    var isDeleted: Boolean = false,
    var observationsByUnit: String? = null
)
