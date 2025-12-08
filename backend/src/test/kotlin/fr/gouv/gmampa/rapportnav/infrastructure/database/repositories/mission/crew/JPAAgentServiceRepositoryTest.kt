package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentServiceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew.JPAAgentServiceRepository
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPAAgentServiceRepository::class])
class JPAAgentServiceRepositoryTest {

    @MockitoBean
    private lateinit var dbServiceRepository: IDBAgentServiceRepository


    private val agentServices: List<AgentServiceModel> = listOf(
        AgentServiceModel(
            id = 1,
            agent = AgentModel(id = 1, firstName = "", lastName = ""),
            service = ServiceEntityMock.create(id = 1, name = "service 1").toServiceModel(),
            role = AgentRoleModel(id = 1, title = "")
        ), AgentServiceModel(
            id = 2,
            agent = AgentModel(id = 1, firstName = "", lastName = ""),
            service = ServiceEntityMock.create(id = 1, name = "service 1").toServiceModel(),
            role = AgentRoleModel(id = 1, title = "")
        )
    )


    @Test
    fun `execute should retrieve agents  service by service id`() {
        Mockito.`when`(dbServiceRepository.findByServiceId(1)).thenReturn(agentServices)
        var jpaAgentServiceRepo = JPAAgentServiceRepository(dbServiceRepository)
        val responses = jpaAgentServiceRepo.findByServiceId(1)
        assertThat(responses).isNotNull()

        assertThat(responses.size).isEqualTo(2)
        assertThat(responses.map { agent -> agent.id }).containsAll(listOf(1, 2))
    }
}
