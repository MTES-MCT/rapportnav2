package fr.gouv.gmampa.rapportnav.domain.entities.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.*
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionActionModelMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class MissionActionCrossControlEntityTest {

    @Test
    fun `execute should retrieve entity  from action and from cross control`() {
        val actionEntity = MissionNavActionEntity.fromMissionActionModel(MissionActionModelMock.create())
        val crossControl = CrossControlEntity(
            id = UUID.randomUUID(),
            type = "",
            agentId = 67,
            vesselId = 4556,
            sumNbrOfHours = 45,
            endDateTimeUtc = Instant.parse("2015-07-30T00:00:00.00Z"),
            startDateTimeUtc = Instant.parse("2015-06-30T00:00:00.00Z"),
            origin = CrossControlOriginType.FOLLOW_UP_CONTROL,
            status = CrossControlStatusType.NEW,
            conclusion = CrossControlConclusionType.NO_FOLLOW_UP,
        )
        val  response = actionEntity.crossControl?.fromCrossControlEntity(
            crossControl = crossControl
        )

        assertThat(response).isNotNull()
        assertThat(response?.id).isEqualTo(actionEntity.crossControl?.id)
        assertThat(response?.status).isEqualTo(actionEntity.crossControl?.status)
        assertThat(response?.nbrOfHours).isEqualTo(actionEntity.crossControl?.nbrOfHours)
        assertThat(response?.conclusion).isEqualTo(actionEntity.crossControl?.conclusion)
        assertThat(response?.crossControlData?.type).isEqualTo(crossControl.type)
        assertThat(response?.crossControlData?.origin).isEqualTo(crossControl.origin)
        assertThat(response?.crossControlData?.agentId).isEqualTo(crossControl.agentId)
        assertThat(response?.crossControlData?.vesselId).isEqualTo(crossControl.vesselId)
        assertThat(response?.isSignedByInspector).isEqualTo(actionEntity.crossControl?.isSignedByInspector)
    }
}
