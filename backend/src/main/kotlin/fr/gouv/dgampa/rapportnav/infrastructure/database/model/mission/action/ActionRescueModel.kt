package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

@Entity
@Table(name = "mission_action_rescue")
class ActionRescueModel(
    @Id
    @Column(name = "id")
    val id: UUID,

    @Column(name = "mission_id", nullable = false)
    val missionId: Int,

    @Column(name = "is_complete_for_stats", nullable = true)
    var isCompleteForStats: Boolean? = null,

    @Column(name = "start_datetime_utc", nullable = false)
    val startDateTimeUtc: Instant,

    @Column(name = "end_datetime_utc", nullable = false)
    val endDateTimeUtc: Instant? = null,

    @Column(name = "latitude", nullable = true)
    var latitude: Float? = null,

    @Column(name = "longitude", nullable = true)
    var longitude: Float? = null,

    @Column(name = "is_vessel_rescue", nullable = true)
    val isVesselRescue: Boolean? = false,

    @Column(name = "is_person_rescue", nullable = true)
    val isPersonRescue: Boolean? = false,

    @Column(name = "is_vessel_noticed", nullable = true)
    val isVesselNoticed: Boolean? = false,

    @Column(name = "is_vessel_towed", nullable = true)
    val isVesselTowed: Boolean? = false,

    @Column(name = "observations", nullable = true)
    val observations: String?,

    @Column(name = "is_in_srr_or_followed_by_cross_mrcc", nullable = true)
    val isInSRRorFollowedByCROSSMRCC: Boolean? = false,

    @Column(name = "number_persons_rescued", nullable = true)
    val numberPersonsRescued: Int?,

    @Column(name = "number_of_deaths", nullable = true)
    val numberOfDeaths: Int?,

    @Column(name = "operation_follows_defrep", nullable = true)
    val operationFollowsDEFREP: Boolean? = false,

    @Column(name = "location_description", nullable = true)
    val locationDescription: String? = null,

    @Column(name = "is_migration_rescue", nullable = true)
    val isMigrationRescue: Boolean? = false,

    @Column(name = "nb_vessels_tracked_without_intervention", nullable = true)
    var nbOfVesselsTrackedWithoutIntervention: Int? = null,

    @Column(name = "nb_assisted_vessels_returning_to_shore", nullable = true)
    val nbAssistedVesselsReturningToShore: Int? = null,

    ) {

    fun toActionRescueEntity(): ActionRescueEntity {
        return ActionRescueEntity(
            id = id,
            missionId = missionId,
            isCompleteForStats = isCompleteForStats,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            latitude = latitude,
            longitude = longitude,
            observations = observations,
            isVesselTowed = isVesselTowed,
            numberPersonsRescued = numberPersonsRescued,
            numberOfDeaths = numberOfDeaths,
            operationFollowsDEFREP = operationFollowsDEFREP,
            isVesselNoticed = isVesselNoticed,
            isVesselRescue = isVesselRescue,
            isPersonRescue = isPersonRescue,
            isInSRRorFollowedByCROSSMRCC = isInSRRorFollowedByCROSSMRCC,
            locationDescription = locationDescription,
            isMigrationRescue = isMigrationRescue,
            nbOfVesselsTrackedWithoutIntervention = nbOfVesselsTrackedWithoutIntervention,
            nbAssistedVesselsReturningToShore = nbAssistedVesselsReturningToShore,
        )
    }

    companion object {
        fun fromActionRescueEntity(action: ActionRescueEntity) = ActionRescueModel(
            id = action.id,
            missionId = action.missionId,
            isCompleteForStats = action.isCompleteForStats,
            startDateTimeUtc = action.startDateTimeUtc,
            observations = action.observations,
            isPersonRescue = action.isPersonRescue,
            isVesselNoticed = action.isVesselNoticed,
            isVesselTowed = action.isVesselTowed,
            isInSRRorFollowedByCROSSMRCC = action.isInSRRorFollowedByCROSSMRCC,
            isVesselRescue = action.isVesselRescue,
            numberOfDeaths = action.numberOfDeaths,
            numberPersonsRescued = action.numberPersonsRescued,
            latitude = action.latitude,
            longitude = action.longitude,
            locationDescription = action.locationDescription,
            endDateTimeUtc = action.endDateTimeUtc,
            operationFollowsDEFREP = action.operationFollowsDEFREP,
            isMigrationRescue = action.isMigrationRescue,
            nbOfVesselsTrackedWithoutIntervention = action.nbOfVesselsTrackedWithoutIntervention,
            nbAssistedVesselsReturningToShore = action.nbAssistedVesselsReturningToShore,
        )
    }
}
