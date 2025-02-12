package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.MapEnvActionControlPlans
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvActionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.GetInfractionsByActionId
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.EnvActionControlMock
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

@SpringBootTest(classes = [GetEnvActionById::class])
@ContextConfiguration(classes = [GetEnvActionById::class])
class GetEnvActionByIdTest {

    @Autowired
    private lateinit var getEnvActionById: GetEnvActionById

    @MockitoBean
    private lateinit var getEnvMissionById2: GetEnvMissionById2

    @MockitoBean
    private lateinit var getControlByActionId: GetControlByActionId2

    @MockitoBean
    private lateinit var getStatusForAction: GetStatusForAction

    @MockitoBean
    private lateinit var mapControlPlans: MapEnvActionControlPlans

    @MockitoBean
    private lateinit var getInfractionsByActionId: GetInfractionsByActionId

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

        val mockControl = ControlMock.createAllControl()

        `when`(getControlByActionId.getAllControl(anyOrNull())).thenReturn(mockControl)
        `when`(getEnvMissionById2.execute(missionId)).thenReturn(missionEntity)

        getEnvActionById = GetEnvActionById(
            getEnvMissionById2 = getEnvMissionById2,
            getStatusForAction = getStatusForAction,
            mapControlPlans = mapControlPlans,
            getControlByActionId = getControlByActionId,
            getInfractionsByActionId = getInfractionsByActionId,
        )
        val missionEnvAction = getEnvActionById.execute(missionId = missionId, actionId = actionId.toString())

        assertThat(missionEnvAction).isNotNull
        assertThat(missionEnvAction?.getActionId()).isEqualTo(actionId.toString())
        assertThat(missionEnvAction?.controlSecurity).isEqualTo(mockControl.controlSecurity)
        assertThat(missionEnvAction?.controlNavigation).isEqualTo(mockControl.controlNavigation)
        assertThat(missionEnvAction?.controlGensDeMer).isEqualTo(mockControl.controlGensDeMer)
        assertThat(missionEnvAction?.controlAdministrative).isEqualTo(mockControl.controlAdministrative)
    }

}

