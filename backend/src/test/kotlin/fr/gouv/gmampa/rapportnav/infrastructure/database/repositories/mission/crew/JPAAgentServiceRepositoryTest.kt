package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentServiceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew.JPAAgentServiceRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPAAgentServiceRepository::class])
class JPAAgentServiceRepositoryTest {

    @MockBean
    private lateinit var dbServiceRepository: IDBAgentServiceRepository;


    private val agentServices: List<AgentServiceModel> = listOf(
        AgentServiceModel(
            id = 1,
            agent = AgentModel(id=1, firstName = "", lastName=""),
            serviceId = 1,
            role = AgentRoleModel(id=1, title = "")
        ),  AgentServiceModel(
            id = 2,
            agent = AgentModel(id=1, firstName = "", lastName=""),
            serviceId = 1,
            role = AgentRoleModel(id=1, title = "")
        )
    );


    @Test
    fun `execute should retrieve agents  service by service id`() {
        Mockito.`when`(dbServiceRepository.findByServiceId(1)).thenReturn(agentServices);
        var jpaAgentServiceRepo = JPAAgentServiceRepository(dbServiceRepository)
        val responses = jpaAgentServiceRepo.findByServiceId(1)
        assertThat(responses).isNotNull();

        assertThat(responses.size).isEqualTo(2);
        assertThat(responses.map { agent -> agent.id }).containsAll(listOf(1, 2));
    }
}
