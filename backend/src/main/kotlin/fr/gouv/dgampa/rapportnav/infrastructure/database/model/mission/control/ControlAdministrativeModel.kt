package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.stringToControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.toStringOrNull
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "control_administrative")
class ControlAdministrativeModel(
    @Column(name = "compliant_operating_permit", nullable = true)
    var compliantOperatingPermit: String? = null,

    @Column(name = "up_to_date_navigation_permit", nullable = true)
    var upToDateNavigationPermit: String? = null,

    @Column(name = "compliant_security_documents", nullable = true)
    var compliantSecurityDocuments: String? = null,
) : ControlModel() {
    fun toControlAdministrativeEntity() = ControlAdministrativeEntity(
        id = id,
        missionId = missionId,
        actionControlId = actionControlId,
        amountOfControls = amountOfControls,
        unitHasConfirmed = unitHasConfirmed,
        unitShouldConfirm = unitShouldConfirm,
        compliantOperatingPermit = stringToControlResult(compliantOperatingPermit),
        upToDateNavigationPermit = stringToControlResult(upToDateNavigationPermit),
        compliantSecurityDocuments = stringToControlResult(compliantSecurityDocuments),
        observations = observations,
        infractions = infractions?.map { it.toInfractionEntity() }
    )

    companion object {
        private fun ControlAdministrativeModel.copyCommonProperties(control: ControlAdministrativeEntity) {
            this.id = control.id
            this.missionId = control.missionId
            this.actionControlId = control.actionControlId
            this.amountOfControls = control.amountOfControls
            this.unitHasConfirmed = control.unitHasConfirmed
            this.unitShouldConfirm = control.unitShouldConfirm
            this.observations = control.observations
        }

        fun fromControlAdministrativeEntity(control: ControlAdministrativeEntity): ControlAdministrativeModel {
            val controlModel = ControlAdministrativeModel()

            controlModel.copyCommonProperties(control)

            controlModel.compliantOperatingPermit = control.compliantOperatingPermit.toStringOrNull()
            controlModel.upToDateNavigationPermit = control.upToDateNavigationPermit.toStringOrNull()
            controlModel.compliantSecurityDocuments = control.compliantSecurityDocuments.toStringOrNull()

            return controlModel
        }
    }
}
