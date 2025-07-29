package fr.gouv.gmampa.rapportnav.domain.entities.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class InquiryEntityTest {

    @Test
    fun `execute should get cross control model from entity`() {
        val inquiryEntity = getInquiryEntity()
        val response = inquiryEntity.toInquiryModel()

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(inquiryEntity.id)
        assertThat(response.type).isEqualTo(inquiryEntity.type)
        assertThat(response.origin).isEqualTo(inquiryEntity.origin?.toString())
        assertThat(response.agentId).isEqualTo(inquiryEntity.agentId.toString())
        assertThat(response.vesselId).isEqualTo(inquiryEntity.vesselId)
        assertThat(response.status).isEqualTo(inquiryEntity.status.toString())
        assertThat(response.serviceId).isEqualTo(inquiryEntity.serviceId)
        assertThat(response.endDateTimeUtc).isEqualTo(inquiryEntity.endDateTimeUtc)
        assertThat(response.conclusion).isEqualTo(inquiryEntity.conclusion?.toString())
        assertThat(response.startDateTimeUtc).isEqualTo(inquiryEntity.startDateTimeUtc)
        assertThat(response.isSignedByInspector).isEqualTo(inquiryEntity.isSignedByInspector)
    }

    @Test
    fun `execute should add vessel attributes`() {
        val vessel = VesselEntity(
            vesselId = 345,
            vesselName = "myVesselNAme",
            externalReferenceNumber = "myExternalNumer"
        )
        val inquiryEntity = getInquiryEntity()
        inquiryEntity.withVessel(vessel = vessel)
        assertThat(inquiryEntity.vesselName).isEqualTo(vessel.vesselName)
        assertThat(inquiryEntity.vesselExternalReferenceNumber).isEqualTo(vessel.externalReferenceNumber)
    }

    private fun getInquiryEntity(): InquiryEntity {
        return InquiryEntity(
            id = UUID.randomUUID(),
            type = "",
            agentId = 5,
            serviceId = 6,
            vesselId = 4556,
            endDateTimeUtc = Instant.parse("2015-07-30T00:00:00.00Z"),
            startDateTimeUtc = Instant.parse("2015-06-30T00:00:00.00Z"),
            origin = InquiryOriginType.FOLLOW_UP_CONTROL,
            status = InquiryStatusType.NEW,
            conclusion = InquiryConclusionType.NO_FOLLOW_UP,
            missionId = 4,
            missionIdUUID = UUID.randomUUID(),
            isSignedByInspector = true
        )
    }
}
