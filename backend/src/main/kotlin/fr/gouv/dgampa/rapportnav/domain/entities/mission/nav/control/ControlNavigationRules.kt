package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

data class ControlNavigationRules(
    val id: Int,
    val missionId: Int,
    val actionControlId: Int,
    val confirmed: Boolean?,
    val observations: String?
)
