package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.passenger

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.passenger.PassengerModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBMissionPassengerRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew.JPAMissionPassengerRepository
import fr.gouv.gmampa.rapportnav.mocks.mission.passenger.MissionPassengerEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.dao.InvalidDataAccessApiUsageException
import java.util.*

class JPAMissionPassengerRepositoryTest {
    private lateinit var dbRepo: IDBMissionPassengerRepository
    private lateinit var jpaRepo: JPAMissionPassengerRepository

    private val sampleEntity = MissionPassengerEntityMock.create(
        id = 1,
        missionId = 1,
        missionIdUUID = UUID.randomUUID(),
    )
    private val sampleModel = sampleEntity.toPassengerModel()

    @BeforeEach
    fun setUp() {
        dbRepo = mock()
        jpaRepo = JPAMissionPassengerRepository(dbRepo)
    }

    @Test
    fun `findByMissionId should delegate to repository`() {
        whenever(dbRepo.findByMissionId(sampleEntity.missionId!!))
            .thenReturn(listOf<PassengerModel>(sampleModel))

        val result = jpaRepo.findByMissionId(sampleEntity.missionId)
        assertEquals(listOf(sampleModel), result)
    }

    @Test
    fun `findByMissionIdUUID should delegate to repository`() {
        whenever(dbRepo.findByMissionIdUUID(sampleEntity.missionIdUUID!!))
            .thenReturn(listOf<PassengerModel>(sampleModel))

        val result = jpaRepo.findByMissionIdUUID(sampleEntity.missionIdUUID)
        assertEquals(listOf(sampleModel), result)
    }


    @Test
    fun `save should convert entity to model and delegate`() {
        whenever(dbRepo.save(any())).thenReturn(sampleModel)

        val result = jpaRepo.save(sampleEntity)
        assertNotNull(result)
        assertEquals(sampleEntity.id, result.id)
        assertEquals(sampleEntity.missionId, result.missionId)
        verify(dbRepo).save(any())
    }

    @Test
    fun `save should throw BackendUsageException on InvalidDataAccessApiUsageException`() {
        whenever(dbRepo.save(any())).thenThrow(InvalidDataAccessApiUsageException("invalid"))

        val exception = org.junit.jupiter.api.assertThrows<BackendUsageException> {
            jpaRepo.save(sampleEntity)
        }

        assertEquals(BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION, exception.code)
    }

    @Test
    fun `save should throw BackendInternalException on generic exception`() {
        whenever(dbRepo.save(any())).thenThrow(RuntimeException("boom"))

        org.junit.jupiter.api.assertThrows<BackendInternalException> {
            jpaRepo.save(sampleEntity)
        }
    }

    @Test
    fun `deleteById should delete when entity exists`() {
        whenever(dbRepo.findById(sampleModel.id!!)).thenReturn(Optional.of(sampleModel))

        val result = jpaRepo.deleteById(sampleModel.id!!)

        assertEquals(true, result)
        verify(dbRepo).deleteById(sampleModel.id!!)
    }

    @Test
    fun `deleteById should throw NoSuchElementException when not found`() {
        whenever(dbRepo.findById(sampleModel.id!!)).thenReturn(Optional.empty())

        org.junit.jupiter.api.assertThrows<NoSuchElementException> {
            jpaRepo.deleteById(sampleModel.id!!)
        }
    }

}
