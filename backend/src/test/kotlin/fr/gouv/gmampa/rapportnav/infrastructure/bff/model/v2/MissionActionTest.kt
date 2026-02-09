package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.FishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.NavActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.*
import fr.gouv.gmampa.rapportnav.mocks.mission.action.EnvActionControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionActionModelMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class MissionActionTest {

    @Test
    fun `execute should retrieve output from mission action fish from fishEntity`() {
        val fishAction = FishActionControlMock.create()
        val entity = FishActionEntity.fromFishAction(action = fishAction)
        val output = MissionAction.fromMissionActionEntity(entity)
        assertThat(output).isInstanceOf(MissionFishAction::class.java)
    }

    @Test
    fun `execute should retrieve output from mission action env from envEntity`() {
        val envAction = EnvActionControlMock.create();
        val entity =  EnvActionEntity.fromEnvActionOutput(761, envAction)
        val output = MissionAction.fromMissionActionEntity(entity)
        assertThat(output).isInstanceOf(MissionEnvAction::class.java)
    }

    @Test
    fun `execute should retrieve output from mission action nav from navEntity`() {
        val model = MissionActionModelMock.create()
        val entity = NavActionEntity.fromActionModel(model)
        val output = MissionAction.fromMissionActionEntity(entity)
        assertThat(output).isInstanceOf(MissionNavAction::class.java)
    }
}
