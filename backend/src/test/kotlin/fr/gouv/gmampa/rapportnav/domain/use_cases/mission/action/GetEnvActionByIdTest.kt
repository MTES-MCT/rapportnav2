package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvActionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessEnvAction
import fr.gouv.gmampa.rapportnav.mocks.mission.action.EnvActionControlMock
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

@SpringBootTest(classes = [GetEnvActionById::class])
@ContextConfiguration(classes = [GetEnvActionById::class])
class GetEnvActionByIdTest {

    @Autowired
    private lateinit var getEnvActionById: GetEnvActionById

    @MockitoBean
    private lateinit var getEnvMissionById2: GetEnvMissionById2

    @MockitoBean
    private lateinit var processEnvAction: ProcessEnvAction

    @Test
    fun `test execute get Env action by id`() {
        val missionId = 761
        val actionId = UUID.randomUUID()
        val action = EnvActionControlMock.create(
            id = actionId,
        )

        val missionEntity = MissionEntity(
            missionTypes = listOf(MissionTypeEnum.SEA),
            startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            hasMissionOrder = false,
            envActions = listOf(action),
            isDeleted = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false,
            missionSource = MissionSourceEnum.MONITORENV
        )
        val response = MissionEnvActionEntity(
            id = actionId,
            missionId = missionId,
            envActionType = ActionTypeEnum.CONTROL,
            startDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            controlPlans = listOf(EnvActionControlPlanEntity(themeId = 104, subThemeIds = listOf(143)))
        )

        `when`(processEnvAction.execute(anyInt(), anyOrNull())).thenReturn(response)
        `when`(getEnvMissionById2.execute(missionId)).thenReturn(missionEntity)

        val missionEnvAction = getEnvActionById.execute(missionId = missionId, actionId = actionId.toString())

        assertThat(missionEnvAction).isNotNull
        assertThat(missionEnvAction?.getActionId()).isEqualTo(actionId.toString())
    }

    @Test
    fun `should return null when missionId is null`() {
        val result = getEnvActionById.execute(missionId = null, actionId = UUID.randomUUID().toString())
        assertThat(result).isNull()
    }

    @Test
    fun `should return null when actionId is not a valid UUID`() {
        val result = getEnvActionById.execute(missionId = 761, actionId = "invalid-uuid")
        assertThat(result).isNull()
    }

    @Test
    fun `should throw BackendInternalException when getEnvMissionById2 fails`() {
        val missionId = 761
        val actionId = UUID.randomUUID().toString()
        `when`(getEnvMissionById2.execute(missionId)).thenThrow(RuntimeException("API error"))

        val exception = assertThrows<BackendInternalException> {
            getEnvActionById.execute(missionId = missionId, actionId = actionId)
        }
        assertThat(exception.message).contains("GetEnvActionById failed for missionId=$missionId")
    }
}