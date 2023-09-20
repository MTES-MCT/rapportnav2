package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControl
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlEquipmentAndSecurityModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlGensDeMerModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationRulesModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlVesselAdministrativeModel
import jakarta.persistence.*

@Entity
@Table(name = "mission_action_control")
data class ActionControlModel(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mission_action_control_id_seq")
    @SequenceGenerator(name = "mission_action_control_id_seq", allocationSize = 1)
    @Column(name = "id")
    var id: Int,

    @Column(name = "mission_action_id", nullable = false, insertable = false, updatable = false)
    var actionId: Int,

    @OneToOne
    @JoinColumn(name = "mission_action_id", referencedColumnName = "id")
    @JsonIgnore
    var actionModel: ActionModel? = null,

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
            actionId = actionId,
            controlsVesselAdministrative = controlsVesselAdministrative?.toControlVesselAdministrative(),
            controlsGensDeMer = controlsGensDeMer?.toControlGensDeMer(),
            controlsNavigationRules = controlsNavigationRules?.toControlNavigationRules(),
            controlsEquipmentAndSecurity = controlsEquipmentAndSecurity?.toControlEquipmentAndSecurity()
        )
    }

}
