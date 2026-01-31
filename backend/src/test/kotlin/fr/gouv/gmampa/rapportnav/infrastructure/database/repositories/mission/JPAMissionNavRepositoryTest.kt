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
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.InvalidDataAccessApiUsageException
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
    fun `finById should return mission when found`() {
        `when`(dbRepository.findById(missionId)).thenReturn(Optional.of(missionModel))

        val result = jpaMissionNavRepository.finById(missionId)

        assertThat(result).isPresent
        assertThat(result.get().id).isEqualTo(missionId)
        verify(dbRepository).findById(missionId)
    }

    @Test
    fun `finById should return empty optional when not found`() {
        val unknownId = UUID.randomUUID()
        `when`(dbRepository.findById(unknownId)).thenReturn(Optional.empty())

        val result = jpaMissionNavRepository.finById(unknownId)

        assertThat(result).isEmpty
        verify(dbRepository).findById(unknownId)
    }

    @Test
    fun `finById should throw BackendInternalException on error`() {
        `when`(dbRepository.findById(missionId)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaMissionNavRepository.finById(missionId)
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
}
