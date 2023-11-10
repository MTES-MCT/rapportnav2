package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.stringToControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.toStringOrNull
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "control_gens_de_mer")
data class ControlGensDeMerModel(
    @Column(name = "staff_outnumbered", nullable = true)
    var staffOutnumbered: String? = null,

    @Column(name = "up_to_date_medical_check", nullable = true)
    var upToDateMedicalCheck: String? = null,

    @Column(name = "knowledge_of_french_law_and_language", nullable = true)
    var knowledgeOfFrenchLawAndLanguage: String? = null,
): ControlModel()  {
    fun toControlGensDeMerEntity() = ControlGensDeMerEntity(
        id = id,
        missionId = missionId,
        actionControlId = actionControlId,
        amountOfControls = amountOfControls,
        unitHasConfirmed = unitHasConfirmed,
        unitShouldConfirm = unitShouldConfirm,
        staffOutnumbered = stringToControlResult(staffOutnumbered),
        upToDateMedicalCheck = stringToControlResult(upToDateMedicalCheck),
        knowledgeOfFrenchLawAndLanguage = stringToControlResult(knowledgeOfFrenchLawAndLanguage),
        observations = observations,
        infractions = infractions?.map { it.toInfractionEntity() }
    )

    companion object {
        private fun ControlGensDeMerModel.copyCommonProperties(control: ControlGensDeMerEntity) {
            this.id = control.id
            this.missionId = control.missionId
            this.actionControlId = control.actionControlId
            this.amountOfControls = control.amountOfControls
            this.unitHasConfirmed = control.unitHasConfirmed
            this.unitShouldConfirm = control.unitShouldConfirm
            this.observations = control.observations
        }
        fun fromControlGensDeMerEntity(control: ControlGensDeMerEntity): ControlGensDeMerModel {
            val controlModel = ControlGensDeMerModel()

            controlModel.copyCommonProperties(control)

            controlModel.staffOutnumbered = control.staffOutnumbered.toStringOrNull()
            controlModel.upToDateMedicalCheck = control.upToDateMedicalCheck.toStringOrNull()
            controlModel.knowledgeOfFrenchLawAndLanguage = control.knowledgeOfFrenchLawAndLanguage.toStringOrNull()

            return controlModel
        }
    }
}
