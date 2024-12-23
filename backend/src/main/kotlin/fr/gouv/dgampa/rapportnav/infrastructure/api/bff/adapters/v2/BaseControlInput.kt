package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.BaseControlEntity
import java.util.*

abstract class BaseControlInput {
    open var id: UUID? = null
    var missionId: Int? = null
    var actionId: String? = null
    open val amountOfControls: Int? = null
    open val unitShouldConfirm: Boolean? = null
    open val unitHasConfirmed: Boolean? = null
    open var infractions: List<InfractionInput2>? = null
    open val observations: String? = null
    open val hasBeenDone: Boolean? = null

    abstract fun toEntity(): BaseControlEntity

    fun setMissionIdAndActionId(missionId: Int, actionId: String) {
        this.actionId = actionId
        this.missionId = missionId
        infractions?.forEach {
            it.setMissionIdAndActionId(actionId = actionId, missionId = missionId)
        }
    }
}
