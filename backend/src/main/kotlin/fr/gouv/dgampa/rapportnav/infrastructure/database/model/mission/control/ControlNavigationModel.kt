package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "control_navigation")
class ControlNavigationModel(
    // Add at least one parameter to the primary constructor
    @Id
    @Column(name = "id", unique = true, nullable = false)
    override var id: UUID,
) : ControlModel() {
    fun toControlNavigationEntity() = ControlNavigationEntity(
        id = id,
        missionId = missionId,
        actionControlId = actionControlId,
        amountOfControls = amountOfControls,
        unitHasConfirmed = unitHasConfirmed,
        unitShouldConfirm = unitShouldConfirm,
        observations = observations,
        infractions = infractions?.map { it.toInfractionEntity() }
    )

    companion object {
        private fun ControlNavigationModel.copyCommonProperties(control: ControlNavigationEntity) {
            this.id = control.id
            this.missionId = control.missionId
            this.actionControlId = control.actionControlId
            this.amountOfControls = control.amountOfControls
            this.unitHasConfirmed = control.unitHasConfirmed
            this.unitShouldConfirm = control.unitShouldConfirm
            this.observations = control.observations
        }
        fun fromControlNavigationEntity(control: ControlNavigationEntity): ControlNavigationModel {
            val controlModel = ControlNavigationModel(id=control.id)
            controlModel.copyCommonProperties(control)
            return controlModel
        }
    }
}
