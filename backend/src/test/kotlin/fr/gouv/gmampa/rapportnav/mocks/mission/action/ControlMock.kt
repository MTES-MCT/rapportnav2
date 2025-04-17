package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.v2.ActionControlEntity
import fr.gouv.gmampa.rapportnav.mocks.mission.infraction.InfractionEntityMock
import java.util.*

object ControlMock {

    fun createAllControl(actionId: String? = null, missionId: String? = null): ActionControlEntity {
        return ActionControlEntity(
            controlSecurity = ControlSecurityEntity(
                id = UUID.randomUUID(),
                missionId = missionId ?: "761",
                actionControlId = actionId ?: "MyActionId",
                amountOfControls = 2,
                hasBeenDone = false
            ),
            controlGensDeMer = ControlGensDeMerEntity(
                id = UUID.randomUUID(),
                missionId = missionId ?: "761",
                actionControlId = actionId ?: "MyActionId",
                amountOfControls = 2,
                hasBeenDone = true,
                upToDateMedicalCheck = ControlResult.NO,
                staffOutnumbered = ControlResult.NOT_CONTROLLED,
                knowledgeOfFrenchLawAndLanguage = ControlResult.YES
            ),
            controlNavigation = ControlNavigationEntity(
                id = UUID.randomUUID(),
                missionId = missionId ?: "761",
                actionControlId = actionId ?: "MyActionId",
                amountOfControls = 2,
                hasBeenDone = true,
                infractions = listOf(InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT))
            ),
            controlAdministrative = ControlAdministrativeEntity(
                id = UUID.randomUUID(),
                missionId = missionId ?: "761",
                actionControlId = actionId ?: "MyActionId",
                amountOfControls = 2,
                hasBeenDone = false,
                compliantOperatingPermit = ControlResult.YES,
                upToDateNavigationPermit = ControlResult.YES,
                compliantSecurityDocuments = ControlResult.NOT_CONTROLLED,
                infractions = listOf(InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT))
            )
        )
    }
}
