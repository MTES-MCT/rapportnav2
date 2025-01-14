package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.FakeActionData
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.MapEnvActionControlPlans
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvActionListByMissionId
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

@SpringBootTest(classes = [GetEnvActionListByMissionId::class])
@ContextConfiguration(classes = [GetEnvActionListByMissionId::class])
class GetEnvActionListByMissionIdTest {

    @Autowired
    private lateinit var getEnvActionListById: GetEnvActionListByMissionId

    @MockitoBean
    private lateinit var monitorEnvApiRepo: IEnvMissionRepository

    @MockitoBean
    private lateinit var getControlByActionId: GetControlByActionId2

    @MockitoBean
    private lateinit var getStatusForAction: GetStatusForAction

    @MockitoBean
    private lateinit var mapControlPlans: MapEnvActionControlPlans

    @MockitoBean
    private lateinit var getInfractionsByActionId: GetInfractionsByActionId

    @MockitoBean
    private lateinit var getFakeActionData: FakeActionData

    @Test
    fun `test execute get Env action list  by mission id`() {
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
        `when`(monitorEnvApiRepo.findMissionById(missionId)).thenReturn(missionEntity)

        getEnvActionListById = GetEnvActionListByMissionId(
            monitorEnvApiRepo = monitorEnvApiRepo,
            getStatusForAction = getStatusForAction,
            mapControlPlans = mapControlPlans,
            getControlByActionId = getControlByActionId,
            getInfractionsByActionId = getInfractionsByActionId,
            getFakeActionData = getFakeActionData
        )
        val envActions = getEnvActionListById.execute(missionId = missionId)

        assertThat(envActions).isNotNull
        assertThat(envActions.size).isEqualTo(1)
        assertThat(envActions.get(0).id).isEqualTo(action.id)
        assertThat(envActions.get(0).controlSecurity).isEqualTo(mockControl.controlSecurity)
        assertThat(envActions.get(0).controlNavigation).isEqualTo(mockControl.controlNavigation)
        assertThat(envActions.get(0).controlGensDeMer).isEqualTo(mockControl.controlGensDeMer)
        assertThat(envActions.get(0).controlAdministrative).isEqualTo(mockControl.controlAdministrative)
    }
}
