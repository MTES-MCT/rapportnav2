package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryConclusionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryOriginType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryStatusType
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Inquiry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class InquiryTest {

    @Test
    fun `execute should convert into entity`() {
        val entity = InquiryEntity(
            id = UUID.randomUUID(),
            type = "",
            agentId = 5,
            vesselId = 4556,
            serviceId = 6,
            status = InquiryStatusType.NEW,
            origin = InquiryOriginType.FOLLOW_UP_CONTROL,
            conclusion = InquiryConclusionType.NO_FOLLOW_UP,
            endDateTimeUtc = Instant.parse("2015-07-30T00:00:00.00Z"),
            startDateTimeUtc = Instant.parse("2015-06-30T00:00:00.00Z"),
        )

        val response = Inquiry.fromInquiryEntity(entity)

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(entity.id)
        assertThat(response.type).isEqualTo(entity.type)
        assertThat(response.origin).isEqualTo(entity.origin)
        assertThat(response.agentId).isEqualTo(entity.agentId)
        assertThat(response.vesselId).isEqualTo(entity.vesselId)
        assertThat(response.serviceId).isEqualTo(entity.serviceId)
        assertThat(response.status).isEqualTo(entity.status)
        assertThat(response.endDateTimeUtc).isEqualTo(entity.endDateTimeUtc)
        assertThat(response.conclusion).isEqualTo(entity.conclusion)
        assertThat(response.startDateTimeUtc).isEqualTo(entity.startDateTimeUtc)
    }
}
