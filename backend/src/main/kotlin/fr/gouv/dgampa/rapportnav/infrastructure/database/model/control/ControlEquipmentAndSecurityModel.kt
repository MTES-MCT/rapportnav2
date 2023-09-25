package fr.gouv.dgampa.rapportnav.infrastructure.database.model.control

import fr.gouv.dgampa.rapportnav.domain.entities.control.ControlEquipmentAndSecurity
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "control_equipment_security")
data class ControlEquipmentAndSecurityModel(
    @Id
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false)
    var id: UUID = UUID.randomUUID(),

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "confirmed", nullable = true)
    var confirmed: Boolean? = false,

    @Column(name = "observations", nullable = true)
    var observations: String? = null,
) {
    fun toControlEquipmentAndSecurity() = ControlEquipmentAndSecurity(
        id = id,
        missionId = missionId,
        confirmed = confirmed,
        observations = observations
    )
}
