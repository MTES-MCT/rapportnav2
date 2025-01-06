package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.*

object ControlInputMock {
    fun createAllControl(actionId: String? = null, missionId: Int? = null): ActionControlInput {
        return ActionControlInput(
            controlSecurity = ControlSecurityInput2(
                observations = "My beautiful observation",
                amountOfControls = 2,
                unitShouldConfirm = false,
                unitHasConfirmed = true,
                infractions = listOf(
                    InfractionInput2(controlType = ControlType.SECURITY.toString()),
                    InfractionInput2(controlType = ControlType.SECURITY.toString())
                ),
                hasBeenDone = true
            ),
            controlGensDeMer = ControlGensDeMerInput2(
                observations = "My beautiful observation",
                amountOfControls = 2,
                unitShouldConfirm = false,
                unitHasConfirmed = true,
                infractions = listOf(
                    InfractionInput2(controlType = ControlType.GENS_DE_MER.toString())
                ),
                hasBeenDone = true,
                knowledgeOfFrenchLawAndLanguage = ControlResult.YES,
                staffOutnumbered = ControlResult.NO,
                upToDateMedicalCheck = ControlResult.NOT_CONTROLLED
            ),
            controlNavigation = ControlNavigationInput2(
                observations = "My beautiful observation",
                amountOfControls = 2,
                unitShouldConfirm = false,
                unitHasConfirmed = true,
                infractions = listOf(
                    InfractionInput2(controlType = ControlType.NAVIGATION.toString())
                ),
                hasBeenDone = true
            ),
            controlAdministrative = ControlAdministrativeInput2(
                observations = "My beautiful observation",
                amountOfControls = 2,
                unitShouldConfirm = false,
                unitHasConfirmed = true,
                infractions = listOf(
                    InfractionInput2(controlType = ControlType.ADMINISTRATIVE.toString())
                ),
                hasBeenDone = true,
                compliantOperatingPermit = ControlResult.YES,
                upToDateNavigationPermit = ControlResult.NO,
                compliantSecurityDocuments = ControlResult.NOT_CONTROLLED,
            )
        )
    }
}
