package fr.gouv.gmampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.CreateInquiry
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Inquiry
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
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
    fun `should create a new inquiry`() {
        val id = UUID.randomUUID()
        val inquiry = Inquiry(
            id = id,
            startDateTimeUtc = Instant.now(),
            status = InquiryStatusType.IN_PROGRESS
        )
        val model = inquiry.toInquiryEntity().toInquiryModel()
        `when`(inquiryRepo.save(model)).thenReturn(model)

        createInquiry.execute(inquiry = inquiry)
        verify(inquiryRepo, times(1)).save(model)
    }
}
