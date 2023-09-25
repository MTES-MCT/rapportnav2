package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

import java.util.*

data class ControlVesselAdministrative(
    val id: UUID,
    val missionId: Int,
    val actionControlId: UUID,
    val confirmed: Boolean?,
    val compliantOperatingPermit: Boolean?,
    val upToDateNavigationPermit: Boolean?,
    val compliantSecurityDocuments: Boolean?,
)