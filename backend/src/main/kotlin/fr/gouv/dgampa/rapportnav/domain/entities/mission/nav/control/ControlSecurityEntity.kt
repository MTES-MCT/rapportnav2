package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

import java.time.ZonedDateTime
import java.util.*

data class ControlSecurityEntity(
    val id: UUID,
    val missionId: Int,
    val actionControlId: String,
    val amountOfControls: Int,
    val unitShouldConfirm: Boolean?,
    val unitHasConfirmed: Boolean?,
    val observations: String?,
) {

}
