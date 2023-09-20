package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

data class ControlVesselAdministrative(
    val id: Int,
    val missionId: Int,
    val actionControlId: Int,
    val confirmed: Boolean?,
    val compliantOperatingPermit: Boolean?,
    val upToDateNavigationPermit: Boolean?,
    val compliantSecurityDocuments: Boolean?,
)
