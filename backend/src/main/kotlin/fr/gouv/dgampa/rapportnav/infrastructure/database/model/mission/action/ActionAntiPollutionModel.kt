package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionAntiPollutionEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime
import java.util.*

@Table(name = "mission_action_anti_pollution")
@Entity
data class ActionAntiPollutionModel(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "is_complete_for_stats", nullable = true)
    var isCompleteForStats: Boolean? = null,

    @Column(name = "start_datetime_utc", nullable = false)
    var startDateTimeUtc: ZonedDateTime,

    @Column(name = "end_datetime_utc", nullable = false)
    var endDateTimeUtc: ZonedDateTime,

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
) {

    fun toAntiPollutionEntity(): ActionAntiPollutionEntity {
        return ActionAntiPollutionEntity(
            id = id,
            missionId = missionId,
            isCompleteForStats = isCompleteForStats,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations,
            latitude = latitude,
            longitude = longitude,
            detectedPollution = detectedPollution,
            pollutionObservedByAuthorizedAgent = pollutionObservedByAuthorizedAgent,
            diversionCarriedOut = diversionCarriedOut,
            isAntiPolDeviceDeployed = isAntiPolDeviceDeployed,
            isSimpleBrewingOperationDone = isSimpleBrewingOperationDone
        )
    }

    companion object {
        fun fromAntiPollutionEntity(action: ActionAntiPollutionEntity) = ActionAntiPollutionModel(
            id = action.id,
            missionId = action.missionId,
            isCompleteForStats = action.isCompleteForStats,
            startDateTimeUtc = action.startDateTimeUtc,
            endDateTimeUtc = action.endDateTimeUtc,
            observations = action.observations,
            latitude = action.latitude,
            longitude = action.longitude,
            detectedPollution = action.detectedPollution,
            pollutionObservedByAuthorizedAgent = action.pollutionObservedByAuthorizedAgent,
            diversionCarriedOut = action.diversionCarriedOut,
            isAntiPolDeviceDeployed = action.isAntiPolDeviceDeployed,
            isSimpleBrewingOperationDone = action.isSimpleBrewingOperationDone
        )
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ActionAntiPollutionModel
        return id == other.id
    }
}


