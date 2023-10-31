package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import java.time.ZonedDateTime
import java.util.*

data class ControlNavigationInput(
    val id: UUID?,
    val missionId: Int,
    val actionControlId: String,
    val amountOfControls: Int,
    val unitShouldConfirm: Boolean?,
    val unitHasConfirmed: Boolean?,
    val observations: String?,
    val deletedAt: ZonedDateTime? = null,
) {
    fun toControlNavigation(): ControlNavigationEntity {
        return ControlNavigationEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            actionControlId = actionControlId,
            amountOfControls = amountOfControls,
            unitShouldConfirm = unitShouldConfirm,
            unitHasConfirmed = unitHasConfirmed,
            observations = observations,
            deletedAt = deletedAt,
        )
    }
}
