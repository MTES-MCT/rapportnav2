package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionByExternalId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.SyncLocalMissionWithMonitorEnv
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.UUID

class SyncLocalMissionWithMonitorEnvTest {

    private val getMissionByExternalId: GetMissionByExternalId = mock()
    private val missionNavRepository: IMissionNavRepository = mock()
    private val useCase = SyncLocalMissionWithMonitorEnv(getMissionByExternalId, missionNavRepository)

    /** Builds a local row whose reconciled fields already match [env], so overrideFromEnv sees no change. */
    private fun rowMatching(env: fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity) =
        MissionModel(
            id = UUID.randomUUID(),
            externalId = "7",
            startDateTimeUtc = env.startDateTimeUtc,
            endDateTimeUtc = env.endDateTimeUtc,
            missionSource = env.missionSource,
            isDeleted = env.isDeleted ?: false,
            observationsByUnit = env.observationsByUnit,
            openBy = env.openBy,
            completedBy = env.completedBy
        )

    @Test
    fun `creates a new local mirror row when none exists yet`() {
        val env = EnvMissionMock.create(id = 7, openBy = "CACEM", observationsByUnit = "obs")
        whenever(getMissionByExternalId.execute("7")).thenReturn(null)
        whenever(missionNavRepository.save(any())).thenAnswer { it.arguments[0] as MissionModel }

        val result = useCase.execute(externalId = 7, env = env)

        // keyed by the MonitorEnv external id, common fields copied from MonitorEnv
        assertEquals("7", result.externalId)
        assertEquals(env.startDateTimeUtc, result.startDateTimeUtc)
        assertEquals(env.endDateTimeUtc, result.endDateTimeUtc)
        assertEquals(env.missionSource, result.missionSource)
        assertEquals("CACEM", result.openBy)
        assertEquals("obs", result.observationsByUnit)
        verify(missionNavRepository).save(any())
    }

    @Test
    fun `returns the existing row without saving when nothing changed`() {
        val env = EnvMissionMock.create(id = 7, openBy = "CACEM", observationsByUnit = "obs")
        val existing = rowMatching(env)
        whenever(getMissionByExternalId.execute("7")).thenReturn(existing)

        val result = useCase.execute(externalId = 7, env = env)

        assertSame(existing, result)
        // change-guard: no needless write when the mirror is already in sync
        verify(missionNavRepository, never()).save(any())
    }

    @Test
    fun `reconciles and saves the existing row when a MonitorEnv field changed`() {
        val env = EnvMissionMock.create(id = 7, openBy = "CNSP", observationsByUnit = "new-obs")
        val existing = rowMatching(env).apply {
            observationsByUnit = "old-obs"
            openBy = "CACEM"
        }
        whenever(getMissionByExternalId.execute("7")).thenReturn(existing)
        whenever(missionNavRepository.save(any())).thenAnswer { it.arguments[0] as MissionModel }

        val result = useCase.execute(externalId = 7, env = env)

        // reconciled to MonitorEnv (the source of truth) and persisted
        assertEquals("new-obs", result.observationsByUnit)
        assertEquals("CNSP", result.openBy)
        verify(missionNavRepository).save(existing)
    }
}
