package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionVigimerEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

@Table(name = "mission_action_vigimer")
@Entity
data class ActionVigimerModel(
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
) {

    fun toActionVigimerEntity(): ActionVigimerEntity {
        return ActionVigimerEntity(
            id = id,
            missionId = missionId,
            isCompleteForStats = isCompleteForStats,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations
        )
    }

    companion object {
        fun fromVigimerEntity(action: ActionVigimerEntity) = ActionVigimerModel(
            id = action.id,
            missionId = action.missionId,
            isCompleteForStats = action.isCompleteForStats,
            startDateTimeUtc = action.startDateTimeUtc,
            endDateTimeUtc = action.endDateTimeUtc,
            observations = action.observations
        )
    }
}


