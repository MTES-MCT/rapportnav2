package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlGensDeMer
import java.time.ZonedDateTime
import java.util.*

data class ControlGensDeMerEntity(
    val id: UUID,
    val missionId: Int,
    val actionControlId: UUID,
    val amountOfControls: Int,
    val unitShouldConfirm: Boolean?,
    val unitHasConfirmed: Boolean?,
    val staffOutnumbered: ControlResult?,
    val upToDateMedicalCheck: ControlResult?,
    val knowledgeOfFrenchLawAndLanguage: ControlResult?,
    val observations: String?,
    val deletedAt: ZonedDateTime? = null,
) {
    fun toControlGensDeMer(): ControlGensDeMer {
        return ControlGensDeMer(
            id = id,
            amountOfControls = amountOfControls,
            unitShouldConfirm = unitShouldConfirm,
            unitHasConfirmed = unitHasConfirmed,
            staffOutnumbered = staffOutnumbered,
            upToDateMedicalCheck = upToDateMedicalCheck,
            knowledgeOfFrenchLawAndLanguage = knowledgeOfFrenchLawAndLanguage,
            observations = observations
        )
    }
}
