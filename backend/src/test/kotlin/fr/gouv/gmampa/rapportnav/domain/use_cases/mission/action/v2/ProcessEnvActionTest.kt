package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.MapEnvActionControlPlans
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeEnvTarget
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessEnvAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.GetInfractionsByActionId
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.EnvActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*


@SpringBootTest(classes = [ProcessEnvAction::class])
@ContextConfiguration(classes = [ProcessEnvAction::class])
class ProcessEnvActionTest {

    @Autowired
    private lateinit var processEnvAction: ProcessEnvAction

    @MockitoBean
    private lateinit var getControlByActionId: GetControlByActionId2

    @MockitoBean
    private lateinit var getStatusForAction: GetStatusForAction

    @MockitoBean
    private lateinit var mapControlPlans: MapEnvActionControlPlans

    @MockitoBean
    private lateinit var getInfractionsByActionId: GetInfractionsByActionId

    @MockitoBean
    private lateinit var getComputeEnvTarget: GetComputeEnvTarget

    @Test
    fun `test execute get Env action by id`() {
        val missionId = "761"
        val actionId = UUID.randomUUID()
        val action = EnvActionControlMock.create(
            id = actionId,
        )

        val mockControl = ControlMock.createAllControl()
        `when`(getControlByActionId.getAllControl(anyOrNull())).thenReturn(mockControl)
        processEnvAction = ProcessEnvAction(
            mapControlPlans = mapControlPlans,
            getStatusForAction = getStatusForAction,
            getControlByActionId = getControlByActionId,
            getInfractionsByActionId = getInfractionsByActionId,
            getComputeEnvTarget = getComputeEnvTarget
        )
        val entity = processEnvAction.execute(missionId = missionId, envAction = action)
        assertThat(entity).isNotNull
        assertThat(entity.id).isEqualTo(actionId)
        assertThat(entity.controlSecurity).isEqualTo(mockControl.controlSecurity)
        assertThat(entity.controlGensDeMer).isEqualTo(mockControl.controlGensDeMer)
        assertThat(entity.controlNavigation).isEqualTo(mockControl.controlNavigation)
        assertThat(entity.controlAdministrative).isEqualTo(mockControl.controlAdministrative)
    }
}
