package fr.gouv.gmampa.rapportnav.infrastructure.database.model.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentServiceModel
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [AgentServiceEntity::class])
class AgentServiceEntityTest {

    @Test
    fun `execute should retrieve mission crew model from agent service`() {
        val missionId = 761;
        val role = AgentRoleModel(id=1, title = "Commandant")
        val agent = AgentModel(id=1, firstName = "firstname", lastName="lastname");

        val agentServiceModel = AgentServiceModel(
            id = 1,
            agent = agent,
            serviceId = 1,
            role = role
        );

        val response = AgentServiceEntity.fromAgentServiceModel(agentServiceModel);
        assertThat(response).isNotNull();
        assertThat(response.id).isEqualTo(missionId);
        assertThat(response.agent).isEqualTo(agent);
        assertThat(response.role).isEqualTo(role);
    }
}
