package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetInquiryByServiceId
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

@SpringBootTest(classes = [GetInquiryByServiceId::class])
@ContextConfiguration(classes = [GetInquiryByServiceId::class])
class GetInquiryByServiceIdTest {

    @MockitoBean
    private lateinit var inquiryRepo: IInquiryRepository

    @Autowired
    private lateinit var getInquiryByServiceId: GetInquiryByServiceId


    @Test
    fun `should return list of cross control of a specific service id`() {
        val id = UUID.randomUUID()
        val controlControls = listOf(
            InquiryModel(
                id = id,
                serviceId = 6,
                startDateTimeUtc = Instant.now(),
                status = InquiryStatusType.NEW.toString()
            ),
            InquiryModel(
                id = id,
                serviceId = 6,
                startDateTimeUtc = Instant.now(),
                status = InquiryStatusType.FOLLOW_UP.toString()
            ),
            InquiryModel(
                id = id,
                serviceId = 6,
                startDateTimeUtc = Instant.now(),
                status = InquiryStatusType.CLOSED.toString()
            )
        )

        //Mock
        `when`(inquiryRepo.findByServiceId(6)).thenReturn(controlControls)

        //When
        getInquiryByServiceId = GetInquiryByServiceId(inquiryRepo = inquiryRepo)
        val response = getInquiryByServiceId.execute(6)

        //Then
        assertThat(response).isNotNull()
        assertThat(response.size).isEqualTo(3)
    }
}
