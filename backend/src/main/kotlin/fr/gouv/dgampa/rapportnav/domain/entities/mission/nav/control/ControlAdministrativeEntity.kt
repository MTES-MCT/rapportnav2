package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlAdministrative
import java.time.ZonedDateTime
import java.util.*

data class ControlAdministrativeEntity(
    val id: UUID,
    val missionId: Int,
    val actionControlId: UUID,
    val amountOfControls: Int,
    val unitShouldConfirm: Boolean?,
    val unitHasConfirmed: Boolean?,
    val compliantOperatingPermit: ControlResult?,
    val upToDateNavigationPermit: ControlResult?,
    val compliantSecurityDocuments: ControlResult?,
    val observations: String?,
    val deletedAt: ZonedDateTime? = null,
) {
    fun toControlAdministrative(): ControlAdministrative {
        return ControlAdministrative(
            id = id,
            amountOfControls = amountOfControls,
            unitShouldConfirm = unitShouldConfirm,
            unitHasConfirmed = unitHasConfirmed,
            compliantOperatingPermit = compliantOperatingPermit,
            upToDateNavigationPermit = upToDateNavigationPermit,
            compliantSecurityDocuments = compliantSecurityDocuments,
            observations = observations
        )
    }
}
