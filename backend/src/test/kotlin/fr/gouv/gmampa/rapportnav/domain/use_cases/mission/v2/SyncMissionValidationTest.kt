package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionByExternalId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.SyncMissionValidation
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionNavEntityMock
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.Instant
import java.util.Optional
import java.util.UUID

class SyncMissionValidationTest {

    private val missionNavRepository: IMissionNavRepository = mock()
    private val getMissionByExternalId: GetMissionByExternalId = mock()
    private val sync = SyncMissionValidation(missionNavRepository, getMissionByExternalId)

    private fun modelRow(id: UUID = UUID.randomUUID()) =
        MissionModel(id = id, startDateTimeUtc = Instant.parse("2019-09-01T00:00:00Z"))

    private fun navMissionEntity(uuid: UUID): MissionEntity {
        val data = MissionEnvEntity.fromMissionNavEntity(MissionNavEntityMock.create(id = uuid))
        return MissionEntity(idUUID = uuid, data = data, actions = emptyList(), generalInfos = null)
    }

    @Test
    fun `writes the computed validation onto a nav mission row when it differs`() {
        val uuid = UUID.randomUUID()
        val mission = navMissionEntity(uuid)
        val row = modelRow(uuid).apply {
            // stored value guaranteed different from any computed completeness
            isCompleteForStats = null
        }
        whenever(missionNavRepository.findById(uuid)).thenReturn(Optional.of(row))

        sync.execute(mission)

        val expected = mission.isCompleteForStats()
        verify(missionNavRepository).save(argThat { this.isCompleteForStats == expected.isComplete })
    }

    @Test
    fun `does not write when the stored validation already matches`() {
        val uuid = UUID.randomUUID()
        val mission = navMissionEntity(uuid)
        val expected = mission.isCompleteForStats()
        val row = modelRow(uuid).apply {
            isCompleteForStats = expected.isComplete
            sourcesOfMissingData = expected.sources
                ?.takeIf { it.isNotEmpty() }
                ?.map { it.name }
                ?.sorted()
                ?.joinToString(",")
        }
        whenever(missionNavRepository.findById(uuid)).thenReturn(Optional.of(row))

        sync.execute(mission)

        verify(missionNavRepository, never()).save(any())
    }

    @Test
    fun `resolves an env mission row by its external id`() {
        val data = MissionEnvEntity.fromMissionNavEntity(MissionNavEntityMock.create(id = UUID.randomUUID()))
        val mission = MissionEntity(id = 42, data = data, actions = emptyList(), generalInfos = null)
        val row = modelRow().apply { isCompleteForStats = false }
        whenever(getMissionByExternalId.execute("42")).thenReturn(row)

        sync.execute(mission)

        verify(getMissionByExternalId).execute("42")
        verify(missionNavRepository).save(any())
    }

    @Test
    fun `no-op when no mission row exists`() {
        val uuid = UUID.randomUUID()
        whenever(missionNavRepository.findById(uuid)).thenReturn(Optional.empty())

        sync.execute(navMissionEntity(uuid))

        verify(missionNavRepository, never()).save(any())
    }

    @Test
    fun `swallows persistence errors so a sync failure never breaks the read`() {
        val uuid = UUID.randomUUID()
        whenever(missionNavRepository.findById(uuid)).thenThrow(RuntimeException("db down"))

        assertDoesNotThrow { sync.execute(navMissionEntity(uuid)) }
    }
}
