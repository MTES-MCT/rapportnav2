package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.EnvActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvActionData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class MissionEnvActionDataTest {

    @Test
    fun `execute should retrieve output from mission action env Entity`() {
       val envAction = getEnvAction();
        val entity =  EnvActionEntity.fromEnvActionOutput(761, envAction)
        val output = MissionEnvAction.fromMissionActionEntity(entity)
        val entity2 = MissionEnvActionData.toMissionEnvActionEntity(output)
        assertThat(output).isNotNull()
        assertThat(output.id).isEqualTo(entity.id.toString())
        assertThat(output.data.startDateTimeUtc).isEqualTo(entity.startDateTimeUtc)
        assertThat(output.data.endDateTimeUtc).isEqualTo(entity.endDateTimeUtc)
        assertThat(output.data.observationsByUnit).isEqualTo(entity2.observationsByUnit)
        assertThat(output.actionType.toString()).isEqualTo(entity2.envActionType.toString())
    }


    private fun getEnvAction(): fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionEnvEntity {
        return ActionControlEnvEntity(
            UUID.randomUUID(),
            observations = "observations",
            observationsByUnit = "observationsByUnit",
            actionStartDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
            actionEndDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
        )
    }
}
