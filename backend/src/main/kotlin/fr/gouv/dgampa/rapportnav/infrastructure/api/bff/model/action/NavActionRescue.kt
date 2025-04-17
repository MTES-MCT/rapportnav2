package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action

import java.time.Instant
import java.util.*

data class NavActionRescue(
    val id: UUID,
    val missionId: String,
    val startDateTimeUtc: Instant,
    val endDateTimeUtc: Instant? = null,
    val latitude: Float? = null,
    val longitude: Float? = null,
    val observations: String? = null,
    val isVesselRescue: Boolean? = false,
    val isPersonRescue: Boolean? = false,
    val isVesselTowed: Boolean? = false,
    val isInSRRorFollowedByCROSSMRCC: Boolean? = false,
    val isVesselNoticed: Boolean? = false,
    val numberPersonsRescued: Int? = null,
    val numberOfDeaths: Int? = null,
    val operationFollowsDEFREP: Boolean? = false,
    val locationDescription: String? = null,
    val isMigrationRescue: Boolean? = false,
    val nbAssistedVesselsReturningToShore: Int? = null,
    val nbOfVesselsTrackedWithoutIntervention: Int? = null,

    ) : ActionData()
