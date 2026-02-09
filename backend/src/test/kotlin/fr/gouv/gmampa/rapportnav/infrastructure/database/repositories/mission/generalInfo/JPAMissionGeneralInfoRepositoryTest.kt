package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.GeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.GeneralInfoModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.generalInfo.IDBMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.generalInfo.JPAGeneralInfoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPAGeneralInfoRepository::class])
class JPAMissionGeneralInfoRepositoryTest {

    @MockitoBean
    private lateinit var dbRepo: IDBMissionGeneralInfoRepository

    private lateinit var jpaRepository: JPAGeneralInfoRepository

    private val missionIdUUID = UUID.randomUUID()
    private val generalInfoModel = GeneralInfoModel(
        id = 1,
        missionId = 100,
        missionIdUUID = missionIdUUID,
        distanceInNauticalMiles = 50.5f,
        consumedGOInLiters = 100.0f
    )

    @BeforeEach
    fun setUp() {
        jpaRepository = JPAGeneralInfoRepository(dbRepo)
    }

    @Test
    fun `findAll should return list of general info`() {
        `when`(dbRepo.findAll()).thenReturn(listOf(generalInfoModel))

        val result = jpaRepository.findAll()

        assertThat(result).hasSize(1)
        assertThat(result[0].missionId).isEqualTo(100)
        verify(dbRepo).findAll()
    }

    @Test
    fun `findAll should return empty list when none found`() {
        `when`(dbRepo.findAll()).thenReturn(emptyList())

        val result = jpaRepository.findAll()

        assertThat(result).isEmpty()
        verify(dbRepo).findAll()
    }

    @Test
    fun `findAll should throw BackendInternalException on error`() {
        `when`(dbRepo.findAll()).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaRepository.findAll()
        }
        assertThat(exception.message).contains("Failed to find all MissionGeneralInfo")
    }

    @Test
    fun `findByMissionId should return general info when found`() {
        `when`(dbRepo.findByMissionId(100)).thenReturn(Optional.of(generalInfoModel))

        val result = jpaRepository.findByMissionId(100)

        assertThat(result).isPresent
        assertThat(result.get().missionId).isEqualTo(100)
        verify(dbRepo).findByMissionId(100)
    }

    @Test
    fun `findByMissionId should return empty optional when not found`() {
        `when`(dbRepo.findByMissionId(999)).thenReturn(Optional.empty())

        val result = jpaRepository.findByMissionId(999)

        assertThat(result).isEmpty
        verify(dbRepo).findByMissionId(999)
    }

    @Test
    fun `findByMissionId should throw BackendInternalException on error`() {
        `when`(dbRepo.findByMissionId(100)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaRepository.findByMissionId(100)
        }
        assertThat(exception.message).contains("Failed to find MissionGeneralInfo for missionId")
    }

    @Test
    fun `findAllByMissionId should return list of general info`() {
        `when`(dbRepo.findAllByMissionId(100)).thenReturn(listOf(generalInfoModel))

        val result = jpaRepository.findAllByMissionId(100)

        assertThat(result).hasSize(1)
        verify(dbRepo).findAllByMissionId(100)
    }

    @Test
    fun `findAllByMissionId should throw BackendInternalException on error`() {
        `when`(dbRepo.findAllByMissionId(100)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaRepository.findAllByMissionId(100)
        }
        assertThat(exception.message).contains("Failed to find all MissionGeneralInfo for missionId")
    }

    @Test
    fun `findByMissionIdUUID should return general info when found`() {
        `when`(dbRepo.findByMissionIdUUID(missionIdUUID)).thenReturn(Optional.of(generalInfoModel))

        val result = jpaRepository.findByMissionIdUUID(missionIdUUID)

        assertThat(result).isPresent
        assertThat(result.get().missionIdUUID).isEqualTo(missionIdUUID)
        verify(dbRepo).findByMissionIdUUID(missionIdUUID)
    }

    @Test
    fun `findByMissionIdUUID should throw BackendInternalException on error`() {
        `when`(dbRepo.findByMissionIdUUID(missionIdUUID)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaRepository.findByMissionIdUUID(missionIdUUID)
        }
        assertThat(exception.message).contains("Failed to find MissionGeneralInfo for missionIdUUID")
    }

    @Test
    fun `findAllByMissionIdUUID should return list of general info`() {
        `when`(dbRepo.findAllByMissionIdUUID(missionIdUUID)).thenReturn(listOf(generalInfoModel))

        val result = jpaRepository.findAllByMissionIdUUID(missionIdUUID)

        assertThat(result).hasSize(1)
        verify(dbRepo).findAllByMissionIdUUID(missionIdUUID)
    }

    @Test
    fun `findAllByMissionIdUUID should throw BackendInternalException on error`() {
        `when`(dbRepo.findAllByMissionIdUUID(missionIdUUID)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaRepository.findAllByMissionIdUUID(missionIdUUID)
        }
        assertThat(exception.message).contains("Failed to find all MissionGeneralInfo for missionIdUUID")
    }

    @Test
    fun `findById should return general info when found`() {
        `when`(dbRepo.findById(1)).thenReturn(Optional.of(generalInfoModel))

        val result = jpaRepository.findById(1)

        assertThat(result).isPresent
        assertThat(result.get().id).isEqualTo(1)
        verify(dbRepo).findById(1)
    }

    @Test
    fun `findById should return empty optional when not found`() {
        `when`(dbRepo.findById(999)).thenReturn(Optional.empty())

        val result = jpaRepository.findById(999)

        assertThat(result).isEmpty
        verify(dbRepo).findById(999)
    }

    @Test
    fun `findById should throw BackendInternalException on error`() {
        `when`(dbRepo.findById(1)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaRepository.findById(1)
        }
        assertThat(exception.message).contains("Failed to find MissionGeneralInfo with id")
    }

    @Test
    fun `existsById should return true when exists`() {
        `when`(dbRepo.existsById(1)).thenReturn(true)

        val result = jpaRepository.existsById(1)

        assertThat(result).isTrue()
        verify(dbRepo).existsById(1)
    }

    @Test
    fun `existsById should return false when not exists`() {
        `when`(dbRepo.existsById(999)).thenReturn(false)

        val result = jpaRepository.existsById(999)

        assertThat(result).isFalse()
        verify(dbRepo).existsById(999)
    }

    @Test
    fun `existsById should throw BackendInternalException on error`() {
        `when`(dbRepo.existsById(1)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaRepository.existsById(1)
        }
        assertThat(exception.message).contains("Failed to check existence")
    }

    @Test
    fun `deleteById should delete successfully`() {
        jpaRepository.deleteById(1)

        verify(dbRepo).deleteById(1)
    }

    @Test
    fun `deleteById should throw BackendUsageException on invalid data`() {
        `when`(dbRepo.deleteById(1)).thenThrow(InvalidDataAccessApiUsageException("Invalid data"))

        val exception = assertThrows<BackendUsageException> {
            jpaRepository.deleteById(1)
        }
        assertThat(exception.message).contains("Unable to delete MissionGeneralInfo")
    }

    @Test
    fun `deleteById should throw BackendInternalException on database error`() {
        `when`(dbRepo.deleteById(1)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaRepository.deleteById(1)
        }
        assertThat(exception.message).contains("Failed to delete MissionGeneralInfo")
    }

    @Test
    fun `save should return saved model`() {
        val entity = GeneralInfoEntity(
            id = 1,
            missionId = 100,
            distanceInNauticalMiles = 50.5f
        )

        `when`(dbRepo.save(any<GeneralInfoModel>())).thenReturn(generalInfoModel)

        val result = jpaRepository.save(entity.toGeneralInfoModel())

        assertThat(result).isNotNull
        assertThat(result.missionId).isEqualTo(100)
        verify(dbRepo).save(any<GeneralInfoModel>())
    }

    @Test
    fun `save should throw BackendUsageException on invalid data`() {
        val entity = GeneralInfoEntity(
            id = 1,
            missionId = 100
        )

        `when`(dbRepo.save(any<GeneralInfoModel>())).thenThrow(InvalidDataAccessApiUsageException("Invalid data"))

        val exception = assertThrows<BackendUsageException> {
            jpaRepository.save(entity.toGeneralInfoModel())
        }
        assertThat(exception.message).contains("Unable to save MissionGeneralInfo")
    }

    @Test
    fun `save should throw BackendInternalException on database error`() {
        val entity = GeneralInfoEntity(
            id = 1,
            missionId = 100
        )

        `when`(dbRepo.save(any<GeneralInfoModel>())).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaRepository.save(entity.toGeneralInfoModel())
        }
        assertThat(exception.message).contains("Unable to prepare data before saving MissionGeneralInfo")
    }
}
