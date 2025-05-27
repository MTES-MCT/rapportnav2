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
        val crossControlEntity = CrossControlEntity(
            id = UUID.randomUUID(),
            type = "",
            agentId = "myAgentId",
            vesselId = 4556,
            sumNbrOfHours = 45,
            endDateTimeUtc = Instant.parse("2015-07-30T00:00:00.00Z"),
            startDateTimeUtc = Instant.parse("2015-06-30T00:00:00.00Z"),
            origin = CrossControlOriginType.FOLLOW_UP_CONTROL,
            status = CrossControlStatusType.NEW,
            conclusion = CrossControlConclusionType.NO_FOLLOW_UP,
        )
        val  response = MissionActionCrossControlEntity.fromNavActionEntityAndCrossControlEntity(
            actionEntity = actionEntity,
            crossControlEntity = crossControlEntity
        )

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(crossControlEntity.id)
        assertThat(response.type).isEqualTo(crossControlEntity.type)
        assertThat(response.origin).isEqualTo(crossControlEntity.origin)
        assertThat(response.agentId).isEqualTo(crossControlEntity.agentId)
        assertThat(response.vesselId).isEqualTo(crossControlEntity.vesselId)
        assertThat(response.status).isEqualTo(actionEntity.crossControl?.status)
        assertThat(response.nbrOfHours).isEqualTo(actionEntity.crossControl?.nbrOfHours)
        assertThat(response.endDateTimeUtc).isEqualTo(crossControlEntity.endDateTimeUtc)
        assertThat(response.conclusion).isEqualTo(actionEntity.crossControl?.conclusion)
        assertThat(response.startDateTimeUtc).isEqualTo(crossControlEntity.startDateTimeUtc)
        assertThat(response.isSignedByInspector).isEqualTo(actionEntity.crossControl?.isSignedByInspector)
    }

    @Test
    fun `execute should retrieve cross control entity from entity`() {
        val entity = MissionActionCrossControlEntity(
            id = UUID.randomUUID(),
            type = "",
            agentId = "",
            vesselId = 4556,
            endDateTimeUtc = Instant.parse("2015-07-30T00:00:00.00Z"),
            startDateTimeUtc = Instant.parse("2015-06-30T00:00:00.00Z"),
            origin = CrossControlOriginType.FOLLOW_UP_CONTROL,
            status = CrossControlStatusType.NEW,
            conclusion = CrossControlConclusionType.NO_FOLLOW_UP
        )

        val  response = entity.toCrossControlEntity()

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(response.id)
        assertThat(response.type).isEqualTo(response.type)
        assertThat(response.origin).isEqualTo(response.origin)
        assertThat(response.agentId).isEqualTo(response.agentId)
        assertThat(response.vesselId).isEqualTo(response.vesselId)
        assertThat(response.status).isEqualTo(response.status)
        assertThat(response.endDateTimeUtc).isEqualTo(response.endDateTimeUtc)
        assertThat(response.conclusion).isEqualTo(response.conclusion)
        assertThat(response.startDateTimeUtc).isEqualTo(response.startDateTimeUtc)
    }
}
