package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.AttachControlToAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetNavActionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [GetNavActionById::class])
@ContextConfiguration(classes = [GetNavActionById::class])
class GetNavActionByIdTest {

    @Autowired
    private lateinit var getNavActionById: GetNavActionById

    @MockBean
    private lateinit var missionActionRepository: INavMissionActionRepository

    @MockBean
    private lateinit var attachControlToAction: AttachControlToAction

    @MockBean
    private lateinit var getControlByActionId: GetControlByActionId2

    @Test
    fun `test execute get nav action by id`() {
        val missionId = 761
        val actionId = UUID.randomUUID()
        val action = MissionActionModel(
            missionId = missionId,
            id = actionId,
            startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            observations = "My beautiful observation",
            isAntiPolDeviceDeployed = true,
            isSimpleBrewingOperationDone = true,
            diversionCarriedOut = true,
            actionType = ActionType.CONTROL.toString(),
        )

        val mockControl  = ControlMock.createAllControl()

        `when`(getControlByActionId.getAllControl(anyOrNull())).thenReturn(mockControl)
        `when`(missionActionRepository.findById(actionId)).thenReturn(Optional.of(action))

        attachControlToAction = AttachControlToAction(getControlByActionId)

        getNavActionById = GetNavActionById(attachControlToAction, missionActionRepository)
        val missionEnvAction = getNavActionById.execute(actionId = actionId)

        assertThat(missionEnvAction).isNotNull
        assertThat(missionEnvAction?.getActionId()).isEqualTo(actionId.toString())
        assertThat(missionEnvAction?.controlSecurity).isEqualTo(mockControl.controlSecurity)
        assertThat(missionEnvAction?.controlNavigation).isEqualTo(mockControl.controlNavigation)
        assertThat(missionEnvAction?.controlGensDeMer).isEqualTo(mockControl.controlGensDeMer)
        assertThat(missionEnvAction?.controlAdministrative).isEqualTo(mockControl.controlAdministrative)
    }
}
