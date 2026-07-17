package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.IDBMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.JPAMissionNavRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPAMissionNavRepository::class])
class JPAMissionNavRepositoryTest {

    @MockitoBean
    private lateinit var dbRepository: IDBMissionRepository

    private lateinit var jpaMissionNavRepository: JPAMissionNavRepository

    private val missionId = UUID.randomUUID()
    private val missionModel = MissionModel(
        id = missionId,
        serviceId = 1,
        startDateTimeUtc = Instant.now(),
        endDateTimeUtc = Instant.now().plusSeconds(3600)
    )

    @BeforeEach
    fun setUp() {
        jpaMissionNavRepository = JPAMissionNavRepository(dbRepository)
    }

    @Test
    fun `save should return saved mission`() {
        `when`(dbRepository.save(any<MissionModel>())).thenReturn(missionModel)

        val result = jpaMissionNavRepository.save(missionModel)

        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(missionId)
        verify(dbRepository).save(missionModel)
    }

    @Test
    fun `save should throw BackendUsageException on invalid data`() {
        `when`(dbRepository.save(any<MissionModel>())).thenThrow(InvalidDataAccessApiUsageException("Invalid data"))

        val exception = assertThrows<BackendUsageException> {
            jpaMissionNavRepository.save(missionModel)
        }
        assertThat(exception.message).contains("Unable to save MissionNav")
    }

    @Test
    fun `save should throw BackendInternalException on database error`() {
        `when`(dbRepository.save(any<MissionModel>())).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaMissionNavRepository.save(missionModel)
        }
        assertThat(exception.message).contains("Unable to prepare data before saving MissionNav")
    }

    @Test
    fun `findById should return mission when found`() {
        `when`(dbRepository.findById(missionId)).thenReturn(Optional.of(missionModel))

        val result = jpaMissionNavRepository.findById(missionId)

        assertThat(result).isPresent
        assertThat(result.get().id).isEqualTo(missionId)
        verify(dbRepository).findById(missionId)
    }

    @Test
    fun `findById should return empty optional when not found`() {
        val unknownId = UUID.randomUUID()
        `when`(dbRepository.findById(unknownId)).thenReturn(Optional.empty())

        val result = jpaMissionNavRepository.findById(unknownId)

        assertThat(result).isEmpty
        verify(dbRepository).findById(unknownId)
    }

    @Test
    fun `findById should throw BackendInternalException on error`() {
        `when`(dbRepository.findById(missionId)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaMissionNavRepository.findById(missionId)
        }
        assertThat(exception.message).contains("Failed to find MissionNav")
    }

    @Test
    fun `findAll should return list of missions between dates`() {
        val startDate = Instant.now().minusSeconds(3600)
        val endDate = Instant.now()

        `when`(dbRepository.findAllBetweenDates(startDate, endDate)).thenReturn(listOf(missionModel))

        val result = jpaMissionNavRepository.findAll(startDate, endDate)

        assertThat(result).hasSize(1)
        assertThat(result[0]?.id).isEqualTo(missionId)
        verify(dbRepository).findAllBetweenDates(startDate, endDate)
    }

    @Test
    fun `findAll should return empty list when no missions`() {
        val startDate = Instant.now().minusSeconds(3600)
        val endDate = Instant.now()

        `when`(dbRepository.findAllBetweenDates(startDate, endDate)).thenReturn(emptyList())

        val result = jpaMissionNavRepository.findAll(startDate, endDate)

        assertThat(result).isEmpty()
        verify(dbRepository).findAllBetweenDates(startDate, endDate)
    }

    @Test
    fun `findAll should throw BackendInternalException on error`() {
        val startDate = Instant.now().minusSeconds(3600)
        val endDate = Instant.now()

        `when`(dbRepository.findAllBetweenDates(startDate, endDate)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaMissionNavRepository.findAll(startDate, endDate)
        }
        assertThat(exception.message).contains("Failed to find MissionNav between dates")
    }

    @Test
    fun `deleteById should delete mission successfully`() {
        jpaMissionNavRepository.deleteById(missionId)

        verify(dbRepository).deleteById(missionId)
    }

    @Test
    fun `deleteById should throw BackendUsageException on invalid data`() {
        `when`(dbRepository.deleteById(missionId)).thenThrow(InvalidDataAccessApiUsageException("Invalid data"))

        val exception = assertThrows<BackendUsageException> {
            jpaMissionNavRepository.deleteById(missionId)
        }
        assertThat(exception.message).contains("Unable to delete MissionNav")
    }

    @Test
    fun `deleteById should throw BackendInternalException on database error`() {
        `when`(dbRepository.deleteById(missionId)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaMissionNavRepository.deleteById(missionId)
        }
        assertThat(exception.message).contains("Failed to delete MissionNav")
    }

    @Test
    fun `findAllPaginated should return paginated missions ordered by start date desc`() {
        val page = PageImpl(listOf(missionModel), PageRequest.of(0, 10), 1)
        `when`(dbRepository.findAllByOrderByStartDateTimeUtcDesc(any<Pageable>())).thenReturn(page)

        val result = jpaMissionNavRepository.findAllPaginated(0, 10)

        assertThat(result.content).hasSize(1)
        assertThat(result.content[0].id).isEqualTo(missionId)
        assertThat(result.totalElements).isEqualTo(1)
        verify(dbRepository).findAllByOrderByStartDateTimeUtcDesc(PageRequest.of(0, 10))
    }

    @Test
    fun `findAllPaginated should throw BackendInternalException on error`() {
        `when`(dbRepository.findAllByOrderByStartDateTimeUtcDesc(any<Pageable>()))
            .thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaMissionNavRepository.findAllPaginated(0, 10)
        }
        assertThat(exception.message).contains("Failed to find paginated MissionNav")
    }

    @Test
    fun `findByIdPaginated should return paginated missions matching the id`() {
        val page = PageImpl(listOf(missionModel), PageRequest.of(0, 10), 1)
        `when`(dbRepository.findByIdOrderByStartDateTimeUtcDesc(any<UUID>(), any<Pageable>())).thenReturn(page)

        val result = jpaMissionNavRepository.findByIdPaginated(missionId, 0, 10)

        assertThat(result.content).hasSize(1)
        assertThat(result.content[0].id).isEqualTo(missionId)
        verify(dbRepository).findByIdOrderByStartDateTimeUtcDesc(missionId, PageRequest.of(0, 10))
    }

    @Test
    fun `findByIdPaginated should throw BackendInternalException on error`() {
        `when`(dbRepository.findByIdOrderByStartDateTimeUtcDesc(any<UUID>(), any<Pageable>()))
            .thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaMissionNavRepository.findByIdPaginated(missionId, 0, 10)
        }
        assertThat(exception.message).contains("Failed to find paginated MissionNav by id")
    }

    @Test
    fun `findByExternalIdPaginated should return paginated missions matching the external id`() {
        val page = PageImpl(listOf(missionModel), PageRequest.of(0, 10), 1)
        `when`(dbRepository.findByExternalIdOrderByStartDateTimeUtcDesc(any<String>(), any<Pageable>())).thenReturn(page)

        val result = jpaMissionNavRepository.findByExternalIdPaginated("12345", 0, 10)

        assertThat(result.content).hasSize(1)
        verify(dbRepository).findByExternalIdOrderByStartDateTimeUtcDesc("12345", PageRequest.of(0, 10))
    }

    @Test
    fun `findByExternalIdPaginated should throw BackendInternalException on error`() {
        `when`(dbRepository.findByExternalIdOrderByStartDateTimeUtcDesc(any<String>(), any<Pageable>()))
            .thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaMissionNavRepository.findByExternalIdPaginated("12345", 0, 10)
        }
        assertThat(exception.message).contains("Failed to find paginated MissionNav by externalId")
    }

    @Test
    fun `softDeleteById should set isDeleted to true and save`() {
        val model = MissionModel(id = missionId, serviceId = 1, startDateTimeUtc = Instant.now())
        `when`(dbRepository.findById(missionId)).thenReturn(Optional.of(model))
        `when`(dbRepository.save(any<MissionModel>())).thenReturn(model)

        jpaMissionNavRepository.softDeleteById(missionId)

        verify(dbRepository).save(argThat { isDeleted })
        assertThat(model.isDeleted).isTrue()
    }

    @Test
    fun `softDeleteById should throw BackendUsageException when mission not found`() {
        `when`(dbRepository.findById(missionId)).thenReturn(Optional.empty())

        val exception = assertThrows<BackendUsageException> {
            jpaMissionNavRepository.softDeleteById(missionId)
        }
        assertThat(exception.message).contains("not found")
        verify(dbRepository, org.mockito.Mockito.never()).save(any<MissionModel>())
    }

    @Test
    fun `softDeleteById should throw BackendInternalException on database error`() {
        `when`(dbRepository.findById(missionId)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaMissionNavRepository.softDeleteById(missionId)
        }
        assertThat(exception.message).contains("Failed to soft-delete MissionNav")
    }
}
