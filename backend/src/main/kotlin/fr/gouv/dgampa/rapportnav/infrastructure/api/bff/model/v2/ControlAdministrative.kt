package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import java.util.*

data class ControlAdministrative(
    override var id: UUID? = null,
    override val amountOfControls: Int? = null,
    override val unitShouldConfirm: Boolean? = null,
    override var unitHasConfirmed: Boolean? = null,
    override var infractions: List<Infraction>? = null,
    override val observations: String? = null,
    override val hasBeenDone: Boolean? = null,
    val compliantOperatingPermit: ControlResult? = null,
    val upToDateNavigationPermit: ControlResult? = null,
    val compliantSecurityDocuments: ControlResult? = null,
) : BaseControl() {
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
    companion object {
        fun fromControlAdministrativeEntity(entity: ControlAdministrativeEntity?): ControlAdministrative {
            return ControlAdministrative(
                id = entity?.id,
                amountOfControls = entity?.amountOfControls,
                unitShouldConfirm = entity?.unitShouldConfirm,
                unitHasConfirmed = entity?.unitHasConfirmed,
                observations = entity?.observations,
                hasBeenDone = entity?.hasBeenDone,
                compliantOperatingPermit = entity?.compliantOperatingPermit,
                upToDateNavigationPermit = entity?.upToDateNavigationPermit,
                compliantSecurityDocuments = entity?.compliantSecurityDocuments,
                infractions = entity?.infractions?.map { Infraction.fromInfractionEntity(it) }
            )
        }
    }
}
