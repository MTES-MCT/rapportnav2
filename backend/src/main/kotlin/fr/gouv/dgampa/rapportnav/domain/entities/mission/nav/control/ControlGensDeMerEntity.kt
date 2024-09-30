package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import java.util.*

data class ControlGensDeMerEntity(
    override var id: UUID,
    val missionId: Int,
    override val actionControlId: String,
    val amountOfControls: Int,
    override val unitShouldConfirm: Boolean? = null,
    override var unitHasConfirmed: Boolean? = null,
    val staffOutnumbered: ControlResult? = null,
    val upToDateMedicalCheck: ControlResult? = null,
    val knowledgeOfFrenchLawAndLanguage: ControlResult? = null,
    override val observations: String? = null,
    override val infractions: List<InfractionEntity>? = null
) : BaseControlEntity() {
    override fun shouldToggleOnUnitHasConfirmed(): Boolean =
        unitShouldConfirm == true &&
            unitHasConfirmed != true &&
            (staffOutnumbered != null ||
                upToDateMedicalCheck != null ||
                knowledgeOfFrenchLawAndLanguage != null ||
                infractions?.isNotEmpty() == true ||
                observations != null
                )
}
