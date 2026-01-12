package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.*
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntity2Mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
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
    private lateinit var getStatusForAction: GetStatusForAction2

    @MockitoBean
    private lateinit var getComputeTarget: GetComputeTarget

    @Test
    fun `test execute get nav action by id`() {
        val missionId = 761
        val actionId = UUID.randomUUID()
        val action = MissionNavActionEntity(
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

        val mockTarget = TargetEntity2Mock.create()
        `when`(getComputeTarget.execute(actionId.toString(), true)).thenReturn(listOf(mockTarget))
        processNavAction = ProcessNavAction(
            getComputeTarget = getComputeTarget,
            getStatusForAction = getStatusForAction
        )

        val entity = processNavAction.execute(action = action)
        val infractionIds = entity.getAllInfractions().map { it.id }.toSet()
        val mockInfractionIds = mockTarget.controls?.flatMap { it.infractions!! }?.map { it.id }?.toSet()
        assertThat(entity).isNotNull
        assertThat(entity.id).isEqualTo(actionId)
        assertThat(infractionIds).isEqualTo(mockInfractionIds)
    }
}
