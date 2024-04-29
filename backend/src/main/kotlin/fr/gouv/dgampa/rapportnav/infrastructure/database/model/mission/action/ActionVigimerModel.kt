package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionVigimerEntity
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.NavActionVigimer
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime
import java.util.*

@Table(name = "mission_action_vigimer")
@Entity
data class ActionVigimerModel(
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

    fun toActionVigimerEntity(): ActionVigimerEntity {
        return ActionVigimerEntity(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations
        )
    }

    companion object {
        fun fromVigimer(vigimer: NavActionVigimer, mapper: ObjectMapper) = ActionVigimerModel(
            id = vigimer.id,
            missionId = vigimer.missionId,
            startDateTimeUtc = vigimer.startDateTimeUtc,
            endDateTimeUtc = vigimer.endDateTimeUtc,
            observations = vigimer.observations
        )
        }
    }


