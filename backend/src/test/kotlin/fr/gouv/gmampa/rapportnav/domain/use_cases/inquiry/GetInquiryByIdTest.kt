package fr.gouv.gmampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.GetInquiryById
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.InquiryModel
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [GetInquiryById::class])
@ContextConfiguration(classes = [GetInquiryById::class])
class GetInquiryByIdTest {

    @Autowired
    private lateinit var getInquiryById: GetInquiryById

    @MockitoBean
    private lateinit var inquiryRepo: IInquiryRepository

    @Test
    fun `should not call repository when id is null`() {
        val id = UUID.randomUUID()
        getInquiryById.execute(id = null)
        verify(inquiryRepo, times(0)).findById(id = id)
    }


    @Test
    fun `should find inquiry by id`() {
        val id = UUID.randomUUID()
        val model = InquiryModel(
            id = id,
            startDateTimeUtc = Instant.now(),
            status = InquiryStatusType.NEW.toString()
        )
        `when`(inquiryRepo.findById(id = id)).thenReturn(Optional.of(model))

        getInquiryById.execute(id = id)
        verify(inquiryRepo, times(1)).findById(id = id)
    }
}
