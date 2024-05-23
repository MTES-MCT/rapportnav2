package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.Infraction
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
    val infractions: List<Infraction>? = null
) {
    fun toControlAdministrativeEntity(missionId: Int, actionId: String): ControlAdministrativeEntity {
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
            observations = observations,
//            infractions = infractions?.map { it.toInfractionEntity() }
        )
    }

    companion object {
        fun fromControlAdministrativeEntity(control: ControlAdministrativeEntity?) = control?.let {
            ControlAdministrative(
                id = it.id,
                amountOfControls = control.amountOfControls,
                unitShouldConfirm = control.unitShouldConfirm,
                unitHasConfirmed = control.unitHasConfirmed,
                compliantOperatingPermit = control.compliantOperatingPermit,
                upToDateNavigationPermit = control.upToDateNavigationPermit,
                compliantSecurityDocuments = control.compliantSecurityDocuments,
                observations = control.observations,
                infractions = control.infractions?.map { Infraction.fromInfractionEntity(it) }
            )
        }
    }
}
