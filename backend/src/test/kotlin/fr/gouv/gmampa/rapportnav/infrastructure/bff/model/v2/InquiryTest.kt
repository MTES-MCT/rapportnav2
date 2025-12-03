package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Establishment
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Inquiry
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Vessel
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class InquiryTest {

    @Test
    fun `execute should convert from entity`() {
        val entity = InquiryEntity(
            id = UUID.randomUUID(),
            type = "",
            agentId = 5,
            vessel = VesselEntity(vesselId = 4556),
            serviceId = 6,
            status = InquiryStatusType.IN_PROGRESS,
            origin = InquiryOriginType.FOLLOW_UP_CONTROL,
            conclusion = InquiryConclusionType.NO_FOLLOW_UP,
            endDateTimeUtc = Instant.parse("2015-07-30T00:00:00.00Z"),
            startDateTimeUtc = Instant.parse("2015-06-30T00:00:00.00Z"),
            isSignedByInspector = true,
            establishment = EstablishmentEntity(id = 2, name = "myName", siren = "mySiren", isForeign = true)
        )

        val response = Inquiry.fromInquiryEntity(entity)

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(entity.id)
        assertThat(response.type).isEqualTo(entity.type)
        assertThat(response.origin).isEqualTo(entity.origin)
        assertThat(response.agentId).isEqualTo(entity.agentId)
        assertThat(response.vessel?.vesselId).isEqualTo(entity.vessel?.vesselId)
        assertThat(response.serviceId).isEqualTo(entity.serviceId)
        assertThat(response.status).isEqualTo(entity.status)
        assertThat(response.endDateTimeUtc).isEqualTo(entity.endDateTimeUtc)
        assertThat(response.conclusion).isEqualTo(entity.conclusion)
        assertThat(response.startDateTimeUtc).isEqualTo(entity.startDateTimeUtc)
        assertThat(response.isSignedByInspector).isEqualTo(entity.isSignedByInspector)
        assertThat(response.establishment?.siren).isEqualTo(entity.establishment?.siren)
        assertThat(response.establishment?.isForeign).isEqualTo(entity.establishment?.isForeign)
    }

    @Test
    fun `execute should convert into entity`() {
        val action = MissionNavAction.fromMissionActionEntity(MissionNavActionEntityMock.create())
        val inquiry = Inquiry(
            id = UUID.randomUUID(),
            type = "",
            agentId = 5,
            vessel = Vessel(vesselId = 4556),
            serviceId = 6,
            status = InquiryStatusType.IN_PROGRESS,
            origin = InquiryOriginType.FOLLOW_UP_CONTROL,
            conclusion = InquiryConclusionType.NO_FOLLOW_UP,
            endDateTimeUtc = Instant.parse("2015-07-30T00:00:00.00Z"),
            startDateTimeUtc = Instant.parse("2015-06-30T00:00:00.00Z"),
            actions = listOf(action),
            isSignedByInspector = false,
            establishment = Establishment(id = 2, name = "myName", siren = "mySiren", isForeign = true)
        )

        val response = inquiry.toInquiryEntity()
        assertThat(response).isNotNull()
        assertThat(response.actions).isEmpty()
        assertThat(response.id).isEqualTo(inquiry.id)
        assertThat(response.type).isEqualTo(inquiry.type)
        assertThat(response.origin).isEqualTo(inquiry.origin)
        assertThat(response.agentId).isEqualTo(inquiry.agentId)
        assertThat(response.vessel?.vesselId).isEqualTo(inquiry.vessel?.vesselId)
        assertThat(response.serviceId).isEqualTo(inquiry.serviceId)
        assertThat(response.status).isEqualTo(inquiry.status)
        assertThat(response.endDateTimeUtc).isEqualTo(inquiry.endDateTimeUtc)
        assertThat(response.conclusion).isEqualTo(inquiry.conclusion)
        assertThat(response.startDateTimeUtc).isEqualTo(inquiry.startDateTimeUtc)
        assertThat(response.isSignedByInspector).isEqualTo(inquiry.isSignedByInspector)
        assertThat(response.establishment?.siren).isEqualTo(inquiry.establishment?.siren)
        assertThat(response.establishment?.isForeign).isEqualTo(inquiry.establishment?.isForeign)
    }
}
