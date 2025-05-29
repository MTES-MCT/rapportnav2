package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionActionCrossControl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class MissionActionCrossControlTest {


    @Test
    fun `execute should convert into cross control entity`() {
        val startDate = Instant.now()
        val endDate = Instant.now()
        val crossControl = getMissionActionCrossControl()

        val response = crossControl.toCrossControlEntity(startDateTimeUtc = startDate, endDateTimeUtc = endDate)

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(crossControl.id)
        assertThat(response.type).isEqualTo(crossControl.type)
        assertThat(response.origin).isEqualTo(crossControl.origin)
        assertThat(response.status).isEqualTo(crossControl.status)
        assertThat(response.endDateTimeUtc).isEqualTo(endDate)
        assertThat(response.startDateTimeUtc).isEqualTo(startDate)
        assertThat(response.agentId).isEqualTo(crossControl.agentId)
        assertThat(response.vesselId).isEqualTo(crossControl.vesselId)
        assertThat(response.serviceId).isEqualTo(crossControl.serviceId)
        assertThat(response.conclusion).isEqualTo(crossControl.conclusion)
    }

    @Test
    fun `execute should convert into mission action cross control entity`() {
        val crossControl = getMissionActionCrossControl()
        val response = crossControl.toMissionActionCrossControlEntity()
        assertThat(response).isNotNull()
        assertThat(response.crossControlData).isNull()
        assertThat(response.id).isEqualTo(crossControl.id)
        assertThat(response.status).isEqualTo(crossControl.status)
        assertThat(response.conclusion).isEqualTo(crossControl.conclusion)
        assertThat(response.nbrOfHours).isEqualTo(crossControl.nbrOfHours)
        assertThat(response.isSignedByInspector).isEqualTo(crossControl.isSignedByInspector)
    }


    @Test
    fun `execute should get from mission action cross control entity`() {

        val entity = MissionActionCrossControlEntity(
            id = UUID.randomUUID(),
            crossControlData = CrossControlDataEntity(
                type = "my type",
                agentId = 5,
                vesselId = 4556,
                serviceId = 7,
                vesselName = "my vessel",
                isRefentialClosed = false,
                origin = CrossControlOriginType.FOLLOW_UP_CONTROL
            ),
            nbrOfHours = 5,
            isSignedByInspector = true,
            status = CrossControlStatusType.NEW,
            conclusion = CrossControlConclusionType.NO_FOLLOW_UP
        )
        val response = MissionActionCrossControl.fromCrossControlEntity(entity)

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(entity.id)
        assertThat(response.status).isEqualTo(entity.status)
        assertThat(response.nbrOfHours).isEqualTo(entity.nbrOfHours)
        assertThat(response.conclusion).isEqualTo(entity.conclusion)
        assertThat(response.type).isEqualTo(entity.crossControlData?.type)
        assertThat(response.origin).isEqualTo(entity.crossControlData?.origin)
        assertThat(response.agentId).isEqualTo(entity.crossControlData?.agentId)
        assertThat(response.vesselId).isEqualTo(entity.crossControlData?.vesselId)
        assertThat(response.serviceId).isEqualTo(entity.crossControlData?.serviceId)
        assertThat(response.isSignedByInspector).isEqualTo(entity.isSignedByInspector)
        assertThat(response.isRefentialClosed).isEqualTo(entity.crossControlData?.isRefentialClosed)
    }




    private fun getMissionActionCrossControl(): MissionActionCrossControl{
        return  MissionActionCrossControl(
            id = UUID.randomUUID(),
            agentId = 34,
            vesselId = 345,
            serviceId = 6,
            type = "My Type",
            nbrOfHours = 4,
            origin = CrossControlOriginType.FOLLOW_UP_CONTROL,
            status = CrossControlStatusType.FOLLOW_UP,
            conclusion = CrossControlConclusionType.WITH_REPORT,
            isSignedByInspector = true
        )
    }
}
