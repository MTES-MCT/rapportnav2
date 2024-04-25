package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRepresentationEntity
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.NavActionRepresentation
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime
import java.util.*

@Table(name = "mission_action_representation")
@Entity
data class ActionRepresentationModel(
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
) {

    fun toRepresentationEntity(): ActionRepresentationEntity {
        return ActionRepresentationEntity(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations
        )
    }

    companion object {
        fun fromRepresentation(representation: NavActionRepresentation, mapper: ObjectMapper) = ActionRepresentationModel(
            id = representation.id,
            missionId = representation.missionId,
            startDateTimeUtc = representation.startDateTimeUtc,
            endDateTimeUtc = representation.endDateTimeUtc,
            observations = representation.observations
        )
        }
    }


