package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

data class ControlGensDeMer(
    val id: Int,
    val missionId: Int,
    val actionControlId: Int,
    val confirmed: Boolean?,
    val staffOutnumbered: Boolean?,
    val upToDateMedicalCheck: Boolean?,
    val knowledgeOfFrenchLawAndLanguage: Boolean?,
)
