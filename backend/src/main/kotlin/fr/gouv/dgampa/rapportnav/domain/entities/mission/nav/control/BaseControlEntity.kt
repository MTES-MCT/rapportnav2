package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import java.util.*

abstract class BaseControlEntity {
    abstract var id: UUID
    abstract val actionControlId: String
    open val unitShouldConfirm: Boolean? = null
    open var unitHasConfirmed: Boolean? = null
    open val infractions: List<InfractionEntity>? = null
    open val observations: String? = null
    open val hasBeenDone: Boolean? = null
    abstract fun shouldToggleOnUnitHasConfirmed(): Boolean


    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + actionControlId.hashCode()
        result = 31 * result + (observations?.hashCode() ?: 0)
        result = 31 * result + (hasBeenDone?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as BaseControlEntity
        return (id == other.id
            && hasBeenDone == other.hasBeenDone
            && observations == other.observations
            && actionControlId == other.actionControlId)
    }
}
