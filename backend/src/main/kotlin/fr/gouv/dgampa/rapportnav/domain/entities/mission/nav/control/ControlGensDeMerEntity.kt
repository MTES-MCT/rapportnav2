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
    override val hasBeenDone: Boolean? = null,
    override var infractions: List<InfractionEntity>? = null
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

    override fun hashCode(): Int {
        var result = missionId.hashCode()
        result = 31 * result + amountOfControls
        result = 31 * result + (staffOutnumbered?.hashCode() ?: 0)
        result = 31 * result + (upToDateMedicalCheck?.hashCode() ?: 0)
        result = 31 * result + (knowledgeOfFrenchLawAndLanguage?.hashCode() ?: 0)
        return super.hashCode() + result
    }

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false
        other as ControlGensDeMerEntity
        return (missionId == other.missionId
            && amountOfControls == other.amountOfControls
            && staffOutnumbered == other.staffOutnumbered
            && upToDateMedicalCheck == other.upToDateMedicalCheck
            && knowledgeOfFrenchLawAndLanguage == other.knowledgeOfFrenchLawAndLanguage)
    }
}
