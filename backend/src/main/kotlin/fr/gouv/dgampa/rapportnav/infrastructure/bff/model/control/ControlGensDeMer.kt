package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import java.util.*

data class ControlGensDeMer(
    val id: UUID,
    val confirmed: Boolean?,
    val staffOutnumbered: ControlResult?,
    val upToDateMedicalCheck: ControlResult?,
    val knowledgeOfFrenchLawAndLanguage: ControlResult?,
    val observations: String?,
) {
    fun toControlGensDeMerEntity(missionId: Int, actionId: UUID): ControlGensDeMerEntity {
        return ControlGensDeMerEntity(
            id = id,
            missionId = missionId,
            actionControlId = actionId,
            confirmed = confirmed,
            staffOutnumbered = staffOutnumbered,
            upToDateMedicalCheck = upToDateMedicalCheck,
            knowledgeOfFrenchLawAndLanguage = knowledgeOfFrenchLawAndLanguage,
            observations = observations
        )
    }
}
