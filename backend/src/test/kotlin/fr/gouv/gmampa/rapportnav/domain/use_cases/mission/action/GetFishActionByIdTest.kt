package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessFishAction
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [GetFishActionById::class])
@ContextConfiguration(classes = [GetFishActionById::class])
class GetFishActionByIdTest {

    @Autowired
    private lateinit var getFishActionById: GetFishActionById

    @MockitoBean
    private lateinit var getFishActionListByMissionId: GetFishActionListByMissionId

    @MockitoBean
    private lateinit var processFishAction: ProcessFishAction

    @Test
    fun `test execute get Fish action by id`() {
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

        val missionEnvAction = getFishActionById.execute(missionId = missionId, actionId = actionId.toString())

        assertThat(missionEnvAction).isNotNull
        assertThat(missionEnvAction?.getActionId()).isEqualTo(actionId.toString())
    }

    @Test
    fun `should return null when missionId is null`() {
        val result = getFishActionById.execute(missionId = null, actionId = "123")
        assertThat(result).isNull()
    }

    @Test
    fun `should return null when actionId is null`() {
        val result = getFishActionById.execute(missionId = 761, actionId = null)
        assertThat(result).isNull()
    }

    @Test
    fun `should return null when actionId is a UUID`() {
        val result = getFishActionById.execute(missionId = 761, actionId = UUID.randomUUID().toString())
        assertThat(result).isNull()
    }

    @Test
    fun `should throw BackendInternalException when getFishActionListByMissionId fails`() {
        val missionId = 761
        val actionId = "123"
        `when`(getFishActionListByMissionId.execute(missionId)).thenThrow(RuntimeException("API error"))

        val exception = assertThrows<BackendInternalException> {
            getFishActionById.execute(missionId = missionId, actionId = actionId)
        }
        assertThat(exception.message).contains("GetFishActionById failed for missionId=$missionId")
    }
}
