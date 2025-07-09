package fr.gouv.gmampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.DeleteInquiry
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.*
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
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

    @MockitoBean
    private lateinit var getNavActionListByOwnerId: GetNavActionListByOwnerId

    @MockitoBean
    private lateinit var deleteNavAction: DeleteNavAction

    @Test
    fun `should not call repository when id is null`() {
        val id = UUID.randomUUID()
        deleteInquiry.execute(id  = null)
        verify(inquiryRepo, times(0)).deleteById(id)
    }


    @Test
    fun `should call delete of repository `() {
        val ownerId = UUID.randomUUID()
        val action = MissionNavActionEntityMock.create(id = UUID.randomUUID(), actionType = ActionType.INQUIRY)

        val  model = InquiryEntity(
            id = ownerId,
            startDateTimeUtc = Instant.now(),
            status = InquiryStatusType.NEW
        ).toInquiryModel()

        `when`(inquiryRepo.findById(id = ownerId)).thenReturn(Optional.of(model))
        `when`(getNavActionListByOwnerId.execute(ownerId = ownerId)).thenReturn(listOf(action))

        deleteInquiry.execute(id  = ownerId)
        verify(inquiryRepo, times(1)).deleteById(ownerId)
        verify(deleteNavAction, times(1)).execute(id  = action.id)

    }
}
