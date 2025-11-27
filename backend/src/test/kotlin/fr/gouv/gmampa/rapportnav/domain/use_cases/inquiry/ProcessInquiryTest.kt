package fr.gouv.gmampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.ProcessInquiry
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetVessels
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [ProcessInquiry::class])
@ContextConfiguration(classes = [ProcessInquiry::class])
class ProcessInquiryTest {
    @Autowired
    private lateinit var processInquiry: ProcessInquiry

    @MockitoBean
    private lateinit var getVessels: GetVessels

    @MockitoBean
    private lateinit var  getComputeNavActionListByMissionId: GetComputeNavActionListByMissionId

    @Test
    fun `test execute get fish action by id`() {
        val vessels = listOf(
            VesselEntity(vesselId = 4556),
            VesselEntity(vesselId = 34556)
        )
        val entity = InquiryEntity(
            id = UUID.randomUUID(),
            type = "",
            agentId = 5,
            vessel = VesselEntity(vesselId = 4556),
            serviceId = 6,
            endDateTimeUtc = Instant.parse("2015-07-30T00:00:00.00Z"),
            startDateTimeUtc = Instant.parse("2015-06-30T00:00:00.00Z"),
            origin = InquiryOriginType.FOLLOW_UP_CONTROL,
            status = InquiryStatusType.IN_PROGRESS,
            conclusion = InquiryConclusionType.NO_FOLLOW_UP
        )

        `when`(getVessels.execute()).thenReturn(vessels)
        processInquiry = ProcessInquiry(
            getVessels = getVessels,
            getComputeNavActionListByMissionId = getComputeNavActionListByMissionId
        )
        val response = processInquiry.execute(entity)

        assertThat(entity).isNotNull
        assertThat(entity.id).isEqualTo(response.id)
        assertThat(entity.vessel?.vesselId).isEqualTo(4556)
    }
}
