package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ControlEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity2
import java.util.*

object ControlMock {

    fun create(
        id: UUID? = null,
        amountOfControls: Int? = null,
        hasBeenDone: Boolean? = null,
        controlType: ControlType? = null,
        observations: String? = null,
        staffOutnumbered: ControlResult? = null,
        upToDateMedicalCheck: ControlResult? = null,
        compliantOperatingPermit: ControlResult? = null,
        upToDateNavigationPermit: ControlResult? = null,
        compliantSecurityDocuments: ControlResult? = null,
        knowledgeOfFrenchLawAndLanguage: ControlResult? = null,
        infractions: List<InfractionEntity2>? = null
    ): ControlEntity2 {
        return ControlEntity2(
            id = id ?: UUID.randomUUID(),
            controlType = controlType ?: ControlType.GENS_DE_MER,
            amountOfControls = amountOfControls ?: 0,
            hasBeenDone = hasBeenDone,
            observations = observations?:"My observations",
            staffOutnumbered = staffOutnumbered?:ControlResult.NO,
            upToDateMedicalCheck = upToDateMedicalCheck?:ControlResult.NO,
            compliantOperatingPermit = compliantOperatingPermit?:ControlResult.YES,
            upToDateNavigationPermit = upToDateNavigationPermit?:ControlResult.NO,
            compliantSecurityDocuments = compliantSecurityDocuments?:ControlResult.NOT_CONCERNED,
            knowledgeOfFrenchLawAndLanguage = knowledgeOfFrenchLawAndLanguage?:ControlResult.NOT_CONTROLLED,
            infractions = infractions?:listOf(
                InfractionEntity2(
                    id = UUID.randomUUID(),
                    infractionType = InfractionTypeEnum.WITH_REPORT,
                    natinfs = listOf("natInf2", "natInf3"),
                    observations = "My observations"
                )
            )
        )
    }

    fun createList(): List<ControlEntity2> {
        return listOf(
            ControlEntity2(
                id = UUID.randomUUID(),
                controlType = ControlType.NAVIGATION,
                amountOfControls = 2,
                hasBeenDone = false,
            ),
            ControlEntity2(
                id = UUID.randomUUID(),
                controlType = ControlType.SECURITY,
                amountOfControls = 0,
                hasBeenDone = true
            ),
            ControlEntity2(
                id = UUID.randomUUID(),
                controlType = ControlType.GENS_DE_MER,
                amountOfControls = 1
            ),
            ControlEntity2(
                id = UUID.randomUUID(),
                controlType = ControlType.ADMINISTRATIVE,
                amountOfControls = 5,
                hasBeenDone = true
            )
        )
    }
}
