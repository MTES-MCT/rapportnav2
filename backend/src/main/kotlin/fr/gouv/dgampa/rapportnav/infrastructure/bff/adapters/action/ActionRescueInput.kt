package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ActionRescueInput(
    val id: UUID? = null,
    val missionId: Int,
    val startDateTimeUtc: String,
    val endDateTimeUtc: String? = null,
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
)
{
    fun toActionRescueEntity(): ActionRescueEntity {
        return ActionRescueEntity(
            id = id?: UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc.let {
                ZonedDateTime.parse(it, DateTimeFormatter.ISO_ZONED_DATE_TIME)
            } ?: ZonedDateTime.now(ZoneId.of("UTC")),
            endDateTimeUtc = endDateTimeUtc?.let {
                ZonedDateTime.parse(it, DateTimeFormatter.ISO_ZONED_DATE_TIME)
            } ?: ZonedDateTime.now(ZoneId.of("UTC")),
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
