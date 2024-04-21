package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.action

import ActionRescueEntity
import java.time.ZonedDateTime
import java.util.*

class ActionRescueInput(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime? = null,
    val latitude: Float? = null,
    val longitude: Float? = null,
    val isVesselRescue: Boolean = false,
    val isPersonRescue: Boolean = false,
    val isVesselNoticed: Boolean = false,
    val isVesselTowed: Boolean = false,
    val observations: String,
    val isInSRRorFollowedByCROSSMRCC: Boolean? = false,
    val numberPersonsRescued: Int? = null,
    val numberOfDeaths: Int? = null,
    val operationFollowsDEFREP: Boolean = false,
    val locationDescription: String? = null
)
{
    fun toActionRescueEntity(): ActionRescueEntity {
        return ActionRescueEntity(
            id = id,
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
            locationDescription = locationDescription
        )
    }
}
