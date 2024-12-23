package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import java.util.*

data class ControlAdministrativeInput2(
    override var id: UUID? = null,
    override val amountOfControls: Int? = null,
    override val unitShouldConfirm: Boolean? = null,
    override var unitHasConfirmed: Boolean? = null,
    override var infractions: List<InfractionInput2>? = null,
    override val observations: String? = null,
    override val hasBeenDone: Boolean? = null,
    val compliantOperatingPermit: ControlResult? = null,
    val upToDateNavigationPermit: ControlResult? = null,
    val compliantSecurityDocuments: ControlResult? = null,
) : BaseControlInput() {
    override fun toEntity(): ControlAdministrativeEntity {
        return ControlAdministrativeEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId!!,
            actionControlId = actionId!!,
            amountOfControls = amountOfControls ?: 0,
            unitShouldConfirm = unitShouldConfirm,
            unitHasConfirmed = unitHasConfirmed,
            compliantOperatingPermit = compliantOperatingPermit,
            upToDateNavigationPermit = upToDateNavigationPermit,
            compliantSecurityDocuments = compliantSecurityDocuments,
            observations = observations,
            hasBeenDone = hasBeenDone
        )
    }
}
