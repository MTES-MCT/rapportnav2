package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ControlEntity2
import java.util.*

class Control2(
    var id: UUID? = null,
    var controlType: ControlType,
    val amountOfControls: Int,
    val infractions: List<Infraction2>? = null,
    val observations: String? = null,
    val compliantOperatingPermit: ControlResult? = null,
    val upToDateNavigationPermit: ControlResult? = null,
    val compliantSecurityDocuments: ControlResult? = null,
    val staffOutnumbered: ControlResult? = null,
    val upToDateMedicalCheck: ControlResult? = null,
    val hasBeenDone: Boolean? = null,
    val knowledgeOfFrenchLawAndLanguage: ControlResult? = null,
    val nbrOfHours: Int? = null
){
    fun toControlEntity(): ControlEntity2 {
        return ControlEntity2(
            id = id?: UUID.randomUUID(),
            controlType = controlType,
            hasBeenDone = hasBeenDone,
            observations = observations,
            amountOfControls = amountOfControls,
            staffOutnumbered = staffOutnumbered,
            upToDateMedicalCheck = upToDateMedicalCheck,
            compliantOperatingPermit = compliantOperatingPermit,
            upToDateNavigationPermit = upToDateNavigationPermit,
            compliantSecurityDocuments = compliantSecurityDocuments,
            infractions = infractions?.map { it.toInfractionEntity() },
            knowledgeOfFrenchLawAndLanguage = knowledgeOfFrenchLawAndLanguage,
        )
    }

    companion object {
        fun fromControlEntity(entity: ControlEntity2): Control2 {
            return Control2(
                id = entity.id,
                controlType = entity.controlType,
                hasBeenDone = entity.hasBeenDone,
                observations = entity.observations,
                amountOfControls = entity.amountOfControls,
                staffOutnumbered = entity.staffOutnumbered,
                upToDateMedicalCheck = entity.upToDateMedicalCheck,
                compliantOperatingPermit = entity.compliantOperatingPermit,
                upToDateNavigationPermit = entity.upToDateNavigationPermit,
                compliantSecurityDocuments = entity.compliantSecurityDocuments,
                knowledgeOfFrenchLawAndLanguage = entity.knowledgeOfFrenchLawAndLanguage,
                infractions = entity.infractions?.map { Infraction2.fromInfractionEntity(it) }?.sortedBy { it.id },
            )
        }
    }
}
