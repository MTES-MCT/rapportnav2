package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import java.util.*

data class ControlAdministrative(
    val id: UUID,
    val amountOfControls: Int,
    val unitShouldConfirm: Boolean?,
    val unitHasConfirmed: Boolean?,
    val compliantOperatingPermit: ControlResult?,
    val upToDateNavigationPermit: ControlResult?,
    val compliantSecurityDocuments: ControlResult?,
    val observations: String?,
) {
    fun toControlAdministrativeEntity(missionId: Int, actionId: UUID): ControlAdministrativeEntity {
        return ControlAdministrativeEntity(
            id = id,
            missionId = missionId,
            actionControlId = actionId,
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
