package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction.Infraction
import java.util.*

data class ControlGensDeMer(
    val id: UUID,
    val amountOfControls: Int,
    val unitShouldConfirm: Boolean?,
    val unitHasConfirmed: Boolean?,
    val staffOutnumbered: ControlResult?,
    val upToDateMedicalCheck: ControlResult?,
    val knowledgeOfFrenchLawAndLanguage: ControlResult?,
    val observations: String?,
    val infractions: List<Infraction>? = null
) {
    fun toControlGensDeMerEntity(missionId: Int, actionId: String): ControlGensDeMerEntity {
        return ControlGensDeMerEntity(
            id = id,
            missionId = missionId,
            actionControlId = actionId,
            amountOfControls = amountOfControls,
            unitShouldConfirm = unitShouldConfirm,
            unitHasConfirmed = unitHasConfirmed,
            staffOutnumbered = staffOutnumbered,
            upToDateMedicalCheck = upToDateMedicalCheck,
            knowledgeOfFrenchLawAndLanguage = knowledgeOfFrenchLawAndLanguage,
            observations = observations
        )
    }

    companion object {
        fun fromControlGensDeMerEntity(control: ControlGensDeMerEntity?) = control?.let {
            ControlGensDeMer(
                id = it.id,
                amountOfControls = control.amountOfControls,
                unitShouldConfirm = control.unitShouldConfirm,
                unitHasConfirmed = control.unitHasConfirmed,
                staffOutnumbered = control.staffOutnumbered,
                upToDateMedicalCheck = control.upToDateMedicalCheck,
                knowledgeOfFrenchLawAndLanguage = control.knowledgeOfFrenchLawAndLanguage,
                observations = control.observations,
                infractions = control.infractions?.map { Infraction.fromInfractionEntity(it) }
            )
        }
    }
}
