package fr.gouv.gmampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.GetInquiryByServiceIdBetweenDates
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.InquiryModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [GetInquiryByServiceIdBetweenDates::class])
@ContextConfiguration(classes = [GetInquiryByServiceIdBetweenDates::class])
class GetInquiryByServiceIdBetweenDatesTest {

    @MockitoBean
    private lateinit var inquiryRepo: IInquiryRepository

    @Autowired
    private lateinit var getInquiryByServiceIdBetweenDates: GetInquiryByServiceIdBetweenDates


    @Test
    fun `should return list of cross control of a specific service id`() {

        val startDateTimeUtc = Instant.parse("2025-01-01T04:50:09Z")
        val endDateTimeUtc = Instant.parse("2025-12-31T20:29:03Z")

        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()

        val controlControls = listOf(
            InquiryModel(
                id = id1,
                serviceId = 6,
                startDateTimeUtc = Instant.parse("2025-01-08T04:50:09Z"),
                status = InquiryStatusType.IN_PROGRESS.toString()
            ),
            InquiryModel(
                id = id1,
                serviceId = 6,
                startDateTimeUtc = Instant.parse("2025-09-01T04:50:09Z"),
                status = InquiryStatusType.IN_PROGRESS.toString()
            ),
            InquiryModel(
                id = id1,
                serviceId = 6,
                startDateTimeUtc = Instant.parse("2025-02-01T04:50:09Z"),
                status = InquiryStatusType.CLOSED.toString()
            ),
            InquiryModel(
                id = id2,
                serviceId = 7,
                startDateTimeUtc = Instant.parse("2025-03-01T04:50:09Z"),
                status = InquiryStatusType.CLOSED.toString()
            )
        )

        //Mock
        `when`(inquiryRepo.findAll(startBeforeDateTime = startDateTimeUtc, endBeforeDateTime = endDateTimeUtc)).thenReturn(controlControls)

        //When
        getInquiryByServiceIdBetweenDates = GetInquiryByServiceIdBetweenDates(inquiryRepo = inquiryRepo)
        val response = getInquiryByServiceIdBetweenDates.execute(6, startDateTimeUtc, endDateTimeUtc)

        //Then
        assertThat(response).isNotNull()
        assertThat(response.size).isEqualTo(3)
        assertThat(response.map { it.id }).doesNotContain(id2)
    }
}
