package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import java.time.Instant
import java.util.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetActionsByOwnerId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetGeneralInfo2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetNavMissionById2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfo2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntity2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionModelMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetComputeEnvMission::class])
class GetComputeEnvMissionTest {

    @Autowired
    private lateinit var getComputeEnvMission: GetComputeEnvMission

    @MockitoBean
    private lateinit var getGeneralInfos2: GetGeneralInfo2

    @MockitoBean
    private lateinit var getEnvMissionById2: GetEnvMissionById2

    @MockitoBean
    private lateinit var getActionsByOwnerId: GetActionsByOwnerId

    @MockitoBean
    private lateinit var getNavMissionById2: GetNavMissionById2

    @MockitoBean
    private lateinit var missionNavRepository: IMissionNavRepository

    @Test
    fun `should throw BackendUsageException when both missionId and envMission are null`() {
        assertThrows(BackendUsageException::class.java) {
            getComputeEnvMission.execute(null, null)
        }
    }

    @Test
    fun `should throw BackendInternalException when missionId is null and envMission has null id`() {
        val envMission = EnvMissionMock.create(externalId = null)
        assertThrows(BackendInternalException::class.java) {
            getComputeEnvMission.execute(null, envMission)
        }
    }

    @Test
    fun `should return missionEntity2 when envMission is provided with valid id`() {
        val envMission = EnvMissionMock.create(externalId = 1)
        val missionUUID = UUID.randomUUID()
        val missionModel = MissionModelMock.create(id = missionUUID)

        val actions = listOf(MissionNavActionEntityMock.create())
        val generalInfos = MissionGeneralInfo2Mock.create().toMissionGeneralInfoEntity(missionId = missionUUID)
        val generalInfos2 = MissionGeneralInfoEntity2Mock.create(data = generalInfos)

        `when`(missionNavRepository.findByExternalId("1")).thenReturn(Optional.of(missionModel))
        `when`(missionNavRepository.save(anyOrNull())).thenReturn(missionModel)
        `when`(getActionsByOwnerId.execute(missionId = missionUUID)).thenReturn(actions)
        `when`(getGeneralInfos2.execute(missionId = missionUUID, controlUnits = listOf())).thenReturn(generalInfos2)

        val result = getComputeEnvMission.execute(envMission = envMission)

        assertNotNull(result)
        assertNotNull(result.id)
        assertEquals(envMission, result.data)
        assertEquals(actions, result.actions)
        assertEquals(generalInfos2, result.generalInfos)
    }

    @Test
    fun `should retrieve mission by ID and return missionEntity2`() {
        val mission = EnvMissionMock.create(externalId = 2)
        val missionUUID = UUID.randomUUID()
        val missionModel = MissionModelMock.create(id = missionUUID)

        val actions = listOf(MissionNavActionEntityMock.create())
        val generalInfos = MissionGeneralInfo2Mock.create().toMissionGeneralInfoEntity(missionId = missionUUID)
        val generalInfos2 = MissionGeneralInfoEntity2Mock.create(data = generalInfos)

        `when`(missionNavRepository.findByExternalId("2")).thenReturn(Optional.of(missionModel))
        `when`(missionNavRepository.save(anyOrNull())).thenReturn(missionModel)
        `when`(getEnvMissionById2.execute(2)).thenReturn(mission)
        `when`(getActionsByOwnerId.execute(missionId = missionUUID)).thenReturn(actions)
        `when`(getGeneralInfos2.execute(missionId = missionUUID, controlUnits = listOf())).thenReturn(generalInfos2)

        val result = getComputeEnvMission.execute(externalId = 2)

        assertNotNull(result)
        assertNotNull(result.id)
        assertEquals(mission, result.data)
        assertEquals(actions, result.actions)
        assertEquals(generalInfos2, result.generalInfos)
    }

    @Test
    fun `should throw BackendInternalException when mission retrieved by ID has null id`() {
        val mission = EnvMissionMock.create(externalId = null)

        `when`(getEnvMissionById2.execute(3)).thenReturn(mission)

        assertThrows(BackendInternalException::class.java) {
            getComputeEnvMission.execute(externalId = 3)
        }
    }

    @Test
    fun `should override local common fields from MonitorEnv when they differ`() {
        val missionUUID = UUID.randomUUID()
        val staleModel = MissionModel(
            id = missionUUID,
            externalId = "4",
            startDateTimeUtc = Instant.parse("2020-01-01T00:00:00Z"),
            endDateTimeUtc = Instant.parse("2020-01-02T00:00:00Z"),
            missionSource = MissionSourceEnum.MONITORENV,
            observationsByUnit = "old obs"
        )
        val envMission = EnvMissionMock.create(
            externalId = 4,
            startDateTimeUtc = Instant.parse("2022-01-02T12:00:01Z"),
            endDateTimeUtc = Instant.parse("2022-01-03T12:00:01Z"),
            observationsByUnit = "new obs"
        )

        `when`(missionNavRepository.findByExternalId("4")).thenReturn(Optional.of(staleModel))
        `when`(missionNavRepository.save(anyOrNull())).thenReturn(staleModel)
        `when`(getActionsByOwnerId.execute(missionId = missionUUID)).thenReturn(listOf())
        `when`(getGeneralInfos2.execute(missionId = missionUUID, controlUnits = listOf())).thenReturn(null)

        getComputeEnvMission.execute(envMission = envMission)

        val captor = argumentCaptor<MissionModel>()
        verify(missionNavRepository).save(captor.capture())
        val saved = captor.firstValue
        assertEquals(envMission.startDateTimeUtc, saved.startDateTimeUtc)
        assertEquals(envMission.endDateTimeUtc, saved.endDateTimeUtc)
        assertEquals("new obs", saved.observationsByUnit)
    }

    @Test
    fun `should not save when local fields already match MonitorEnv`() {
        val missionUUID = UUID.randomUUID()
        val envMission = EnvMissionMock.create(externalId = 5)
        val upToDateModel = MissionModel(
            id = missionUUID,
            externalId = "5",
            startDateTimeUtc = envMission.startDateTimeUtc,
            endDateTimeUtc = envMission.endDateTimeUtc,
            missionSource = envMission.missionSource,
            isDeleted = envMission.isDeleted ?: false,
            observationsByUnit = envMission.observationsByUnit,
            openBy = envMission.openBy,
            completedBy = envMission.completedBy
        )

        `when`(missionNavRepository.findByExternalId("5")).thenReturn(Optional.of(upToDateModel))
        `when`(getActionsByOwnerId.execute(missionId = missionUUID)).thenReturn(listOf())
        `when`(getGeneralInfos2.execute(missionId = missionUUID, controlUnits = listOf())).thenReturn(null)

        getComputeEnvMission.execute(envMission = envMission)

        verify(missionNavRepository, never()).save(anyOrNull())
    }

    @Test
    fun `should create local row from MonitorEnv when none exists`() {
        val envMission = EnvMissionMock.create(externalId = 6)

        `when`(missionNavRepository.findByExternalId("6")).thenReturn(Optional.empty())
        `when`(missionNavRepository.save(anyOrNull())).thenAnswer { it.arguments[0] as MissionModel }

        getComputeEnvMission.execute(envMission = envMission)

        val captor = argumentCaptor<MissionModel>()
        verify(missionNavRepository).save(captor.capture())
        val saved = captor.firstValue
        assertEquals("6", saved.externalId)
        assertEquals(envMission.startDateTimeUtc, saved.startDateTimeUtc)
        assertEquals(envMission.missionSource, saved.missionSource)
    }
}
