package fr.gouv.gmampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.CreateInquiry
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.UpdateInquiry
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Inquiry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [UpdateInquiry::class])
@ContextConfiguration(classes = [UpdateInquiry::class])
class UpdateInquiryTest {

    @Autowired
    private lateinit var updateInquiry: UpdateInquiry

    @MockitoBean
    private lateinit var inquiryRepo: IInquiryRepository

    @Test
    fun `should not update inquiry when id is null`() {
        val id = UUID.randomUUID()
        val inquiry = Inquiry(
            id = id,
            startDateTimeUtc = Instant.now(),
            status = InquiryStatusType.IN_PROGRESS
        )
        val model = inquiry.toInquiryEntity().toInquiryModel()
        `when`(inquiryRepo.save(model)).thenReturn(model)

        updateInquiry.execute(id = null, inquiry = inquiry)
        verify(inquiryRepo, times(0)).save(model)
    }

    @Test
    fun `should not update inquiry when id and input are different`() {
        val id = UUID.randomUUID()
        val inquiry = Inquiry(
            id = UUID.randomUUID(),
            startDateTimeUtc = Instant.now(),
            status = InquiryStatusType.IN_PROGRESS
        )
        val model = inquiry.toInquiryEntity().toInquiryModel()
        `when`(inquiryRepo.save(model)).thenReturn(model)

        updateInquiry.execute(id = id, inquiry = inquiry)
        verify(inquiryRepo, times(0)).save(model)
    }

    @Test
    fun `should not update inquiry find return null`() {
        val id = UUID.randomUUID()
        val inquiry = Inquiry(
            id = id,
            startDateTimeUtc = Instant.now(),
            status = InquiryStatusType.IN_PROGRESS
        )
        val model = inquiry.toInquiryEntity().toInquiryModel()
        `when`(inquiryRepo.findById(id)).thenReturn(Optional.ofNullable(null))

        val response = updateInquiry.execute(id = id, inquiry = inquiry)

        verify(inquiryRepo, times(0)).save(model)
        assertThat(response).isNull()
    }


    @Test
    fun `should update inquiry`() {
        val id = UUID.randomUUID()
        val inquiry = Inquiry(
            id = id,
            startDateTimeUtc = Instant.now(),
            status = InquiryStatusType.IN_PROGRESS
        )
        val model = inquiry.toInquiryEntity().toInquiryModel()
        `when`(inquiryRepo.save(model)).thenReturn(model)
        `when`(inquiryRepo.findById(id)).thenReturn(Optional.ofNullable(model))

        updateInquiry.execute(id = id, inquiry = inquiry)
        verify(inquiryRepo, times(1)).save(model)
    }
}
