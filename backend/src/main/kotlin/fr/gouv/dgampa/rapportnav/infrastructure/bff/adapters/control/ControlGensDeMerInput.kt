package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import java.util.*

data class ControlGensDeMerInput(
    val id: UUID?,
    val missionId: Int,
    val actionControlId: UUID,
    val confirmed: Boolean?,
    val staffOutnumbered: Boolean?,
    val upToDateMedicalCheck: Boolean?,
    val knowledgeOfFrenchLawAndLanguage: Boolean?,
    val observations: String?
) {
    fun toControlGensDeMer(): ControlGensDeMerEntity {
        return ControlGensDeMerEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            actionControlId = actionControlId,
            confirmed = confirmed,
            staffOutnumbered = staffOutnumbered,
            upToDateMedicalCheck = upToDateMedicalCheck,
            knowledgeOfFrenchLawAndLanguage = knowledgeOfFrenchLawAndLanguage,
            observations = observations
        )
    }
}
