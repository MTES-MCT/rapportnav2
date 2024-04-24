package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionBAAEMPermanenceEntity
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.NavActionBAAEMPermanence
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime
import java.util.*

@Table(name = "mission_action_baaem_permanence")
@Entity
data class ActionBAAEMPermanenceModel(
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

    fun toActionNauticalEventEntity(): ActionBAAEMPermanenceEntity {
        return ActionBAAEMPermanenceEntity(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations
        )
    }

    companion object {
        fun fromNauticalEvent(baaemPermanence: NavActionBAAEMPermanence, mapper: ObjectMapper) = ActionBAAEMPermanenceModel(
            id = baaemPermanence.id,
            missionId = baaemPermanence.missionId,
            startDateTimeUtc = baaemPermanence.startDateTimeUtc,
            endDateTimeUtc = baaemPermanence.endDateTimeUtc,
            observations = baaemPermanence.observations
        )
        }
    }


