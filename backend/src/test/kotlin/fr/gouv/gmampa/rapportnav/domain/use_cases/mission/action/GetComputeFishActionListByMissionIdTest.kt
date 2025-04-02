package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeFishActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessFishAction
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
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

@SpringBootTest(classes = [GetComputeFishActionListByMissionId::class])
@ContextConfiguration(classes = [GetComputeFishActionListByMissionId::class])
class GetComputeFishActionListByMissionIdTest {

    @Autowired
    private lateinit var getFishActionList: GetComputeFishActionListByMissionId

    @MockitoBean
    private lateinit var getFishActionListByMissionId: GetFishActionListByMissionId

    @MockitoBean
    private lateinit var processFishAction: ProcessFishAction

    @Test
    fun `test execute get Fish action list  by mission id`() {

        val missionId = 761
        val actionId = UUID.randomUUID().hashCode()
        val action = FishActionControlMock.create(
            id = actionId,
        )

        val response = MissionFishActionEntity(
            id = actionId,
            missionId = 761,
            fishActionType = MissionActionType.SEA_CONTROL,
            actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
            actionEndDatetimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            speciesQuantitySeized = 4
        )

        `when`(processFishAction.execute(anyInt(), anyOrNull())).thenReturn(response)
        `when`(getFishActionListByMissionId.execute(missionId)).thenReturn(listOf(action))

        getFishActionList = GetComputeFishActionListByMissionId(
            processFishAction = processFishAction,
            getFishActionListByMissionId = getFishActionListByMissionId,
        )
        val fishActions = getFishActionList.execute(missionId = missionId)

        assertThat(fishActions).isNotNull
        assertThat(fishActions.size).isEqualTo(1)
        assertThat(fishActions[0].id).isEqualTo(action.id)
        assertThat(fishActions[0].getActionId()).isEqualTo(actionId.toString())
    }
}
