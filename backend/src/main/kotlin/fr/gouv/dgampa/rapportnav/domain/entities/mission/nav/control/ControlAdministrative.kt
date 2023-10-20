package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

import java.time.ZonedDateTime
import java.util.*

data class ControlAdministrative(
    val id: UUID,
    val missionId: Int,
    val actionControlId: UUID,
    val confirmed: Boolean?,
    val compliantOperatingPermit: Boolean?,
    val upToDateNavigationPermit: Boolean?,
    val compliantSecurityDocuments: Boolean?,
    val observations: String?,
    val deletedAt: ZonedDateTime? = null,
)
