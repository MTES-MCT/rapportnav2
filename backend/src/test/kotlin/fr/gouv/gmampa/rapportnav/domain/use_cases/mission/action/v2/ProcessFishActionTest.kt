package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeTarget
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessFishAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*


@SpringBootTest(classes = [ProcessFishAction::class])
@ContextConfiguration(classes = [ProcessFishAction::class])
class ProcessFishActionTest {

    @Autowired
    private lateinit var processFishAction: ProcessFishAction

    @MockitoBean
    private lateinit var getControlByActionId: GetControlByActionId2

    @MockitoBean
    private lateinit var getStatusForAction: GetStatusForAction

    @MockitoBean
    private lateinit var getComputeTarget: GetComputeTarget

    @Test
    fun `test execute get fish action by id`() {
        val missionId = "761"
        val actionId = UUID.randomUUID().hashCode()
        val action = FishActionControlMock.create(
            id = actionId,
        )

        val mockControl = ControlMock.createAllControl()
        `when`(getControlByActionId.getAllControl(anyOrNull())).thenReturn(mockControl)
        processFishAction = ProcessFishAction(
            getComputeTarget = getComputeTarget,
            getStatusForAction = getStatusForAction,
            getControlByActionId = getControlByActionId
        )
        val entity = processFishAction.execute(missionId = missionId, action = action)
        assertThat(entity).isNotNull
        assertThat(entity.id).isEqualTo(actionId)
        assertThat(entity.controlSecurity).isEqualTo(mockControl.controlSecurity)
        assertThat(entity.controlGensDeMer).isEqualTo(mockControl.controlGensDeMer)
        assertThat(entity.controlNavigation).isEqualTo(mockControl.controlNavigation)
        assertThat(entity.controlAdministrative).isEqualTo(mockControl.controlAdministrative)
    }
}
