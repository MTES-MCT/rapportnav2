package fr.gouv.gmampa.rapportnav.infrastructure.bff.model

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionActionOutput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvActionOutput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionFishActionOutput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavActionOutput
import fr.gouv.gmampa.rapportnav.mocks.mission.action.EnvActionControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionActionModelMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class MissionActionOutputTest {

    @Test
    fun `execute should retrieve output from mission action fish from fishEntity`() {
        val fishAction = FishActionControlMock.create()
        val entity = MissionFishActionEntity.fromFishAction(action = fishAction)
        val output = MissionActionOutput.fromMissionActionEntity(entity)
        assertThat(output).isInstanceOf(MissionFishActionOutput::class.java)
    }

    @Test
    fun `execute should retrieve output from mission action env from envEntity`() {
        val envAction = EnvActionControlMock.create();
        val entity =  MissionEnvActionEntity.fromEnvAction(761, envAction)
        val output = MissionActionOutput.fromMissionActionEntity(entity)
        assertThat(output).isInstanceOf(MissionEnvActionOutput::class.java)
    }

    @Test
    fun `execute should retrieve output from mission action nav from navEntity`() {
        val model = MissionActionModelMock.create()
        val entity = MissionNavActionEntity.fromMissionActionModel(model)
        val output = MissionActionOutput.fromMissionActionEntity(entity)
        assertThat(output).isInstanceOf(MissionNavActionOutput::class.java)
    }
}
