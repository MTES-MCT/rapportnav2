package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.AttachControlToAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetNavActionListByMissionId
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

@SpringBootTest(classes = [GetNavActionListByMissionId::class])
@ContextConfiguration(classes = [GetNavActionListByMissionId::class])
class GetNavActionListByMissionIdTest {

    @Autowired
    private lateinit var getNavActionList: GetNavActionListByMissionId

    @MockBean
    private lateinit var missionActionRepository: INavMissionActionRepository

    @MockBean
    private lateinit var attachControlToAction: AttachControlToAction

    @MockBean
    private lateinit var getControlByActionId: GetControlByActionId2


    @Test
    fun `test execute get nav action list by mission id`() {
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

        val mockControl = ControlMock.createAllControl()

        `when`(getControlByActionId.getAllControl(anyOrNull())).thenReturn(mockControl)
        `when`(missionActionRepository.findByMissionId(missionId)).thenReturn(listOf(action))

        attachControlToAction = AttachControlToAction(getControlByActionId)

        getNavActionList = GetNavActionListByMissionId(missionActionRepository, attachControlToAction)
        val navActions = getNavActionList.execute(missionId = missionId)

        assertThat(navActions).isNotNull
        assertThat(navActions.size).isEqualTo(1)
        assertThat(navActions[0].id).isEqualTo(action.id)
        assertThat(navActions[0].getActionId()).isEqualTo(actionId.toString())
        assertThat(navActions[0].controlSecurity).isEqualTo(mockControl.controlSecurity)
        assertThat(navActions[0].controlNavigation).isEqualTo(mockControl.controlNavigation)
        assertThat(navActions[0].controlGensDeMer).isEqualTo(mockControl.controlGensDeMer)
        assertThat(navActions[0].controlAdministrative).isEqualTo(mockControl.controlAdministrative)
    }
}
