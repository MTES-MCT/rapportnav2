package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity
import java.util.*

object ControlEntityMock {

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
        infractions: List<InfractionEntity> = listOf<InfractionEntity>(),
    ): ControlEntity {
        val infractions = if (infractions.isEmpty()) listOf<InfractionEntity>(
            InfractionEntity(
                id = UUID.randomUUID(),
                infractionType = InfractionTypeEnum.WITH_REPORT,
                natinfs = listOf("natInf2", "natInf3"),
                observations = "My observations"
            )
        ) else infractions
        return ControlEntity(
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
            infractions = infractions
        )
    }

    fun createList(): List<ControlEntity> {
        return listOf(
            ControlEntity(
                id = UUID.randomUUID(),
                controlType = ControlType.NAVIGATION,
                amountOfControls = 2,
                hasBeenDone = false,
            ),
            ControlEntity(
                id = UUID.randomUUID(),
                controlType = ControlType.SECURITY,
                amountOfControls = 0,
                hasBeenDone = true
            ),
            ControlEntity(
                id = UUID.randomUUID(),
                controlType = ControlType.GENS_DE_MER,
                amountOfControls = 1
            ),
            ControlEntity(
                id = UUID.randomUUID(),
                controlType = ControlType.ADMINISTRATIVE,
                amountOfControls = 5,
                hasBeenDone = true
            )
        )
    }
}
