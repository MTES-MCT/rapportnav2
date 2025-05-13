package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeTarget
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessFishAction
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
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
    private lateinit var getStatusForAction: GetStatusForAction

    @MockitoBean
    private lateinit var getComputeTarget: GetComputeTarget

    @Test
    fun `test execute get fish action by id`() {
        val missionId = 761
        val actionId = UUID.randomUUID()
        val action = FishActionControlMock.create(
            id = actionId.hashCode(),
        )

        val mockTarget = TargetMissionMock.create()
        `when`(getComputeTarget.execute(actionId.hashCode().toString(), true)).thenReturn(listOf(mockTarget))
        processFishAction = ProcessFishAction(
            getComputeTarget = getComputeTarget,
            getStatusForAction = getStatusForAction
        )
        val entity = processFishAction.execute(missionId = missionId, action = action)
        val infractionIds = entity.getInfractions().map { it.id }.toSet()
        val mockInfractionIds = mockTarget.controls?.flatMap { it.infractions!! }?.map { it.id }?.toSet()
        assertThat(entity).isNotNull
        assertThat(entity.id).isEqualTo(actionId.hashCode())
        assertThat(infractionIds).isEqualTo(mockInfractionIds)
    }
}
