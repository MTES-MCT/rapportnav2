package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessNavAction
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
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
    private lateinit var navMissionActionRepository: INavMissionActionRepository

    @MockitoBean
    private lateinit var processNavAction: ProcessNavAction


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
            actionType = ActionType.CONTROL,
        )

        val response = MissionNavActionEntity(
            id = actionId,
            missionId = 761,
            actionType = ActionType.ILLEGAL_IMMIGRATION,
            startDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00")
        )

        `when`(processNavAction.execute(anyInt(), anyOrNull())).thenReturn(response)
        `when`(navMissionActionRepository.findByMissionId(missionId)).thenReturn(listOf(action))

        getNavActionList = GetComputeNavActionListByMissionId(
            processNavAction = processNavAction,
            navMissionActionRepository = navMissionActionRepository
        )
        val navActions = getNavActionList.execute(missionId = missionId)

        assertThat(navActions).isNotNull
        assertThat(navActions.size).isEqualTo(1)
        assertThat(navActions[0].id).isEqualTo(action.id)
        assertThat(navActions[0].getActionId()).isEqualTo(actionId.toString())
    }
}
