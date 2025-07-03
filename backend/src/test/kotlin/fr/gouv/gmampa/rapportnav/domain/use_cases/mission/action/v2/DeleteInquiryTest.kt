package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [DeleteInquiry::class])
@ContextConfiguration(classes = [DeleteInquiry::class])
class DeleteInquiryTest {

    @Autowired
    private lateinit var deleteInquiry: DeleteInquiry

    @MockitoBean
    private lateinit var inquiryRepo: IInquiryRepository

    @Test
    fun `should not call repository when type is not CROSS CONTROL or id is null`() {
        val id = UUID.randomUUID()
        deleteInquiry = DeleteInquiry(inquiryRepo = inquiryRepo)
        deleteInquiry.execute(id  = null, actionType = ActionType.CROSS_CONTROL)
        verify(inquiryRepo, times(0)).deleteById(id)

        deleteInquiry.execute(id  = id, actionType = ActionType.CONTROL)
        verify(inquiryRepo, times(0)).deleteById(id)
    }


    @Test
    fun `should not call repository delete if status is Follow up`() {
        val id = UUID.randomUUID()
        val actionType = ActionType.CROSS_CONTROL

        val inquiryControl = InquiryEntity(
            id = id,
            startDateTimeUtc = Instant.now(),
            status = InquiryStatusType.FOLLOW_UP
        ).toInquiryModel()

        `when`(inquiryRepo.findById(id)).thenReturn(Optional.of(inquiryControl))
        deleteInquiry = DeleteInquiry(inquiryRepo = inquiryRepo)
        deleteInquiry.execute(id  = id, actionType = actionType)
        verify(inquiryRepo, times(0)).deleteById(id)
    }

    @Test
    fun `should call delete of repository `() {
        val id = UUID.randomUUID()
        val actionType = ActionType.CROSS_CONTROL
        val controlControl = InquiryEntity(
            id = id,
            startDateTimeUtc = Instant.now(),
            status = InquiryStatusType.NEW
        ).toInquiryModel()
        `when`(inquiryRepo.findById(id)).thenReturn(Optional.of(controlControl))
        deleteInquiry = DeleteInquiry(inquiryRepo = inquiryRepo)
        deleteInquiry.execute(id  = id, actionType = actionType)
        verify(inquiryRepo, times(1)).deleteById(id)
    }
}
