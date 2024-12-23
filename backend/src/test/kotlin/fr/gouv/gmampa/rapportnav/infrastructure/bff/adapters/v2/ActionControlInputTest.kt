package fr.gouv.gmampa.rapportnav.infrastructure.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class ActionControlInputTest {
    @Test
    fun `execute should return list of all infractions`() {
        val action = getActionInput()
        val infractions = action.getAllInfractions()
        assertThat(infractions.size).isEqualTo(5)
    }

    @Test
    fun `execute should return Action control entity`() {
        val action = getActionInput()
        val entity = action.toActionControlEntity()
        assertThat(entity).isNotNull()
        assertThat(entity.controlSecurity?.id).isNotNull()
        assertThat(entity.controlGensDeMer?.id).isNotNull()
        assertThat(entity.controlNavigation?.id).isNotNull()
        assertThat(entity.controlAdministrative?.id).isNotNull()
    }

    private fun getActionInput(): ActionControlInput {
        val controlAdministrative = ControlAdministrativeInput2(
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
        controlAdministrative.setMissionIdAndActionId(missionId = 761, actionId = "my action id")

        val controlGensDeMer = ControlGensDeMerInput2(
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
        )
        controlGensDeMer.setMissionIdAndActionId(missionId = 761, actionId = "my action id")


        val controlNavigation = ControlNavigationInput2(
            observations = "My beautiful observation",
            amountOfControls = 2,
            unitShouldConfirm = false,
            unitHasConfirmed = true,
            infractions = listOf(
                InfractionInput2(controlType = ControlType.NAVIGATION.toString())
            ),
            hasBeenDone = true
        )
        controlNavigation.setMissionIdAndActionId(missionId = 761, actionId = "my action id")

        val controlSecurity = ControlSecurityInput2(
            observations = "My beautiful observation",
            amountOfControls = 2,
            unitShouldConfirm = false,
            unitHasConfirmed = true,
            infractions = listOf(
                InfractionInput2(controlType = ControlType.SECURITY.toString()),
                InfractionInput2(controlType = ControlType.SECURITY.toString())
            ),
            hasBeenDone = true
        )
        controlSecurity.setMissionIdAndActionId(missionId = 761, actionId = "my action id")
        return ActionControlInput(
            controlSecurity = controlSecurity,
            controlGensDeMer = controlGensDeMer,
            controlNavigation = controlNavigation,
            controlAdministrative = controlAdministrative
        )
    }
}
