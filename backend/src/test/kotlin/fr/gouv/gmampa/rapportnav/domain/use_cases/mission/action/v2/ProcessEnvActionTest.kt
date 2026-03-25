package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeEnvTarget
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessEnvAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.MissionDatesOutput
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntityMock
import java.time.Instant
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
    private lateinit var getStatusForAction: GetStatusForAction

    @MockitoBean
    private lateinit var getComputeEnvTarget: GetComputeEnvTarget

    @MockitoBean
    private lateinit var getMissionDates: GetMissionDates

    @Test
    fun `test execute get Env action by id`() {
        val missionId = 761
        val actionId = UUID.randomUUID()
        val action = EnvActionControlMock.create(
            id = actionId,
        )

        val mockTarget = TargetEntityMock.create()
        `when`(getComputeEnvTarget.execute(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(listOf(mockTarget))
        `when`(getMissionDates.execute(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(
            MissionDatesOutput(
                startDateTimeUtc = Instant.parse("2019-09-01T00:00:00Z"),
                endDateTimeUtc = Instant.parse("2019-09-10T00:00:00Z")
            )
        )
        processEnvAction = ProcessEnvAction(
            getStatusForAction = getStatusForAction,
            getComputeEnvTarget = getComputeEnvTarget,
            getMissionDates = getMissionDates
        )
        val entity = processEnvAction.execute(missionId = missionId, envAction = action)
        val infractionIds = entity.getAllInfractions().map { it.id }.toSet()
        val mockInfractionIds = mockTarget.controls?.flatMap { it.infractions!! }?.map { it.id }?.toSet()
        assertThat(entity).isNotNull
        assertThat(entity.id).isEqualTo(actionId)
        assertThat(infractionIds).isEqualTo(mockInfractionIds)
    }
}