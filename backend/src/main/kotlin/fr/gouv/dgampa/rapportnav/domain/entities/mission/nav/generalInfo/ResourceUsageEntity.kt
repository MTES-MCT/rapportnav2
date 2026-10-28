package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo

import java.util.UUID

data class ResourceUsageEntity(
    var id: Int? = null,
    var missionIdUUID: UUID,
    var resourceId: Int,
    // ULAM, per resource
    var nbKms: Double? = null,
    var nbEngineHours: Double? = null,
    // PAM, on the unit's ship resource
    var distanceInNauticalMiles: Double? = null,
    var consumedGOInLiters: Double? = null,
    var consumedFuelInLiters: Double? = null
)
