package fr.gouv.gmampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.CreateInquiry
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Inquiry
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.InquiryModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [CreateInquiry::class])
@ContextConfiguration(classes = [CreateInquiry::class])
class CreateInquiryTest {

    @Autowired
    private lateinit var createInquiry: CreateInquiry

    @MockitoBean
    private lateinit var inquiryRepo: IInquiryRepository

    @Test
    fun `creates an inquiry owned by the caller's service, ignoring the body serviceId`() {
        val id = UUID.randomUUID()
        val inquiry = Inquiry(
            id = id,
            serviceId = 999, // attacker-supplied in the body; must be ignored
            startDateTimeUtc = Instant.now(),
            status = InquiryStatusType.IN_PROGRESS
        )
        whenever(inquiryRepo.save(any())).thenAnswer { it.getArgument(0) }

        createInquiry.execute(inquiry = inquiry, serviceId = 10)

        val captor = argumentCaptor<InquiryModel>()
        verify(inquiryRepo).save(captor.capture())
        assertThat(captor.firstValue.serviceId).isEqualTo(10)
    }
}
