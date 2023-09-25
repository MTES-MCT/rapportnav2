package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControl
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavAction
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlEquipmentAndSecurityModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlGensDeMerModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationRulesModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlVesselAdministrativeModel
import jakarta.persistence.*
import java.time.ZonedDateTime
import java.util.*

@Entity
@Table(name = "mission_action_control")
data class ActionControlModel(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "start_datetime_utc", nullable = false)
    var startDateTimeUtc: ZonedDateTime,

    @Column(name = "end_datetime_utc", nullable = true)
    var endDateTimeUtc: ZonedDateTime,

    @OneToOne(mappedBy = "actionControl")
    @JoinColumn(name = "action_control_id")
    var controlsVesselAdministrative: ControlVesselAdministrativeModel? = null,

    @OneToOne(mappedBy = "actionControl")
    @JoinColumn(name = "action_control_id")
    var controlsGensDeMer: ControlGensDeMerModel? = null,

    @OneToOne(mappedBy = "actionControl")
    @JoinColumn(name = "action_control_id")
    var controlsNavigationRules: ControlNavigationRulesModel? = null,

    @OneToOne(mappedBy = "actionControl")
    @JoinColumn(name = "action_control_id")
    var controlsEquipmentAndSecurity: ControlEquipmentAndSecurityModel? = null,
) {
    fun toActionControl(): ActionControl {
        return ActionControl(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            controlsVesselAdministrative = controlsVesselAdministrative?.toControlVesselAdministrative(),
            controlsGensDeMer = controlsGensDeMer?.toControlGensDeMer(),
            controlsNavigationRules = controlsNavigationRules?.toControlNavigationRules(),
            controlsEquipmentAndSecurity = controlsEquipmentAndSecurity?.toControlEquipmentAndSecurity()
        )
    }

        fun toNavAction(): NavAction {
            return NavAction(
                id = id,
                missionId = missionId,
                actionType = ActionType.CONTROL,
                actionStartDateTimeUtc = startDateTimeUtc,
                actionEndDateTimeUtc = endDateTimeUtc,
//                controlAction = controlAction?.toActionControl(),
            )
        }


}
