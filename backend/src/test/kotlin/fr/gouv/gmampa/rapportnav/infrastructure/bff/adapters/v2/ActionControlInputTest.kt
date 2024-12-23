package fr.gouv.gmampa.rapportnav.infrastructure.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.*
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlInputMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class ActionControlInputTest {
    @Test
    fun `execute should return list of all infractions`() {
        val action = getActionInput()
        action.controlSecurity?.setMissionIdAndActionId(missionId = 761, actionId = "my action id")
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
        val action =  ControlInputMock.createAllControl()
        action.controlAdministrative?.setMissionIdAndActionId(missionId = 761, actionId = "my action id")
        action.controlGensDeMer?.setMissionIdAndActionId(missionId = 761, actionId = "my action id")
        action.controlNavigation?.setMissionIdAndActionId(missionId = 761, actionId = "my action id")
        action.controlSecurity?.setMissionIdAndActionId(missionId = 761, actionId = "my action id")
        return action
    }
}
