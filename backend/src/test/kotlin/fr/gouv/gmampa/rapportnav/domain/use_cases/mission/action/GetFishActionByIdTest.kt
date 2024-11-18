package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionById
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

@SpringBootTest(classes = [GetFishActionById::class])
@ContextConfiguration(classes = [GetFishActionById::class])
class GetFishActionByIdTest {

    @Autowired
    private lateinit var getFishActionById: GetFishActionById

    @MockBean
    private lateinit var fishActionRepo: IFishActionRepository

    @MockBean
    private lateinit var getControlByActionId: GetControlByActionId2

    @MockBean
    private lateinit var getStatusForAction: GetStatusForAction

    @Test
    fun `test execute get Fish action by id`() {

        val missionId = 761
        val actionId = UUID.randomUUID().hashCode()
        val action = FishActionControlMock.create(
            id = actionId,
        )

        val mockControl  = ControlMock.createAllControl()

        `when`(getControlByActionId.getAllControl(anyOrNull())).thenReturn(mockControl)
        `when`(fishActionRepo.findFishActions(missionId)).thenReturn(listOf(action))

        getFishActionById = GetFishActionById(
            fishActionRepo = fishActionRepo,
            getStatusForAction = getStatusForAction,
            getControlByActionId = getControlByActionId
        )
        val missionEnvAction = getFishActionById.execute(missionId = missionId, actionId = actionId.toString())

        assertThat(missionEnvAction).isNotNull
        assertThat(missionEnvAction?.getActionId()).isEqualTo(actionId.toString())
        assertThat(missionEnvAction?.controlSecurity).isEqualTo(mockControl.controlSecurity)
        assertThat(missionEnvAction?.controlNavigation).isEqualTo(mockControl.controlNavigation)
        assertThat(missionEnvAction?.controlGensDeMer).isEqualTo(mockControl.controlGensDeMer)
        assertThat(missionEnvAction?.controlAdministrative).isEqualTo(mockControl.controlAdministrative)
    }
}
