package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*

@Entity
@Table(name = "control_security")
@EntityListeners(AuditingEntityListener::class)
class ControlSecurityModel(
    // Add at least one parameter to the primary constructor
    @Id
    @Column(name = "id", unique = true, nullable = false)
    override var id: UUID,
) : ControlModel() {
    fun toControlSecurityEntity() = ControlSecurityEntity(
        id = id,
        missionId = missionId,
        actionControlId = actionControlId,
        amountOfControls = amountOfControls,
        unitHasConfirmed = unitHasConfirmed,
        unitShouldConfirm = unitShouldConfirm,
        observations = observations,
        hasBeenDone = hasBeenDone,
        infractions = infractions?.map { it.toInfractionEntity() }
    )

    companion object {
        private fun ControlSecurityModel.copyCommonProperties(control: ControlSecurityEntity) {
            this.id = control.id
            this.missionId = control.missionId
            this.actionControlId = control.actionControlId
            this.amountOfControls = control.amountOfControls
            this.unitHasConfirmed = control.unitHasConfirmed
            this.unitShouldConfirm = control.unitShouldConfirm
            this.observations = control.observations
            this.hasBeenDone = control.hasBeenDone
        }

        fun fromControlSecurityEntity(control: ControlSecurityEntity): ControlSecurityModel {
            val controlModel = ControlSecurityModel(id = control.id)
            controlModel.copyCommonProperties(control)
            return controlModel
        }
    }
}
