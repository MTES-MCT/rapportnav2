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
        val crossControlEntity = getInquiryEntity()
        val response = crossControlEntity.toInquiryModel()

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(crossControlEntity.id)
        assertThat(response.type).isEqualTo(crossControlEntity.type)
        assertThat(response.origin).isEqualTo(crossControlEntity.origin?.toString())
        assertThat(response.agentId).isEqualTo(crossControlEntity.agentId.toString())
        assertThat(response.vesselId).isEqualTo(crossControlEntity.vesselId)
        assertThat(response.status).isEqualTo(crossControlEntity.status.toString())
        assertThat(response.serviceId).isEqualTo(crossControlEntity.serviceId)
        assertThat(response.endDateTimeUtc).isEqualTo(crossControlEntity.endDateTimeUtc)
        assertThat(response.conclusion).isEqualTo(crossControlEntity.conclusion?.toString())
        assertThat(response.startDateTimeUtc).isEqualTo(crossControlEntity.startDateTimeUtc)
    }

    @Test
    fun `execute should transform to model with new data only`() {
        val crossControlEntity = getInquiryEntity()
        val inquiryEntityData = InquiryEntity(
            type = "myType",
            agentId = 445,
            vesselId = 654264525,
            serviceId = 5,
            startDateTimeUtc = Instant.parse("2015-09-30T00:00:00.00Z"),
            origin = InquiryOriginType.FOLLOW_UP_CONTROL,
        )

        val response = crossControlEntity.toModelSetData(entity = inquiryEntityData)

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(crossControlEntity.id)
        assertThat(response.type).isEqualTo(inquiryEntityData.type)
        assertThat(response.origin).isEqualTo(inquiryEntityData.origin?.toString())
        assertThat(response.agentId).isEqualTo(inquiryEntityData.agentId.toString())
        assertThat(response.vesselId).isEqualTo(inquiryEntityData.vesselId)
        assertThat(response.serviceId).isEqualTo(inquiryEntityData.serviceId)
        assertThat(response.status).isEqualTo(crossControlEntity.status.toString())
        assertThat(response.endDateTimeUtc).isEqualTo(crossControlEntity.endDateTimeUtc)
        assertThat(response.conclusion).isEqualTo(crossControlEntity.conclusion?.toString())
        assertThat(response.startDateTimeUtc).isEqualTo(inquiryEntityData.startDateTimeUtc)
        assertThat(response.missionId).isEqualTo(crossControlEntity.missionId)
        assertThat(response.missionIdUUID).isEqualTo(crossControlEntity.missionIdUUID)
    }

    @Test
    fun `execute should transform to model with new conclusion only`() {
        val crossControlEntity = getInquiryEntity()
        val inquiryEntityConclusion = InquiryEntity(
            status = InquiryStatusType.CLOSED,
            endDateTimeUtc = Instant.parse("2015-11-30T00:00:00.00Z"),
            conclusion = InquiryConclusionType.WITH_REPORT
        )

        val response = crossControlEntity.toModelSetConclusion(entity = inquiryEntityConclusion)

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(crossControlEntity.id)
        assertThat(response.type).isEqualTo(crossControlEntity.type)
        assertThat(response.origin).isEqualTo(crossControlEntity.origin?.toString())
        assertThat(response.agentId).isEqualTo(crossControlEntity.agentId.toString())
        assertThat(response.vesselId).isEqualTo(crossControlEntity.vesselId)
        assertThat(response.serviceId).isEqualTo(crossControlEntity.serviceId)
        assertThat(response.status).isEqualTo(inquiryEntityConclusion.status?.toString())
        assertThat(response.endDateTimeUtc).isEqualTo(inquiryEntityConclusion.endDateTimeUtc)
        assertThat(response.conclusion).isEqualTo(inquiryEntityConclusion.conclusion?.toString())
        assertThat(response.startDateTimeUtc).isEqualTo(crossControlEntity.startDateTimeUtc)
        assertThat(response.missionId).isEqualTo(crossControlEntity.missionId)
        assertThat(response.missionIdUUID).isEqualTo(crossControlEntity.missionIdUUID)
    }

    @Test
    fun `execute should add vessel attributes`() {
        val vessel = VesselEntity(
            vesselId = 345,
            vesselName = "myVesselNAme",
            externalReferenceNumber = "myExternalNumer"
        )
        val crossControlEntity = getInquiryEntity()
        crossControlEntity.withVessel(vessel = vessel)
        assertThat(crossControlEntity.vesselName).isEqualTo(vessel.vesselName)
        assertThat(crossControlEntity.vesselExternalReferenceNumber).isEqualTo(vessel.externalReferenceNumber)
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
            missionIdUUID = UUID.randomUUID()
        )
    }
}
