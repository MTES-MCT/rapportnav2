package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.fish

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FacadeTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FishAuctionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.fish.FishAuctionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.fish.IDBFishAuctionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.fish.JPAFishAuctionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.assertArg
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPAFishAuctionRepository::class])
class JPAFishAuctionRepositoryTest {

    @MockitoBean
    private lateinit var dbFishAuctionRepository: IDBFishAuctionRepository

    private lateinit var jpaFishAuctionRepository: JPAFishAuctionRepository

    private val fishAuctionModel = FishAuctionModel(id = 1, name = "Boulogne-sur-Mer", facade = "MEMN")

    @BeforeEach
    fun setUp() {
        jpaFishAuctionRepository = JPAFishAuctionRepository(dbFishAuctionRepository)
    }

    // findAll

    @Test
    fun `findAll should return list of fish auction entities`() {
        val models = listOf(
            fishAuctionModel,
            FishAuctionModel(id = 2, name = "Saint-Malo", facade = "NAMO")
        )
        `when`(dbFishAuctionRepository.findAll()).thenReturn(models)

        val result = jpaFishAuctionRepository.findAll()

        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(1)
        assertThat(result[0].name).isEqualTo("Boulogne-sur-Mer")
        assertThat(result[0].facade).isEqualTo(FacadeTypeEnum.MEMN)
        assertThat(result[1].facade).isEqualTo(FacadeTypeEnum.NAMO)
        verify(dbFishAuctionRepository).findAll()
    }

    @Test
    fun `findAll should return empty list when no auctions`() {
        `when`(dbFishAuctionRepository.findAll()).thenReturn(emptyList())

        val result = jpaFishAuctionRepository.findAll()

        assertThat(result).isEmpty()
        verify(dbFishAuctionRepository).findAll()
    }

    @Test
    fun `findAll should throw BackendInternalException on error`() {
        `when`(dbFishAuctionRepository.findAll()).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaFishAuctionRepository.findAll()
        }
        assertThat(exception.message).contains("findAll failed")
    }

    // findById

    @Test
    fun `findById should return entity when found`() {
        `when`(dbFishAuctionRepository.findById(1)).thenReturn(Optional.of(fishAuctionModel))

        val result = jpaFishAuctionRepository.findById(1)

        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(1)
        assertThat(result?.name).isEqualTo("Boulogne-sur-Mer")
        assertThat(result?.facade).isEqualTo(FacadeTypeEnum.MEMN)
        verify(dbFishAuctionRepository).findById(1)
    }

    @Test
    fun `findById should return null when not found`() {
        `when`(dbFishAuctionRepository.findById(999)).thenReturn(Optional.empty())

        val result = jpaFishAuctionRepository.findById(999)

        assertThat(result).isNull()
        verify(dbFishAuctionRepository).findById(999)
    }

    @Test
    fun `findById should throw BackendInternalException on error`() {
        `when`(dbFishAuctionRepository.findById(1)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaFishAuctionRepository.findById(1)
        }
        assertThat(exception.message).contains("findById failed")
    }

    // save

    @Test
    fun `save should return saved entity`() {
        val entity = FishAuctionEntity(id = null, name = "Sète", facade = FacadeTypeEnum.MED)
        val savedModel = FishAuctionModel(id = 10, name = "Sète", facade = "MED")
        `when`(dbFishAuctionRepository.save(any<FishAuctionModel>())).thenReturn(savedModel)

        val result = jpaFishAuctionRepository.save(entity)

        assertThat(result.id).isEqualTo(10)
        assertThat(result.name).isEqualTo("Sète")
        assertThat(result.facade).isEqualTo(FacadeTypeEnum.MED)
    }

    @Test
    fun `save should throw BackendUsageException on invalid data`() {
        val entity = FishAuctionEntity(name = "Test", facade = FacadeTypeEnum.MEMN)
        `when`(dbFishAuctionRepository.save(any<FishAuctionModel>())).thenThrow(InvalidDataAccessApiUsageException("Invalid data"))

        val exception = assertThrows<BackendUsageException> {
            jpaFishAuctionRepository.save(entity)
        }
        assertThat(exception.message).contains("save failed")
    }

    @Test
    fun `save should throw BackendInternalException on database error`() {
        val entity = FishAuctionEntity(name = "Test", facade = FacadeTypeEnum.MEMN)
        `when`(dbFishAuctionRepository.save(any<FishAuctionModel>())).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaFishAuctionRepository.save(entity)
        }
        assertThat(exception.message).contains("save failed")
    }

    // deleteById

    @Test
    fun `deleteById should soft delete by setting deletedAt`() {
        `when`(dbFishAuctionRepository.findById(1)).thenReturn(Optional.of(fishAuctionModel))
        `when`(dbFishAuctionRepository.save(any<FishAuctionModel>())).thenReturn(fishAuctionModel)

        jpaFishAuctionRepository.deleteById(1)

        verify(dbFishAuctionRepository).findById(1)
        verify(dbFishAuctionRepository).save(assertArg { model ->
            assertThat(model.deletedAt).isNotNull()
        })
    }

    @Test
    fun `deleteById should do nothing when not found`() {
        `when`(dbFishAuctionRepository.findById(999)).thenReturn(Optional.empty())

        jpaFishAuctionRepository.deleteById(999)

        verify(dbFishAuctionRepository).findById(999)
    }

    @Test
    fun `deleteById should throw BackendUsageException on invalid data`() {
        `when`(dbFishAuctionRepository.findById(1)).thenReturn(Optional.of(fishAuctionModel))
        `when`(dbFishAuctionRepository.save(any<FishAuctionModel>())).thenThrow(InvalidDataAccessApiUsageException("Invalid data"))

        val exception = assertThrows<BackendUsageException> {
            jpaFishAuctionRepository.deleteById(1)
        }
        assertThat(exception.message).contains("deleteById failed")
    }

    @Test
    fun `deleteById should throw BackendInternalException on database error`() {
        `when`(dbFishAuctionRepository.findById(1)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaFishAuctionRepository.deleteById(1)
        }
        assertThat(exception.message).contains("deleteById failed")
    }
}
