package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionIllegalImmigrationEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

@Table(name = "mission_action_illegal_immigration")
@Entity
data class ActionIllegalImmigrationModel(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "is_complete_for_stats", nullable = true)
    var isCompleteForStats: Boolean? = null,

    @Column(name = "start_datetime_utc", nullable = false)
    var startDateTimeUtc: Instant,

    @Column(name = "end_datetime_utc", nullable = true)
    var endDateTimeUtc: Instant? = null,

    @Column(name = "observations", nullable = true, columnDefinition = "LONGTEXT")
    var observations: String? = null,

    @Column(name = "nb_of_intercepted_vessels", nullable = true)
    var nbOfInterceptedVessels: Int? = null,

    @Column(name = "nb_of_intercepted_migrants", nullable = true)
    val nbOfInterceptedMigrants: Int? = null,

    @Column(name = "nb_of_suspected_smugglers", nullable = true)
    val nbOfSuspectedSmugglers: Int? = null,

    @Column(name = "latitude", nullable = true)
    val latitude: Float? = null,

    @Column(name = "longitude", nullable = true)
    val longitude: Float? = null
) {

    fun toActionIllegalImmigrationEntity(): ActionIllegalImmigrationEntity {
        return ActionIllegalImmigrationEntity(
            id = id,
            missionId = missionId,
            isCompleteForStats = isCompleteForStats,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations,
            nbOfInterceptedVessels = nbOfInterceptedVessels,
            nbOfSuspectedSmugglers = nbOfSuspectedSmugglers,
            nbOfInterceptedMigrants = nbOfInterceptedMigrants,
            latitude = latitude,
            longitude = longitude
        )
    }

    companion object {
        fun fromIllegalImmigrationEntity(action: ActionIllegalImmigrationEntity) =
            ActionIllegalImmigrationModel(
                id = action.id,
                missionId = action.missionId,
                isCompleteForStats = action.isCompleteForStats,
                startDateTimeUtc = action.startDateTimeUtc,
                endDateTimeUtc = action.endDateTimeUtc,
                observations = action.observations,
                nbOfInterceptedMigrants = action.nbOfInterceptedMigrants,
                nbOfSuspectedSmugglers = action.nbOfSuspectedSmugglers,
                nbOfInterceptedVessels = action.nbOfInterceptedVessels,
                latitude = action.latitude,
                longitude = action.longitude
            )
    }
}


