package fr.gouv.dgampa.rapportnav.domain.entities.control

import java.util.*

data class ControlGensDeMer(
    val id: UUID,
    val missionId: Int,
    val confirmed: Boolean?,
    val staffOutnumbered: Boolean?,
    val upToDateMedicalCheck: Boolean?,
    val knowledgeOfFrenchLawAndLanguage: Boolean?,
)
