package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action

import java.time.Instant
import java.util.*

data class NavActionAntiPollution(
    var id: UUID,
    var missionId: Int,
    var startDateTimeUtc: Instant,
    var endDateTimeUtc: Instant? = null,
    var observations: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val detectedPollution: Boolean? = null,
    val pollutionObservedByAuthorizedAgent: Boolean? = null,
    val diversionCarriedOut: Boolean? = null,
    val isSimpleBrewingOperationDone: Boolean? = null,
    val isAntiPolDeviceDeployed: Boolean? = null,
) : ActionData()
