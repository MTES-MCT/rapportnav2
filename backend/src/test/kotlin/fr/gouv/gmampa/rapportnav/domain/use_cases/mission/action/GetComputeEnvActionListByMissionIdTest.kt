package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeEnvActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessEnvAction
import fr.gouv.gmampa.rapportnav.mocks.mission.action.EnvActionControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.EnvActionNoteMock
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

@SpringBootTest(classes = [GetComputeEnvActionListByMissionId::class])
@ContextConfiguration(classes = [GetComputeEnvActionListByMissionId::class])
class GetComputeEnvActionListByMissionIdTest {

    @Autowired
    private lateinit var getEnvActionListById: GetComputeEnvActionListByMissionId

    @MockitoBean
    private lateinit var getEnvMissionById2: GetEnvMissionById2

    @MockitoBean
    private lateinit var processEnvAction: ProcessEnvAction

    @Test
    fun `test execute get Env action list by mission id`() {
        val missionId = 761
        val actionId = UUID.randomUUID()

        val missionEntity = MissionEntity(
            missionTypes = listOf(MissionTypeEnum.SEA),
            startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            hasMissionOrder = false,
            envActions = listOf(
                EnvActionControlMock.create(
                    id = actionId,
                ),
                EnvActionNoteMock.create() // this one will be filtered out because it's a note
            ),
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

        val envActions = getEnvActionListById.execute(missionId = missionId)
        assertThat(envActions).isNotNull
        assertThat(envActions.size).isEqualTo(1)
        assertThat(envActions[0].id).isEqualTo(actionId)
    }

    @Test
    fun `should throw BackendInternalException when getEnvMissionById2 fails`() {
        val missionId = 761
        `when`(getEnvMissionById2.execute(missionId))
            .thenThrow(RuntimeException("API error"))

        val exception = assertThrows<BackendInternalException> {
            getEnvActionListById.execute(missionId = missionId)
        }
        assertThat(exception.message).contains("GetComputeEnvActionListByMissionId failed for missionId=$missionId")
    }

    @Test
    fun `should propagate BackendInternalException from getEnvMissionById2`() {
        val missionId = 761
        val originalException = BackendInternalException(message = "Original error")
        `when`(getEnvMissionById2.execute(missionId))
            .thenAnswer { throw originalException }

        val exception = assertThrows<BackendInternalException> {
            getEnvActionListById.execute(missionId = missionId)
        }
        assertThat(exception.message).isEqualTo("Original error")
    }
}
