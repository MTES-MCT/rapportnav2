package fr.gouv.gmampa.rapportnav.domain.entities.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class CrossControlEntityTest {

    @Test
    fun `execute should get cross control model from entity`() {
        val crossControlEntity = getCrossControlEntity()
        val response = crossControlEntity.toCrossControlModel()

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
        val crossControlEntity = getCrossControlEntity()
        val crossControlEntityData = CrossControlEntity(
            type = "myType",
            agentId = 445,
            vesselId = 654264525,
            serviceId = 5,
            startDateTimeUtc = Instant.parse("2015-09-30T00:00:00.00Z"),
            origin = CrossControlOriginType.FOLLOW_UP_CONTROL,
        )

        val response = crossControlEntity.toModelSetData(entity = crossControlEntityData)

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(crossControlEntity.id)
        assertThat(response.type).isEqualTo(crossControlEntityData.type)
        assertThat(response.origin).isEqualTo(crossControlEntityData.origin?.toString())
        assertThat(response.agentId).isEqualTo(crossControlEntityData.agentId.toString())
        assertThat(response.vesselId).isEqualTo(crossControlEntityData.vesselId)
        assertThat(response.serviceId).isEqualTo(crossControlEntityData.serviceId)
        assertThat(response.status).isEqualTo(crossControlEntity.status.toString())
        assertThat(response.endDateTimeUtc).isEqualTo(crossControlEntity.endDateTimeUtc)
        assertThat(response.conclusion).isEqualTo(crossControlEntity.conclusion?.toString())
        assertThat(response.startDateTimeUtc).isEqualTo(crossControlEntityData.startDateTimeUtc)
    }

    @Test
    fun `execute should transform to model with new conclusion only`() {
        val crossControlEntity = getCrossControlEntity()
        val crossControlEntityConclusion = CrossControlEntity(
            status = CrossControlStatusType.CLOSED,
            endDateTimeUtc = Instant.parse("2015-11-30T00:00:00.00Z"),
            conclusion = CrossControlConclusionType.WITH_REPORT
        )

        val response = crossControlEntity.toModelSetConclusion(entity = crossControlEntityConclusion)

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(crossControlEntity.id)
        assertThat(response.type).isEqualTo(crossControlEntity.type)
        assertThat(response.origin).isEqualTo(crossControlEntity.origin?.toString())
        assertThat(response.agentId).isEqualTo(crossControlEntity.agentId.toString())
        assertThat(response.vesselId).isEqualTo(crossControlEntity.vesselId)
        assertThat(response.serviceId).isEqualTo(crossControlEntity.serviceId)
        assertThat(response.status).isEqualTo(crossControlEntityConclusion.status?.toString())
        assertThat(response.endDateTimeUtc).isEqualTo(crossControlEntityConclusion.endDateTimeUtc)
        assertThat(response.conclusion).isEqualTo(crossControlEntityConclusion.conclusion?.toString())
        assertThat(response.startDateTimeUtc).isEqualTo(crossControlEntity.startDateTimeUtc)
    }

    @Test
    fun `execute should add vessel attributes`() {
        val vessel = VesselEntity(
            vesselId = 345,
            vesselName = "myVesselNAme",
            externalReferenceNumber = "myExternalNumer"
        )
        val crossControlEntity = getCrossControlEntity()
        crossControlEntity.withVessel(vessel = vessel)
        assertThat(crossControlEntity.vesselName).isEqualTo(vessel.vesselName)
        assertThat(crossControlEntity.vesselExternalReferenceNumber).isEqualTo(vessel.externalReferenceNumber)
    }


    private fun getCrossControlEntity(): CrossControlEntity {
        return CrossControlEntity(
            id = UUID.randomUUID(),
            type = "",
            agentId = 5,
            vesselId = 4556,
            sumNbrOfHours = 45,
            serviceId = 6,
            endDateTimeUtc = Instant.parse("2015-07-30T00:00:00.00Z"),
            startDateTimeUtc = Instant.parse("2015-06-30T00:00:00.00Z"),
            origin = CrossControlOriginType.FOLLOW_UP_CONTROL,
            status = CrossControlStatusType.NEW,
            conclusion = CrossControlConclusionType.NO_FOLLOW_UP
        )
    }
}
