package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import java.time.Instant
import java.util.*

class ActionRescueInput(
    val id: UUID? = null,
    val missionId: Int,
    val startDateTimeUtc: Instant,
    val endDateTimeUtc: Instant? = null,
    val latitude: Float? = null,
    val longitude: Float? = null,
    val isVesselRescue: Boolean? = false,
    val isPersonRescue: Boolean? = false,
    val isVesselNoticed: Boolean? = false,
    val isVesselTowed: Boolean? = false,
    val observations: String?,
    val isInSRRorFollowedByCROSSMRCC: Boolean? = false,
    val numberPersonsRescued: Int? = null,
    val numberOfDeaths: Int? = null,
    val operationFollowsDEFREP: Boolean? = false,
    val locationDescription: String? = null,
    val isMigrationRescue: Boolean? = false,
    val nbOfVesselsTrackedWithoutIntervention: Int? = null,
    val nbAssistedVesselsReturningToShore: Int? = null,
) {
    fun toActionRescueEntity(): ActionRescueEntity {
        return ActionRescueEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isVesselRescue = isVesselRescue,
            observations = observations,
            isPersonRescue = isPersonRescue,
            numberOfDeaths = numberOfDeaths,
            operationFollowsDEFREP = operationFollowsDEFREP,
            isVesselNoticed = isVesselNoticed,
            isVesselTowed = isVesselTowed,
            longitude = longitude,
            latitude = latitude,
            isInSRRorFollowedByCROSSMRCC = isInSRRorFollowedByCROSSMRCC,
            numberPersonsRescued = numberPersonsRescued,
            locationDescription = locationDescription,
            isMigrationRescue = isMigrationRescue,
            nbOfVesselsTrackedWithoutIntervention = nbOfVesselsTrackedWithoutIntervention,
            nbAssistedVesselsReturningToShore = nbAssistedVesselsReturningToShore,
        )
    }
}
