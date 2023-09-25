package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlEquipmentAndSecurity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionControlModel
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "control_equipment_security")
data class ControlEquipmentAndSecurityModel(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    var id: UUID,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "action_control_id", nullable = false, insertable = false, updatable = false)
    var actionControlId: UUID,

    @Column(name = "confirmed", nullable = true)
    var confirmed: Boolean? = false,

    @Column(name = "observations", nullable = true)
    var observations: String? = null,

    @OneToOne
    @JoinColumn(name = "action_control_id", referencedColumnName = "id")
    @JsonIgnore
    var actionControl: ActionControlModel
) {
    fun toControlEquipmentAndSecurity() = ControlEquipmentAndSecurity(
        id = id,
        missionId = missionId,
        actionControlId = actionControl.id,
        confirmed = confirmed,
        observations = observations
    )
}
