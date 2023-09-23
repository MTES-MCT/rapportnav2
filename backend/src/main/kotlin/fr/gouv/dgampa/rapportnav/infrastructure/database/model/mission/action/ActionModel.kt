package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.mapStringToActionType
import jakarta.persistence.*
import java.time.ZonedDateTime

@Entity
@Table(name = "mission_action")
data class ActionModel(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mission_action_id_seq")
    @SequenceGenerator(name = "mission_action_id_seq", allocationSize = 1)
    @Column(name = "id")
    var id: Int,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "action_start_datetime_utc", nullable = false)
    var actionStartDateTimeUtc: ZonedDateTime,

    @Column(name = "action_end_datetime_utc")
    var actionEndDateTimeUtc: ZonedDateTime?,

    @Column(name = "action_type", nullable = false)
    var actionType: String,

    @OneToOne(mappedBy = "actionModelForControl")
    @JoinColumn(name = "mission_action_id")
    var controlAction: ActionControlModel? = null,

    @OneToOne(mappedBy = "actionModelForStatus")
    @JoinColumn(name = "mission_action_id")
    var statusAction: ActionStatusModel? = null,
) {
    fun toNavAction(): NavAction {
        return NavAction(
            id = id,
            missionId = missionId,
            actionType = mapStringToActionType(actionType),
            actionStartDateTimeUtc = actionStartDateTimeUtc,
            actionEndDateTimeUtc = actionEndDateTimeUtc,
            controlAction = controlAction?.toActionControl(),
            statusAction = statusAction?.toActionStatus()
        )
    }
}
