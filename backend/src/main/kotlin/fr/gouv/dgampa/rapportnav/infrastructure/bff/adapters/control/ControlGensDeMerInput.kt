package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import java.util.*

data class ControlGensDeMerInput(
    val id: UUID?,
    val missionId: Int,
    val actionControlId: String,
    val amountOfControls: Int,
    val unitShouldConfirm: Boolean?,
    val unitHasConfirmed: Boolean?,
    val staffOutnumbered: ControlResult?,
    val upToDateMedicalCheck: ControlResult?,
    val knowledgeOfFrenchLawAndLanguage: ControlResult?,
    val observations: String?,
) {
    fun toControlGensDeMerEntity(): ControlGensDeMerEntity {
        return ControlGensDeMerEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            actionControlId = actionControlId,
            amountOfControls = amountOfControls,
            unitShouldConfirm = unitShouldConfirm,
            unitHasConfirmed = unitHasConfirmed,
            staffOutnumbered = staffOutnumbered,
            upToDateMedicalCheck = upToDateMedicalCheck,
            knowledgeOfFrenchLawAndLanguage = knowledgeOfFrenchLawAndLanguage,
            observations = observations,
        )
    }
}
