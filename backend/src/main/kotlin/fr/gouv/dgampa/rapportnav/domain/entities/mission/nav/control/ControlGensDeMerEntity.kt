package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

import java.time.ZonedDateTime
import java.util.*

data class ControlGensDeMerEntity(
    val id: UUID,
    val missionId: Int,
    val actionControlId: String,
    val amountOfControls: Int,
    val unitShouldConfirm: Boolean?,
    val unitHasConfirmed: Boolean?,
    val staffOutnumbered: ControlResult?,
    val upToDateMedicalCheck: ControlResult?,
    val knowledgeOfFrenchLawAndLanguage: ControlResult?,
    val observations: String?,
) {

}
