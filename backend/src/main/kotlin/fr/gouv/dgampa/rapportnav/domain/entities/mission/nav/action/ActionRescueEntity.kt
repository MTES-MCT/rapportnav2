package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.NavActionRescue
import java.time.ZonedDateTime
import java.util.*

data class ActionRescueEntity(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime? = null,
    val latitude: Float? = null,
    val longitude: Float? = null,
    val isVesselRescue: Boolean? = false,
    val isPersonRescue: Boolean? = false,
    val isVesselNoticed: Boolean? = false,
    val isVesselTowed: Boolean? = false,
    val isInSRRorFollowedByCROSSMRCC: Boolean? = false,
    val numberPersonsRescued: Int?,
    val numberOfDeaths: Int?,
    val operationFollowsDEFREP: Boolean? = false,
    val observations: String?,
    val locationDescription: String? = null,
    val isMigrationRescue: Boolean? = false,
    val nbAssistedVesselsReturningToShore: Int? = null,
    val nbOfVesselsTrackedWithoutIntervention: Int? = null,
){
    fun toNavAction(): NavActionEntity {
        return NavActionEntity(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            actionType = ActionType.RESCUE,
            rescueAction = this
        )
    }

    fun toNavActionRescue(): NavActionRescue {
        return NavActionRescue(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isVesselTowed = isVesselTowed,
            isVesselNoticed = isVesselNoticed,
            isVesselRescue = isVesselRescue,
            isPersonRescue = isPersonRescue,
            observations = observations,
            numberOfDeaths = numberOfDeaths,
            operationFollowsDEFREP = operationFollowsDEFREP,
            latitude = latitude,
            longitude = longitude,
            isInSRRorFollowedByCROSSMRCC = isInSRRorFollowedByCROSSMRCC,
            numberPersonsRescued = numberPersonsRescued,
            locationDescription = locationDescription,
            isMigrationRescue = isMigrationRescue,
            nbAssistedVesselsReturningToShore = nbAssistedVesselsReturningToShore,
            nbOfVesselsTrackedWithoutIntervention = nbOfVesselsTrackedWithoutIntervention
        )
    }
}
