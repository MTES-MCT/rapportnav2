package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.generalInfo.IDBMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.generalInfo.JPAMissionGeneralInfoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPAMissionGeneralInfoRepository::class])
class JPAMissionGeneralInfoRepositoryTest {

    @MockitoBean
    private lateinit var dbRepo: IDBMissionGeneralInfoRepository

    private lateinit var jpaRepository: JPAMissionGeneralInfoRepository

    private val missionId = UUID.randomUUID()
    private val generalInfoModel = MissionGeneralInfoModel(
        id = 1,
        missionId = missionId,
        distanceInNauticalMiles = 50.5f,
        consumedGOInLiters = 100.0f
    )

    @BeforeEach
    fun setUp() {
        jpaRepository = JPAMissionGeneralInfoRepository(dbRepo)
    }

    @Test
    fun `findAll should return list of general info`() {
        `when`(dbRepo.findAll()).thenReturn(listOf(generalInfoModel))

        val result = jpaRepository.findAll()

        assertThat(result).hasSize(1)
        assertThat(result[0].missionId).isEqualTo(missionId)
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
    fun `findAllPaginated should return page of general info`() {
        val pageable = PageRequest.of(0, 10)
        val page = PageImpl(listOf(generalInfoModel), pageable, 1)
        `when`(dbRepo.findAllByOrderByMissionIdDesc(pageable)).thenReturn(page)

        val result = jpaRepository.findAllPaginated(0, 10)

        assertThat(result.content).hasSize(1)
        assertThat(result.content[0].missionId).isEqualTo(missionId)
        assertThat(result.totalElements).isEqualTo(1)
        verify(dbRepo).findAllByOrderByMissionIdDesc(pageable)
    }

    @Test
    fun `findAllPaginated should return empty page when none found`() {
        val pageable = PageRequest.of(0, 10)
        val page = PageImpl<MissionGeneralInfoModel>(emptyList(), pageable, 0)
        `when`(dbRepo.findAllByOrderByMissionIdDesc(pageable)).thenReturn(page)

        val result = jpaRepository.findAllPaginated(0, 10)

        assertThat(result.content).isEmpty()
        assertThat(result.totalElements).isEqualTo(0)
        verify(dbRepo).findAllByOrderByMissionIdDesc(pageable)
    }

    @Test
    fun `findAllPaginated should throw BackendInternalException on error`() {
        val pageable = PageRequest.of(0, 10)
        `when`(dbRepo.findAllByOrderByMissionIdDesc(pageable)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaRepository.findAllPaginated(0, 10)
        }
        assertThat(exception.message).contains("Failed to find paginated MissionGeneralInfo")
    }

    @Test
    fun `findByMissionIdPaginated should return page of general info`() {
        val pageable = PageRequest.of(0, 10)
        val page = PageImpl(listOf(generalInfoModel), pageable, 1)
        `when`(dbRepo.findByMissionIdOrderByMissionIdDesc(missionId, pageable)).thenReturn(page)

        val result = jpaRepository.findByMissionIdPaginated(missionId, 0, 10)

        assertThat(result.content).hasSize(1)
        assertThat(result.content[0].missionId).isEqualTo(missionId)
        verify(dbRepo).findByMissionIdOrderByMissionIdDesc(missionId, pageable)
    }

    @Test
    fun `findByMissionIdPaginated should throw BackendInternalException on error`() {
        val pageable = PageRequest.of(0, 10)
        `when`(dbRepo.findByMissionIdOrderByMissionIdDesc(missionId, pageable)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaRepository.findByMissionIdPaginated(missionId, 0, 10)
        }
        assertThat(exception.message).contains("Failed to find paginated MissionGeneralInfo by missionId")
    }

    @Test
    fun `findByMissionId should return general info when found`() {
        `when`(dbRepo.findByMissionId(missionId)).thenReturn(Optional.of(generalInfoModel))

        val result = jpaRepository.findByMissionId(missionId)

        assertThat(result).isPresent
        assertThat(result.get().missionId).isEqualTo(missionId)
        verify(dbRepo).findByMissionId(missionId)
    }

    @Test
    fun `findByMissionId should throw BackendInternalException on error`() {
        `when`(dbRepo.findByMissionId(missionId)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaRepository.findByMissionId(missionId)
        }
        assertThat(exception.message).contains("Failed to find MissionGeneralInfo for missionId")
    }

    @Test
    fun `findAllByMissionId should return list of general info`() {
        `when`(dbRepo.findAllByMissionId(missionId)).thenReturn(listOf(generalInfoModel))

        val result = jpaRepository.findAllByMissionId(missionId)

        assertThat(result).hasSize(1)
        verify(dbRepo).findAllByMissionId(missionId)
    }

    @Test
    fun `findAllByMissionId should throw BackendInternalException on error`() {
        `when`(dbRepo.findAllByMissionId(missionId)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaRepository.findAllByMissionId(missionId)
        }
        assertThat(exception.message).contains("Failed to find all MissionGeneralInfo for missionId")
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
        val entity = MissionGeneralInfoEntity(
            id = 1,
            distanceInNauticalMiles = 50.5f
        )

        `when`(dbRepo.save(any<MissionGeneralInfoModel>())).thenReturn(generalInfoModel)

        val result = jpaRepository.save(entity)

        assertThat(result).isNotNull
        assertThat(result.missionId).isEqualTo(missionId)
        verify(dbRepo).save(any<MissionGeneralInfoModel>())
    }

    @Test
    fun `save should throw BackendUsageException on invalid data`() {
        val entity = MissionGeneralInfoEntity(
            id = 1,
        )

        `when`(dbRepo.save(any<MissionGeneralInfoModel>())).thenThrow(InvalidDataAccessApiUsageException("Invalid data"))

        val exception = assertThrows<BackendUsageException> {
            jpaRepository.save(entity)
        }
        assertThat(exception.message).contains("Unable to save MissionGeneralInfo")
    }

    @Test
    fun `save should throw BackendInternalException on database error`() {
        val entity = MissionGeneralInfoEntity(
            id = 1,
        )

        `when`(dbRepo.save(any<MissionGeneralInfoModel>())).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaRepository.save(entity)
        }
        assertThat(exception.message).contains("Unable to prepare data before saving MissionGeneralInfo")
    }

    @Test
    fun `save should return existing record on duplicate mission_id constraint violation`() {
        val dupeUUID = UUID.randomUUID()
        val entity = MissionGeneralInfoEntity(
            id = 2,
            missionId = dupeUUID
        )

        `when`(dbRepo.save(any<MissionGeneralInfoModel>())).thenThrow(DataIntegrityViolationException("Unique index violation"))
        `when`(dbRepo.findAllByMissionId(dupeUUID)).thenReturn(listOf(generalInfoModel))

        val result = jpaRepository.save(entity)

        assertThat(result.id).isEqualTo(generalInfoModel.id)
    }

    @Test
    fun `save should throw BackendInternalException on constraint violation without mission_id`() {
        val entity = MissionGeneralInfoEntity(
            id = 1,
            missionId = null
        )

        `when`(dbRepo.save(any<MissionGeneralInfoModel>())).thenThrow(DataIntegrityViolationException("Constraint violation"))

        val exception = assertThrows<BackendInternalException> {
            jpaRepository.save(entity)
        }
        assertThat(exception.message).contains("Unable to save MissionGeneralInfo")
    }
}
