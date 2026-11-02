package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.ResourceUsageEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.ResourceUsageModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.generalInfo.IDBResourceUsageRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.generalInfo.JPAResourceUsageRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.UUID

class JPAResourceUsageRepositoryTest {

    private lateinit var dbRepo: IDBResourceUsageRepository
    private lateinit var repository: JPAResourceUsageRepository

    private val missionId: UUID = UUID.fromString("11111111-1111-1111-1111-111111111111")

    @BeforeEach
    fun setup() {
        dbRepo = mock()
        repository = JPAResourceUsageRepository(dbRepo)
    }

    @Test
    fun `findByMissionId maps models to entities`() {
        whenever(dbRepo.findAllByMissionIdUUID(missionId)).thenReturn(
            listOf(
                ResourceUsageModel(id = 1, missionIdUUID = missionId, resourceId = 7, nbKms = 12.5),
                ResourceUsageModel(id = 2, missionIdUUID = missionId, resourceId = 8, nbEngineHours = 4.0)
            )
        )

        val result = repository.findByMissionId(missionId)

        assertThat(result).hasSize(2)
        assertThat(result.first { it.resourceId == 7 }.nbKms).isEqualTo(12.5)
        assertThat(result.first { it.resourceId == 7 }.nbEngineHours).isNull()
        assertThat(result.first { it.resourceId == 8 }.nbEngineHours).isEqualTo(4.0)
        assertThat(result.map { it.missionIdUUID }.distinct()).containsExactly(missionId)
    }

    @Test
    fun `replaceForMission deletes then flushes then inserts, mapping entities to models`() {
        val usages = listOf(
            ResourceUsageEntity(missionIdUUID = missionId, resourceId = 7, nbKms = 10.0),
            ResourceUsageEntity(missionIdUUID = missionId, resourceId = 8, nbEngineHours = 5.0)
        )

        repository.replaceForMission(missionId, usages)

        // Order matters: the bulk delete must flush before the inserts so the unique
        // (mission_id_uuid, resource_id) constraint is never tripped when a resource is re-saved.
        val order = inOrder(dbRepo)
        order.verify(dbRepo).deleteAllByMissionIdUUID(missionId)
        order.verify(dbRepo).flush()
        val captor = argumentCaptor<List<ResourceUsageModel>>()
        order.verify(dbRepo).saveAll(captor.capture())

        val saved = captor.firstValue
        assertThat(saved).hasSize(2)
        assertThat(saved.map { it.resourceId }).containsExactlyInAnyOrder(7, 8)
        assertThat(saved.first { it.resourceId == 7 }.nbKms).isEqualTo(10.0)
        assertThat(saved.first { it.resourceId == 8 }.nbEngineHours).isEqualTo(5.0)
        assertThat(saved.map { it.missionIdUUID }.distinct()).containsExactly(missionId)
    }

    @Test
    fun `replaceForMission with empty list clears rows without calling saveAll`() {
        repository.replaceForMission(missionId, emptyList())

        val order = inOrder(dbRepo)
        order.verify(dbRepo).deleteAllByMissionIdUUID(missionId)
        order.verify(dbRepo).flush()
        verify(dbRepo, never()).saveAll(any<List<ResourceUsageModel>>())
    }

    @Test
    fun `findByMissionId wraps repository errors in BackendInternalException`() {
        whenever(dbRepo.findAllByMissionIdUUID(any())).thenThrow(RuntimeException("boom"))

        assertThrows<BackendInternalException> { repository.findByMissionId(missionId) }
    }

    @Test
    fun `replaceForMission wraps repository errors in BackendInternalException`() {
        doThrow(RuntimeException("boom")).whenever(dbRepo).deleteAllByMissionIdUUID(any())

        assertThrows<BackendInternalException> {
            repository.replaceForMission(
                missionId,
                listOf(ResourceUsageEntity(missionIdUUID = missionId, resourceId = 7, nbKms = 1.0))
            )
        }
    }
}
