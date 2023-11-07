package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime
import java.util.*

@Entity
@Table(name = "control_security")
data class ControlSecurityModel(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    var id: UUID,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "action_control_id", nullable = false)
    var actionControlId: String,

    @Column(name = "amount_of_controls", nullable = false)
    var amountOfControls: Int = 1,

    @Column(name = "unit_should_confirm", nullable = true)
    var unitShouldConfirm: Boolean? = false,

    @Column(name = "unit_has_confirmed", nullable = true)
    var unitHasConfirmed: Boolean? = false,

    @Column(name = "observations", nullable = true)
    var observations: String? = null,

) {
    fun toControlSecurityEntity() = ControlSecurityEntity(
        id = id,
        missionId = missionId,
        actionControlId = actionControlId,
        amountOfControls = amountOfControls,
        unitHasConfirmed = unitHasConfirmed,
        unitShouldConfirm = unitShouldConfirm,
        observations = observations,
    )

    companion object {
        fun fromControlSecurityEntity(control: ControlSecurityEntity) = ControlSecurityModel(
            id = control.id,
            missionId = control.missionId,
            actionControlId = control.actionControlId,
            amountOfControls = control.amountOfControls,
            unitHasConfirmed = control.unitHasConfirmed,
            unitShouldConfirm = control.unitShouldConfirm,
            observations = control.observations,
        )
    }
}
