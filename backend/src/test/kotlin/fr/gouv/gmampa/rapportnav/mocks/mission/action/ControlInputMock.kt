package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.*

object ControlInputMock {
    fun createAllControl(actionId: String? = null, missionId: Int? = null): ActionControl {
        return ActionControl(
            controlSecurity = ControlSecurity(
                observations = "My beautiful observation",
                amountOfControls = 2,
                unitShouldConfirm = false,
                unitHasConfirmed = true,
                infractions = listOf(
                    Infraction(controlType = ControlType.SECURITY),
                    Infraction(controlType = ControlType.SECURITY)
                ),
                hasBeenDone = true
            ),
            controlGensDeMer = ControlGensDeMer(
                observations = "My beautiful observation",
                amountOfControls = 2,
                unitShouldConfirm = false,
                unitHasConfirmed = true,
                infractions = listOf(
                    Infraction(controlType = ControlType.GENS_DE_MER)
                ),
                hasBeenDone = true,
                knowledgeOfFrenchLawAndLanguage = ControlResult.YES,
                staffOutnumbered = ControlResult.NO,
                upToDateMedicalCheck = ControlResult.NOT_CONTROLLED
            ),
            controlNavigation = ControlNavigation(
                observations = "My beautiful observation",
                amountOfControls = 2,
                unitShouldConfirm = false,
                unitHasConfirmed = true,
                infractions = listOf(
                    Infraction(controlType = ControlType.NAVIGATION)
                ),
                hasBeenDone = true
            ),
            controlAdministrative = ControlAdministrative(
                observations = "My beautiful observation",
                amountOfControls = 2,
                unitShouldConfirm = false,
                unitHasConfirmed = true,
                infractions = listOf(
                    Infraction(controlType = ControlType.ADMINISTRATIVE)
                ),
                hasBeenDone = true,
                compliantOperatingPermit = ControlResult.YES,
                upToDateNavigationPermit = ControlResult.NO,
                compliantSecurityDocuments = ControlResult.NOT_CONTROLLED,
            )
        )
    }
}
