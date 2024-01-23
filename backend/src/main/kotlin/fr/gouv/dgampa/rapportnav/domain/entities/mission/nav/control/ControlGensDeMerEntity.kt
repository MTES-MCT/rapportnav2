package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import java.util.*

data class ControlGensDeMerEntity(
    var id: UUID,
    val missionId: Int,
    val actionControlId: String,
    val amountOfControls: Int,
    val unitShouldConfirm: Boolean? = null,
    val unitHasConfirmed: Boolean? = null,
    val staffOutnumbered: ControlResult? = null,
    val upToDateMedicalCheck: ControlResult? = null,
    val knowledgeOfFrenchLawAndLanguage: ControlResult? = null,
    val observations: String? = null,
    val infractions: List<InfractionEntity>? = null
) {

}
