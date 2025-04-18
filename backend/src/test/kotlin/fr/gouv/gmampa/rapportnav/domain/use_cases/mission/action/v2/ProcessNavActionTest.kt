package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [ProcessNavAction::class])
@ContextConfiguration(classes = [ProcessNavAction::class])
class ProcessNavActionTest {

    @Autowired
    private lateinit var processNavAction: ProcessNavAction

    @MockitoBean
    private lateinit var getControlByActionId: GetControlByActionId2

    @MockitoBean
    private lateinit var getStatusForAction: GetStatusForAction

    @MockitoBean
    private lateinit var getComputeTarget: GetComputeTarget

    @Test
    fun `test execute get fish action by id`() {
        val missionId = 761
        val actionId = UUID.randomUUID()
        val action = MissionActionModel(
            id = actionId,
            missionId = missionId,
            startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            observations = "My beautiful observation",
            isAntiPolDeviceDeployed = true,
            isSimpleBrewingOperationDone = true,
            diversionCarriedOut = true,
            actionType = ActionType.CONTROL,
        )

        val mockControl = ControlMock.createAllControl()
        `when`(getControlByActionId.getAllControl(anyOrNull())).thenReturn(mockControl)
        processNavAction = ProcessNavAction(
            getComputeTarget = getComputeTarget,
            getStatusForAction = getStatusForAction,
            getControlByActionId = getControlByActionId
        )
        val entity = processNavAction.execute(missionId = missionId, action = action)
        assertThat(entity).isNotNull
        assertThat(entity.id).isEqualTo(actionId)
        assertThat(entity.controlSecurity).isEqualTo(mockControl.controlSecurity)
        assertThat(entity.controlGensDeMer).isEqualTo(mockControl.controlGensDeMer)
        assertThat(entity.controlNavigation).isEqualTo(mockControl.controlNavigation)
        assertThat(entity.controlAdministrative).isEqualTo(mockControl.controlAdministrative)
    }
}
