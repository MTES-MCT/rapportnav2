package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control

import java.util.*

data class ControlAdministrative(
    val id: UUID,
    val confirmed: Boolean?,
    val compliantOperatingPermit: Boolean?,
    val upToDateNavigationPermit: Boolean?,
    val compliantSecurityDocuments: Boolean?,
    val observations: String?,
)
