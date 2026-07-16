package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action

import java.time.Instant
import java.util.*

data class ActionFishInput(
    val missionId: UUID,
    val actionId: String,
    // MonitorFish external id of the owning mission; used to evict the Int-keyed fish caches.
    val externalId: Int? = null,
    val observationsByUnit: String? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
    val hasDivingDuringOperation: Boolean? = null,
    val incidentDuringOperation: Boolean? = null
)
