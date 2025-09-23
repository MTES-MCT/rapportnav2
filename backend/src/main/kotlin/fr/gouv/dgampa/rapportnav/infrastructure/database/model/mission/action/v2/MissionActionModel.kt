package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.FishingGearType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.LeisureType
import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Table(name = "mission_action")
@Entity
@EntityListeners(AuditingEntityListener::class)
data class MissionActionModel(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "mission_id", nullable = true)
    var missionId: Int? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    var actionType: ActionType,

    @Column(name = "is_complete_for_stats", nullable = true)
    var isCompleteForStats: Boolean? = null,

    @Column(name = "start_datetime_utc", nullable = false)
    var startDateTimeUtc: Instant,

    @Column(name = "end_datetime_utc", nullable = true)
    var endDateTimeUtc: Instant? = null,

    @Column(name = "observations", nullable = true, columnDefinition = "LONGTEXT")
    var observations: String? = null,

    @Column(name = "latitude", nullable = true)
    val latitude: Double? = null,

    @Column(name = "longitude", nullable = true)
    val longitude: Double? = null,

    @Column(name = "detected_pollution", nullable = true)
    val detectedPollution: Boolean? = null,

    @Column(name = "pollution_observed_by_authorized_agent", nullable = true)
    val pollutionObservedByAuthorizedAgent: Boolean? = null,

    @Column(name = "diversion_carried_out", nullable = true)
    val diversionCarriedOut: Boolean? = null,

    @Column(name = "simple_brewing_operation", nullable = true)
    val isSimpleBrewingOperationDone: Boolean? = null,

    @Column(name = "anti_pol_device_deployed", nullable = true)
    val isAntiPolDeviceDeployed: Boolean? = null,

    @Column(name = "control_method", nullable = true)
    var controlMethod: String? = null,

    @Column(name = "vessel_identifier", nullable = true)
    var vesselIdentifier: String? = null,

    @Column(name = "vessel_type", nullable = true)
    var vesselType: String? = null,

    @Column(name = "vessel_size", nullable = true)
    var vesselSize: String? = null,

    @Column(name = "identity_controlled_person", nullable = true)
    var identityControlledPerson: String? = null,

    @Column(name = "nb_of_intercepted_vessels", nullable = true)
    var nbOfInterceptedVessels: Int? = null,

    @Column(name = "nb_of_intercepted_migrants", nullable = true)
    val nbOfInterceptedMigrants: Int? = null,

    @Column(name = "nb_of_suspected_smugglers", nullable = true)
    val nbOfSuspectedSmugglers: Int? = null,
    @Column(name = "is_vessel_rescue", nullable = true)
    val isVesselRescue: Boolean? = false,

    @Column(name = "is_person_rescue", nullable = true)
    val isPersonRescue: Boolean? = false,

    @Column(name = "is_vessel_noticed", nullable = true)
    val isVesselNoticed: Boolean? = false,

    @Column(name = "is_vessel_towed", nullable = true)
    val isVesselTowed: Boolean? = false,

    @Column(name = "is_in_srr_or_followed_by_cross_mrcc", nullable = true)
    val isInSRRorFollowedByCROSSMRCC: Boolean? = false,

    @Column(name = "number_persons_rescued", nullable = true)
    val numberPersonsRescued: Int? = null,

    @Column(name = "number_of_deaths", nullable = true)
    val numberOfDeaths: Int? = null,

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

    @Column(name = "status", nullable = true)
    val status: String? = null,

    @Column(name = "reason", nullable = true)
    var reason: String? = null,

    @Column(name = "owner_id", nullable = true)
    var ownerId: UUID? = null,

    @Column(name = "nbr_of_hours", nullable = true)
    var nbrOfHours: Int? = null,

    @Column(name = "training_type", nullable = true)
    var trainingType: String? = null,

    @Column(name = "unit_management_training_type", nullable = true)
    var unitManagementTrainingType: String? = null,

    @Column(name = "is_within_department", nullable = true)
    var isWithinDepartment: Boolean? = null,

    @Column(name = "has_diving_during_operation", nullable = true)
    var hasDivingDuringOperation: Boolean? = null,

    @Column(name = "resource_type", nullable = true)
    var resourceType: String? = null,

    @Column(name = "resource_id", nullable = true)
    var resourceId: Int? = null,

    @Column(name = "siren", nullable = true)
    var siren: String? = null,

    @Column(name = "nbr_of_control", nullable = true)
    var nbrOfControl: Int? = null,

    @Column(name = "leisure_type", nullable = true)
    var leisureType: String? = null,

    @Column(name = "control_type", nullable = true)
    var controlType: String? = null,

    @Column(name = "fishing_gear_type", nullable = true)
    var fishingGearType: String? = null,

    @Column(name = "sector_type", nullable = true)
    val sectorType: String? = null, //SectorType

    @Column(name = "nbr_of_control_amp", nullable = true)
    var nbrOfControlAmp: Int? = null,

    @Column(name = "nbr_of_control_300m", nullable = true)
    var nbrOfControl300m: Int? = null,

    @Column(name = "is_control_during_security_day", nullable = true)
    var isControlDuringSecurityDay: Boolean? =null,

    @Column(name = "is_seizure_sleeping_fishing_gear", nullable = true)
    var isSeizureSleepingFishingGear: Boolean? = null,

    @Column(name = "sector_establishment_type", nullable = true)
    var sectorEstablishmentType:String? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null,

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    var createdBy: Int? = null,

    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: Int? = null

) {
    override fun hashCode(): Int {
        return Objects.hash(id)
    }

   override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MissionActionModel
        return id == other.id
    }
}
