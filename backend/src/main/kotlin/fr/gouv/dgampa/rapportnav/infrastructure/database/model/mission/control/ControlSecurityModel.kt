package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionControlModel
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "control_security")
data class ControlSecurityModel(
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
    var actionControl: ActionControlModel? = null
) {
    fun toControlSecurity() = ControlSecurity(
        id = id,
        missionId = missionId,
        actionControlId = actionControl!!.id,
        confirmed = confirmed,
        observations = observations
    )

    companion object {
        fun fromControlSecurity(control: ControlSecurity, mapper: ObjectMapper) = ControlSecurityModel(
            id = control.id,
            missionId = control.missionId,
            actionControlId = control.actionControlId,
            confirmed = control.confirmed,
            observations = control.observations,
        )
    }
}
