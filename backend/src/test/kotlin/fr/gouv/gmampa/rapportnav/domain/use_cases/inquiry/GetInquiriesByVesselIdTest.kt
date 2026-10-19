package fr.gouv.gmampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.GetInquiriesByVesselId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetNavActionListByOwnerId
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.InquiryModel
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetModelMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.argumentCaptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.time.ZoneOffset
import java.util.*

@SpringBootTest(classes = [GetInquiriesByVesselId::class])
@ContextConfiguration(classes = [GetInquiriesByVesselId::class])
class GetInquiriesByVesselIdTest {

    @MockitoBean
    private lateinit var inquiryRepo: IInquiryRepository

    @MockitoBean
    private lateinit var getNavActionListByOwnerId: GetNavActionListByOwnerId

    @MockitoBean
    private lateinit var targetRepo: ITargetRepository

    @Autowired
    private lateinit var getInquiriesByVesselId: GetInquiriesByVesselId

    @Test
    fun `should return inquiries of the vessel from the given start date`() {
        val startDateTimeUtc = Instant.parse("2022-01-01T00:00:00Z")
        val id = UUID.randomUUID()
        `when`(inquiryRepo.findByVesselId(vesselId = 42, startDateTimeUtc = startDateTimeUtc)).thenReturn(
            listOf(
                InquiryModel(
                    id = id,
                    vesselId = 42,
                    serviceId = 6,
                    startDateTimeUtc = Instant.parse("2024-03-01T10:00:00Z"),
                    status = InquiryStatusType.IN_PROGRESS.toString()
                )
            )
        )

        val result = getInquiriesByVesselId.execute(vesselId = 42, startDateTimeUtc = startDateTimeUtc)

        assertThat(result).hasSize(1)
        assertThat(result.first().id).isEqualTo(id)
        assertThat(result.first().vessel?.vesselId).isEqualTo(42)
        assertThat(result.first().serviceId).isEqualTo(6)
        assertThat(result.first().status).isEqualTo(InquiryStatusType.IN_PROGRESS)
    }

    @Test
    fun `should attach the inquiry actions with their targets read from the repository`() {
        val id = UUID.randomUUID()
        val actionId = UUID.randomUUID()
        `when`(inquiryRepo.findByVesselId(eq(42), any())).thenReturn(
            listOf(InquiryModel(id = id, vesselId = 42, startDateTimeUtc = Instant.parse("2024-03-01T10:00:00Z")))
        )
        `when`(getNavActionListByOwnerId.execute(ownerId = id)).thenReturn(
            listOf(
                MissionNavActionEntityMock.create(
                    id = actionId,
                    ownerId = id,
                    actionType = ActionType.INQUIRY,
                    observations = "Débarquement contrôlé"
                )
            )
        )
        `when`(targetRepo.findByActionId(actionId.toString())).thenReturn(
            listOf(TargetModelMock.create(actionId = actionId.toString()))
        )

        val result = getInquiriesByVesselId.execute(vesselId = 42)

        val action = result.first().actions?.first()
        assertThat(action?.targets).hasSize(1)
        verify(targetRepo).findByActionId(actionId.toString())
    }

    @Test
    fun `should default to the start of the year 5 years ago when no start date is given`() {
        `when`(inquiryRepo.findByVesselId(eq(42), any())).thenReturn(emptyList())

        val result = getInquiriesByVesselId.execute(vesselId = 42)

        assertThat(result).isEmpty()
        val captor = argumentCaptor<Instant>()
        verify(inquiryRepo).findByVesselId(eq(42), captor.capture())
        val expectedYear = Instant.now().atZone(ZoneOffset.UTC).year - 5
        assertThat(captor.firstValue).isEqualTo(Instant.parse("$expectedYear-01-01T00:00:00Z"))
    }

    @Test
    fun `default start date should be the first day of the year 5 years before now`() {
        val result = getInquiriesByVesselId.defaultStartDateTimeUtc(now = Instant.parse("2026-10-13T15:32:00Z"))

        assertThat(result).isEqualTo(Instant.parse("2021-01-01T00:00:00Z"))
    }
}
