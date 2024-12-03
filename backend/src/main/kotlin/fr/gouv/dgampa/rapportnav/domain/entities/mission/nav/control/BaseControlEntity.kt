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
}
