package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import java.util.*

data class ControlNavigationEntity(
    override var id: UUID,
    val missionId: String,
    override val actionControlId: String,
    val amountOfControls: Int,
    override val unitShouldConfirm: Boolean? = null,
    override var unitHasConfirmed: Boolean? = null,
    override val observations: String? = null,
    override val hasBeenDone: Boolean? = null,
    override var infractions: List<InfractionEntity>? = null
) : BaseControlEntity() {
    override fun shouldToggleOnUnitHasConfirmed(): Boolean =
        unitShouldConfirm == true &&
            unitHasConfirmed != true &&
            (
                infractions?.isNotEmpty() == true ||
                    observations != null
                )

    override fun hashCode(): Int {
        var result = missionId.hashCode()
        result = 31 * result + amountOfControls
        return super.hashCode() + result
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other::class != this::class) return false
        if (!super.equals(other)) return false
        other as ControlNavigationEntity
        return (missionId == other.missionId
            && amountOfControls == other.amountOfControls)
    }
}
