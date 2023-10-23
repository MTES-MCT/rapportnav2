package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control

import java.util.*

data class ControlGensDeMer(
    val id: UUID,
    val confirmed: Boolean?,
    val staffOutnumbered: Boolean?,
    val upToDateMedicalCheck: Boolean?,
    val knowledgeOfFrenchLawAndLanguage: Boolean?,
    val observations: String?,
)
