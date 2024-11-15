package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.FakeActionData
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import java.util.*

@SpringBootTest(classes = [GetFishActionListByMissionId::class])
@ContextConfiguration(classes = [GetFishActionListByMissionId::class])
class GetFishActionListByMissionIdTest {

    @Autowired
    private lateinit var getFishActionList: GetFishActionListByMissionId

    @MockBean
    private lateinit var fishActionRepo: IFishActionRepository


    @MockBean
    private lateinit var getStatusForAction: GetStatusForAction

    @MockBean
    private lateinit var getControlByActionId: GetControlByActionId2

    @MockBean
    private  lateinit var  getFakeActionData: FakeActionData

    @Test
    fun `test execute get Fish action list  by mission id`() {

        val missionId = 761
        val actionId = UUID.randomUUID().hashCode()
        val action = FishActionControlMock.create(
            id = actionId,
        )

        val mockControl  = ControlMock.createAllControl()

        `when`(getControlByActionId.getAllControl(anyOrNull())).thenReturn(mockControl)
        `when`(fishActionRepo.findFishActions(missionId)).thenReturn(listOf(action))

        getFishActionList = GetFishActionListByMissionId(
            fishActionRepo = fishActionRepo,
            getControlByActionId = getControlByActionId,
            getStatusForAction = getStatusForAction,
            getFakeActionData = getFakeActionData
        )
        val fishActions = getFishActionList.execute(missionId = missionId)

        assertThat(fishActions).isNotNull
        assertThat(fishActions.size).isEqualTo(1)
        assertThat(fishActions[0].id).isEqualTo(action.id)
        assertThat(fishActions[0].getActionId()).isEqualTo(actionId.toString())
        assertThat(fishActions[0].controlSecurity).isEqualTo(mockControl.controlSecurity)
        assertThat(fishActions[0].controlNavigation).isEqualTo(mockControl.controlNavigation)
        assertThat(fishActions[0].controlGensDeMer).isEqualTo(mockControl.controlGensDeMer)
        assertThat(fishActions[0].controlAdministrative).isEqualTo(mockControl.controlAdministrative)
    }
}
