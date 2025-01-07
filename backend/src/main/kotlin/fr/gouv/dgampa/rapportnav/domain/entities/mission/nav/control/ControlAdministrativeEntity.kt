package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import java.util.*

data class ControlAdministrativeEntity(
    override var id: UUID,
    val missionId: Int,
    override val actionControlId: String,
    val amountOfControls: Int,
    override val unitShouldConfirm: Boolean? = null,
    override var unitHasConfirmed: Boolean? = null,
    val compliantOperatingPermit: ControlResult? = null,
    val upToDateNavigationPermit: ControlResult? = null,
    val compliantSecurityDocuments: ControlResult? = null,
    override val observations: String? = null,
    override val hasBeenDone: Boolean? = null,
    override var infractions: List<InfractionEntity>? = null
) : BaseControlEntity() {
    override fun shouldToggleOnUnitHasConfirmed(): Boolean =
        unitShouldConfirm == true &&
            unitHasConfirmed != true &&
            (compliantOperatingPermit != null ||
                upToDateNavigationPermit != null ||
                compliantSecurityDocuments != null ||
                infractions?.isNotEmpty() == true ||
                observations != null
                )

    override fun hashCode(): Int {
        var result = missionId.hashCode()
        result = 31 * result + amountOfControls
        result = 31 * result + (compliantOperatingPermit?.hashCode() ?: 0)
        result = 31 * result + (upToDateNavigationPermit?.hashCode() ?: 0)
        result = 31 * result + (compliantSecurityDocuments?.hashCode() ?: 0)
        return super.hashCode() + result
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other::class != this::class) return false
        if (!super.equals(other)) return false
        other as ControlAdministrativeEntity
        return (missionId == other.missionId
            && amountOfControls == other.amountOfControls
            && compliantOperatingPermit == other.compliantOperatingPermit
            && upToDateNavigationPermit == other.upToDateNavigationPermit
            && compliantSecurityDocuments == other.compliantSecurityDocuments)
    }
}
