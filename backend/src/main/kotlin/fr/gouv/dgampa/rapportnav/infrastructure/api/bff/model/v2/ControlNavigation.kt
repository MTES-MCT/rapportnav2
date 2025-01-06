package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import java.util.*

data class ControlNavigation(
    override var id: UUID? = null,
    override val amountOfControls: Int? = null,
    override val unitShouldConfirm: Boolean? = null,
    override var unitHasConfirmed: Boolean? = null,
    override var infractions: List<Infraction>? = null,
    override val observations: String? = null,
    override val hasBeenDone: Boolean? = null,
) : BaseControl() {
    override fun toEntity(): ControlNavigationEntity {
        return ControlNavigationEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId!!,
            actionControlId = actionId!!,
            amountOfControls = amountOfControls ?: 0,
            unitShouldConfirm = unitShouldConfirm,
            unitHasConfirmed = unitHasConfirmed,
            observations = observations,
            hasBeenDone = hasBeenDone
        )
    }

    companion object {
        fun fromControlNavigationEntity(entity: ControlNavigationEntity?): ControlNavigation {
            return ControlNavigation(
                id = entity?.id,
                amountOfControls = entity?.amountOfControls,
                unitShouldConfirm = entity?.unitShouldConfirm,
                unitHasConfirmed = entity?.unitHasConfirmed,
                observations = entity?.observations,
                hasBeenDone = entity?.hasBeenDone,
                infractions = entity?.infractions?.map { Infraction.fromInfractionEntity(it) }
            )
        }
    }
}
