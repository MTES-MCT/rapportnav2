package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetMissionAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetGeneralInfo
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionByExternalId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetNavMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.SyncMissionValidation
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfo2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntity2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.UUID

@SpringBootTest(classes = [GetComputeEnvMission::class])
class GetComputeEnvMissionTest {

    @Autowired
    private lateinit var getComputeEnvMission: GetComputeEnvMission

    @MockitoBean
    private lateinit var getGeneralInfos2: GetGeneralInfo

    @MockitoBean
    private lateinit var getEnvMissionById: GetEnvMissionById

    @MockitoBean
    private lateinit var getMissionAction: GetMissionAction

    @MockitoBean
    private lateinit var getNavMissionById: GetNavMissionById

    @MockitoBean
    private lateinit var getMissionByExternalId: GetMissionByExternalId

    @MockitoBean
    private lateinit var missionNavRepository: IMissionNavRepository

    @MockitoBean
    private lateinit var syncMissionValidation: SyncMissionValidation

    @BeforeEach
    fun setup() {
        // syncLocalMission now returns the resolved row; a freshly-created mirror has null completeness.
        whenever(missionNavRepository.save(any())).thenAnswer { it.arguments[0] as MissionModel }
    }

    @Test
    fun `should throw BackendUsageException when both missionId and envMission are null`() {
        assertThrows(BackendUsageException::class.java) {
            getComputeEnvMission.execute(null, null)
        }
    }

    @Test
    fun `should throw BackendInternalException when missionId is null and envMission has null id`() {
        val envMission = EnvMissionMock.create(id = null)
        assertThrows(BackendInternalException::class.java) {
            getComputeEnvMission.execute(null, envMission)
        }
    }

    @Test
    fun `should return missionEntity2 when envMission is provided with valid id`() {
        val envMission = EnvMissionMock.create(id = 1)

        val actions = listOf(MissionNavActionEntityMock.create())
        val generalInfos = MissionGeneralInfo2Mock.create().toMissionGeneralInfoEntity(missionId = 1)
        val generalInfos2 = MissionGeneralInfoEntity2Mock.create(data = generalInfos)

        `when`(getMissionByExternalId.execute(anyString())).thenReturn(null)
        `when`(getMissionAction.execute(missionId = 1)).thenReturn(actions)
        `when`(getGeneralInfos2.execute(missionId = 1, controlUnits = listOf())).thenReturn(generalInfos2)

        val result = getComputeEnvMission.execute(envMission = envMission)

        assertNotNull(result)
        assertEquals(1, result.id)
        assertEquals(envMission, result.data)
        assertEquals(actions, result.actions)
        assertEquals(generalInfos2, result.generalInfos)
        // no local mirror existed yet, so one is created, and the mission-level validation is synced
        verify(missionNavRepository).save(any())
        verify(syncMissionValidation).execute(result)
    }

    @Test
    fun `should retrieve mission by ID and return missionEntity2`() {
        val mission = EnvMissionMock.create(id = 2)

        val actions = listOf(MissionNavActionEntityMock.create())
        val generalInfos = MissionGeneralInfo2Mock.create().toMissionGeneralInfoEntity(missionId = 2)
        val generalInfos2 = MissionGeneralInfoEntity2Mock.create(data = generalInfos)

        `when`(getMissionByExternalId.execute(anyString())).thenReturn(null)
        `when`(getEnvMissionById.execute(2)).thenReturn(mission)
        `when`(getMissionAction.execute(missionId = 2)).thenReturn(actions)
        `when`(getGeneralInfos2.execute(missionId = 2, controlUnits = listOf())).thenReturn(generalInfos2)

        val result = getComputeEnvMission.execute(missionId = 2)

        assertNotNull(result)
        assertEquals(2, result.id)
        assertEquals(mission, result.data)
        assertEquals(actions, result.actions)
        assertEquals(generalInfos2, result.generalInfos)
        verify(syncMissionValidation).execute(result)
    }

    @Test
    fun `does not use the stored validation shortcut even when the local mirror is stored-complete`() {
        // For this PR we only COLLECT the mission validation; the read shortcut is disabled, so validation
        // must run every time (bypassValidation = false) regardless of the stored is_complete_for_stats.
        val envMission = EnvMissionMock.create(id = 5)
        val storedComplete = MissionModel(
            id = UUID.randomUUID(),
            externalId = "5",
            startDateTimeUtc = Instant.parse("2025-01-02T00:00:00Z"),
            isCompleteForStats = true
        )
        val generalInfos = MissionGeneralInfo2Mock.create().toMissionGeneralInfoEntity(missionId = 5)
        val generalInfos2 = MissionGeneralInfoEntity2Mock.create(data = generalInfos)

        `when`(getMissionByExternalId.execute(anyString())).thenReturn(storedComplete)
        `when`(getMissionAction.execute(missionId = 5, bypassValidation = false)).thenReturn(emptyList())
        `when`(getGeneralInfos2.execute(missionId = 5, controlUnits = listOf())).thenReturn(generalInfos2)

        getComputeEnvMission.execute(envMission = envMission)

        verify(getMissionAction).execute(missionId = 5, bypassValidation = false)
    }

    @Test
    fun `should throw BackendInternalException when mission retrieved by ID has null id`() {
        val mission = EnvMissionMock.create(id = null)

        `when`(getEnvMissionById.execute(3)).thenReturn(mission)

        assertThrows(BackendInternalException::class.java) {
            getComputeEnvMission.execute(missionId = 3)
        }
    }
}
