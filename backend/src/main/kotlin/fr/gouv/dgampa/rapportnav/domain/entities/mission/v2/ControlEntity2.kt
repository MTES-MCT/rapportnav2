package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.v2.ControlModel2
import java.util.*

data class ControlEntity2(
    var id: UUID,
    var controlType: ControlType,
    val amountOfControls: Int,
    val infractions: List<InfractionEntity2>? = null,
    val observations: String? = null,
    val compliantOperatingPermit: ControlResult? = null,
    val upToDateNavigationPermit: ControlResult? = null,
    val compliantSecurityDocuments: ControlResult? = null,
    val staffOutnumbered: ControlResult? = null,
    val upToDateMedicalCheck: ControlResult? = null,
    val hasBeenDone: Boolean? = null,
    val knowledgeOfFrenchLawAndLanguage: ControlResult? = null,
    val nbrOfHours: Int? = null
) {
    fun toControlModel(): ControlModel2 {
        return ControlModel2(
            id = id,
            nbrOfHours = nbrOfHours,
            controlType = controlType,
            hasBeenDone = hasBeenDone,
            observations = observations,
            amountOfControls = amountOfControls,
            staffOutnumbered = staffOutnumbered?.toString(),
            infractions = infractions?.map { it.toInfractionModel() },
            upToDateMedicalCheck = upToDateMedicalCheck?.toString(),
            compliantOperatingPermit = compliantOperatingPermit?.toString(),
            upToDateNavigationPermit = upToDateNavigationPermit?.toString(),
            compliantSecurityDocuments = compliantSecurityDocuments?.toString(),
            knowledgeOfFrenchLawAndLanguage = knowledgeOfFrenchLawAndLanguage?.toString(),
        )
    }


    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (observations?.hashCode() ?: 0)
        result = 31 * result + (hasBeenDone?.hashCode() ?: 0)
        result = 31 * result + controlType.hashCode()
        result = 31 * result + infractions.hashCode()
        result = 31 * result + amountOfControls.hashCode()
        result = 31 * result + (compliantOperatingPermit?.hashCode() ?: 0)
        result = 31 * result + (upToDateNavigationPermit?.hashCode() ?: 0)
        result = 31 * result + (compliantSecurityDocuments?.hashCode() ?: 0)
        result = 31 * result + (staffOutnumbered?.hashCode() ?: 0)
        result = 31 * result + (upToDateMedicalCheck?.hashCode() ?: 0)
        result = 31 * result + (hasBeenDone?.hashCode() ?: 0)
        result = 31 * result + (knowledgeOfFrenchLawAndLanguage?.hashCode() ?: 0)
        result = 31 * result + (nbrOfHours?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ControlEntity2
        return (id == other.id
            && nbrOfHours == other.nbrOfHours
            && controlType == other.controlType
            && hasBeenDone == other.hasBeenDone
            && observations == other.observations
            && amountOfControls == other.amountOfControls
            && infractions?.size == other.infractions?.size
            && staffOutnumbered == other.staffOutnumbered
            && upToDateMedicalCheck == other.upToDateMedicalCheck
            && infractions?.toSet() == other.infractions?.toSet()
            && compliantOperatingPermit == other.compliantOperatingPermit
            && upToDateNavigationPermit == other.upToDateNavigationPermit
            && compliantSecurityDocuments == other.compliantSecurityDocuments
            && knowledgeOfFrenchLawAndLanguage == other.knowledgeOfFrenchLawAndLanguage
            )
    }

    companion object {
        fun fromControlModel(model: ControlModel2): ControlEntity2 {
            return ControlEntity2(
                id = model.id,
                nbrOfHours = model.nbrOfHours,
                controlType = model.controlType,
                hasBeenDone = model.hasBeenDone,
                observations = model.observations,
                amountOfControls = model.amountOfControls,
                staffOutnumbered = model.staffOutnumbered?.let { ControlResult.valueOf(it) },
                infractions = model.infractions?.map { InfractionEntity2.fromInfractionModel(it) },
                upToDateMedicalCheck = model.upToDateMedicalCheck?.let { ControlResult.valueOf(it) },
                compliantOperatingPermit = model.compliantOperatingPermit?.let { ControlResult.valueOf(it) },
                upToDateNavigationPermit = model.upToDateNavigationPermit?.let { ControlResult.valueOf(it) },
                compliantSecurityDocuments = model.compliantSecurityDocuments?.let { ControlResult.valueOf(it) },
                knowledgeOfFrenchLawAndLanguage = model.knowledgeOfFrenchLawAndLanguage?.let { ControlResult.valueOf(it) },
            )
        }
    }
}
