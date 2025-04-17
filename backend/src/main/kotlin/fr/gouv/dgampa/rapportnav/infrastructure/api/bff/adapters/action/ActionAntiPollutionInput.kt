package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionAntiPollutionEntity
import java.time.Instant
import java.util.*

class ActionAntiPollutionInput(
    val id: UUID? = null,
    val missionId: String,
    val startDateTimeUtc: Instant,
    val endDateTimeUtc: Instant? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val detectedPollution: Boolean? = null,
    val pollutionObservedByAuthorizedAgent: Boolean? = null,
    val diversionCarriedOut: Boolean? = null,
    val observations: String? = null,
    val isSimpleBrewingOperationDone: Boolean? = null,
    val isAntiPolDeviceDeployed: Boolean? = null,

    ) {

    fun toActionAntiPollutionEntity(): ActionAntiPollutionEntity {
        return ActionAntiPollutionEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations,
            latitude = latitude,
            longitude = longitude,
            detectedPollution = detectedPollution,
            pollutionObservedByAuthorizedAgent = pollutionObservedByAuthorizedAgent,
            diversionCarriedOut = diversionCarriedOut,
            isSimpleBrewingOperationDone = isSimpleBrewingOperationDone,
            isAntiPolDeviceDeployed = isAntiPolDeviceDeployed,
        )
    }
}
