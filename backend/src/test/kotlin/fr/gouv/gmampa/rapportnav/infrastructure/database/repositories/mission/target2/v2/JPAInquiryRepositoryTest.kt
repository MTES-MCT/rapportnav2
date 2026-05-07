package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.target2.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.InquiryModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.target2.v2.IDBInquiryRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.target2.v2.JPAInquiryRepository
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
@SpringBootTest(classes = [JPAInquiryRepository::class])
class JPAInquiryRepositoryTest {

    @MockitoBean
    private lateinit var dbInquiryRepository: IDBInquiryRepository

    private lateinit var jpaInquiryRepository: JPAInquiryRepository

    private val inquiryId = UUID.randomUUID()
    private val inquiryModel = InquiryModel(
        id = inquiryId,
        startDateTimeUtc = Instant.now()
    )

    @BeforeEach
    fun setUp() {
        jpaInquiryRepository = JPAInquiryRepository(dbInquiryRepository)
    }

    @Test
    fun `findById should return inquiry when found`() {
        `when`(dbInquiryRepository.findById(inquiryId)).thenReturn(Optional.of(inquiryModel))

        val result = jpaInquiryRepository.findById(inquiryId)

        assertThat(result).isPresent
        assertThat(result.get().id).isEqualTo(inquiryId)
        verify(dbInquiryRepository).findById(inquiryId)
    }

    @Test
    fun `findById should return empty optional when not found`() {
        val unknownId = UUID.randomUUID()
        `when`(dbInquiryRepository.findById(unknownId)).thenReturn(Optional.empty())

        val result = jpaInquiryRepository.findById(unknownId)

        assertThat(result).isEmpty
        verify(dbInquiryRepository).findById(unknownId)
    }

    @Test
    fun `save should return saved inquiry`() {
        `when`(dbInquiryRepository.save(any<InquiryModel>())).thenReturn(inquiryModel)

        val result = jpaInquiryRepository.save(inquiryModel)

        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(inquiryId)
        verify(dbInquiryRepository).save(inquiryModel)
    }

    @Test
    fun `save should throw BackendUsageException on InvalidDataAccessApiUsageException`() {
        `when`(dbInquiryRepository.save(any<InquiryModel>()))
            .thenThrow(InvalidDataAccessApiUsageException("Invalid data"))

        val exception = assertThrows<BackendUsageException> {
            jpaInquiryRepository.save(inquiryModel)
        }
        assertThat(exception.message).contains(inquiryId.toString())
    }

    @Test
    fun `save should throw BackendInternalException on unexpected error`() {
        `when`(dbInquiryRepository.save(any<InquiryModel>()))
            .thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaInquiryRepository.save(inquiryModel)
        }
        assertThat(exception.message).contains("Unable to prepare data before saving")
    }

    @Test
    fun `findByServiceId should return list of inquiries`() {
        val serviceId = 42
        `when`(dbInquiryRepository.findByServiceId(serviceId)).thenReturn(listOf(inquiryModel))

        val result = jpaInquiryRepository.findByServiceId(serviceId)

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(inquiryId)
        verify(dbInquiryRepository).findByServiceId(serviceId)
    }

    @Test
    fun `findByServiceId should return empty list when none found`() {
        `when`(dbInquiryRepository.findByServiceId(999)).thenReturn(emptyList())

        val result = jpaInquiryRepository.findByServiceId(999)

        assertThat(result).isEmpty()
    }

    @Test
    fun `findAll should return inquiries between dates`() {
        val start = Instant.now().minusSeconds(3600)
        val end = Instant.now()
        `when`(dbInquiryRepository.findAllBetweenDates(start, end)).thenReturn(listOf(inquiryModel))

        val result = jpaInquiryRepository.findAll(start, end)

        assertThat(result).hasSize(1)
        assertThat(result[0]?.id).isEqualTo(inquiryId)
        verify(dbInquiryRepository).findAllBetweenDates(
            startBeforeDateTime = start,
            endBeforeDateTime = end
        )
    }

    @Test
    fun `findAll should return empty list when no inquiries in range`() {
        val start = Instant.now().minusSeconds(3600)
        val end = Instant.now()
        `when`(dbInquiryRepository.findAllBetweenDates(start, end)).thenReturn(emptyList())

        val result = jpaInquiryRepository.findAll(start, end)

        assertThat(result).isEmpty()
    }

    @Test
    fun `deleteById should delegate to db repository`() {
        jpaInquiryRepository.deleteById(inquiryId)

        verify(dbInquiryRepository).deleteById(inquiryId)
    }
}