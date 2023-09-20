package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlEquipmentAndSecurity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionControlModel
import jakarta.persistence.*

@Entity
@Table(name = "control_equipment_security")
data class ControlEquipmentAndSecurityModel(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "control_equipment_security_id_seq")
    @SequenceGenerator(name = "control_equipment_security_id_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "action_control_id", nullable = false, insertable = false, updatable = false)
    var actionControlId: Int,

    @Column(name = "confirmed", nullable = true)
    var confirmed: Boolean? = false,

    @Column(name = "observations", nullable = true)
    var observations: String? = null,

    @OneToOne
    @JoinColumn(name = "action_control_id", referencedColumnName = "id")
    @JsonIgnore
    var actionControl: ActionControlModel
) {
    fun toControlEquipmentAndSecurity() = actionControl.id?.let {
        ControlEquipmentAndSecurity(
        id = id,
        missionId = missionId,
        actionControlId = it,
        confirmed = confirmed,
        observations = observations
    )
    }
}
