package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetNavActionListByOwnerId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessNavAction
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

@SpringBootTest(classes = [GetComputeNavActionListByMissionId::class])
@ContextConfiguration(classes = [GetComputeNavActionListByMissionId::class])
class GetComputeNavActionListByMissionIdTest {

    @Autowired
    private lateinit var getNavActionList: GetComputeNavActionListByMissionId

    @MockitoBean
    private lateinit var getNavActionListByOwnerId: GetNavActionListByOwnerId

    @MockitoBean
    private lateinit var processNavAction: ProcessNavAction
    @Test
    fun `test execute get nav action list by mission id`() {
        val missionId = UUID.randomUUID()
        val actionId = UUID.randomUUID()
        val action = MissionNavActionEntity(

            id = actionId,
            ownerId = UUID.randomUUID(),
            startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            observations = "My beautiful observation",
            isAntiPolDeviceDeployed = true,
            isSimpleBrewingOperationDone = true,
            diversionCarriedOut = true,
            actionType = ActionType.CONTROL
        )

        val response = MissionNavActionEntity(
            id = actionId,
            ownerId = UUID.randomUUID(),
            actionType = ActionType.ILLEGAL_IMMIGRATION,
            startDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00")
        )

        `when`(processNavAction.execute(anyOrNull())).thenReturn(response)
        `when`(getNavActionListByOwnerId.execute(ownerId = missionId)).thenReturn(listOf(action))

        val navActions = getNavActionList.execute(ownerId = missionId)

        assertThat(navActions).isNotNull
        assertThat(navActions.size).isEqualTo(1)
        assertThat(navActions[0].id).isEqualTo(action.id)
        assertThat(navActions[0].getActionId()).isEqualTo(actionId.toString())
    }
    @Test
    fun `test execute get nav action list by mission id UUID`() {
        val actionId = UUID.randomUUID()
        val missionId = UUID.randomUUID()
        val action = MissionNavActionEntity(
            id = actionId,
            ownerId = missionId,
            startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            observations = "My beautiful observation",
            isAntiPolDeviceDeployed = true,
            isSimpleBrewingOperationDone = true,
            diversionCarriedOut = true,
            actionType = ActionType.CONTROL
        )

        val response = MissionNavActionEntity(
            id = actionId,
            ownerId = UUID.randomUUID(),
            actionType = ActionType.ILLEGAL_IMMIGRATION,
            startDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00")
        )

        `when`(processNavAction.execute(anyOrNull())).thenReturn(response)
        `when`(getNavActionListByOwnerId.execute(ownerId = missionId)).thenReturn(listOf(action))

        val navActions = getNavActionList.execute(ownerId = missionId)

        assertThat(navActions).isNotNull
        assertThat(navActions.size).isEqualTo(1)
        assertThat(navActions[0].id).isEqualTo(action.id)
        assertThat(navActions[0].getActionId()).isEqualTo(actionId.toString())
    }
}
