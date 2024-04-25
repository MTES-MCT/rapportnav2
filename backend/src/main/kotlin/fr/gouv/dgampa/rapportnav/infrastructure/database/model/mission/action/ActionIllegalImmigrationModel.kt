package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionIllegalImmigrationEntity
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.NavActionIllegalImmigration
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime
import java.util.*

@Table(name = "mission_action_illegal_immigration")
@Entity
data class ActionIllegalImmigrationModel(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "start_datetime_utc", nullable = false)
    var startDateTimeUtc: ZonedDateTime,

    @Column(name = "end_datetime_utc", nullable = false)
    var endDateTimeUtc: ZonedDateTime,

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
        fun fromIllegalImmigration(illegalImmigrationAction: NavActionIllegalImmigration, mapper: ObjectMapper) = ActionIllegalImmigrationModel(
            id = illegalImmigrationAction.id,
            missionId = illegalImmigrationAction.missionId,
            startDateTimeUtc = illegalImmigrationAction.startDateTimeUtc,
            endDateTimeUtc = illegalImmigrationAction.endDateTimeUtc,
            observations = illegalImmigrationAction.observations,
            nbOfInterceptedMigrants = illegalImmigrationAction.nbOfInterceptedMigrants,
            nbOfSuspectedSmugglers = illegalImmigrationAction.nbOfSuspectedSmugglers,
            nbOfInterceptedVessels = illegalImmigrationAction.nbOfInterceptedVessels,
            latitude = illegalImmigrationAction.latitude,
            longitude = illegalImmigrationAction.longitude
        )
        }
    }


