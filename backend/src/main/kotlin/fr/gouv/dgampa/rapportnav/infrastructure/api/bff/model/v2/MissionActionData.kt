package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import java.time.Instant

open class MissionActionData(
    open val startDateTimeUtc: Instant? = null,
    open val endDateTimeUtc: Instant? = null,
    open val observations: String? = null,
    open val targets: List<Target>? = null,
    open val hasDivingDuringOperation: Boolean? = null,
    open val incidentDuringOperation: Boolean? = null
)
