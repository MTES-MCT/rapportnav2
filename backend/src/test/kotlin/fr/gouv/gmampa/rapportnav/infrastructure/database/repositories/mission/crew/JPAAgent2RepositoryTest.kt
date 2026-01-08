package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgent2Repository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew.JPAAgent2Repository
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.assertArg
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPAAgent2Repository::class])
class JPAAgent2RepositoryTest {
    @MockitoBean
    private lateinit var dbServiceRepository: IDBAgent2Repository

    private lateinit var jpaAgentSRepo: JPAAgent2Repository

    private val agents: List<AgentModel2> = listOf(
        AgentModel2(
            id = 1,
            lastName = "lastName1",
            firstName = "firstName1",
            role = AgentRoleModel(id = 1, title = ""),
            service = ServiceEntityMock.create(id = 1, name = "service 1").toServiceModel()
        ),
        AgentModel2(
            id = 2,
            lastName = "lastName2",
            firstName = "firstName2",
            disabledAt = Instant.now(),
            role = AgentRoleModel(id = 1, title = ""),
            service = ServiceEntityMock.create(id = 1, name = "service 1").toServiceModel()
        ),
        AgentModel2(
            id = 3,
            lastName = "lastName3",
            firstName = "firstName3",
            role = AgentRoleModel(id = 1, title = ""),
            service = ServiceEntityMock.create(id = 1, name = "service 1").toServiceModel()
        )
    )

    @Test
    fun `execute should retrieve agents service by service id`() {
        Mockito.`when`(dbServiceRepository.findByServiceId(1)).thenReturn(agents)
        jpaAgentSRepo = JPAAgent2Repository(dbServiceRepository)
        val responses = jpaAgentSRepo.findByServiceId(1)
        assertThat(responses).isNotNull()
        assertThat(responses.size).isEqualTo(2)
        assertThat(responses.map { agent -> agent.id }).containsAll(listOf(1, 3))
    }


    @Test
    fun `execute should get only active agents`() {
        Mockito.`when`(dbServiceRepository.findAll()).thenReturn(agents)
        jpaAgentSRepo = JPAAgent2Repository(dbServiceRepository)
        val responses = jpaAgentSRepo.findAllActive()
        assertThat(responses).isNotNull()
        assertThat(responses.size).isEqualTo(2)
        assertThat(responses.map { agent -> agent.id }).containsAll(listOf(1, 3))
    }

    @Test
    fun `execute should get every agents event disabled`() {
        Mockito.`when`(dbServiceRepository.findAll()).thenReturn(agents)
        jpaAgentSRepo = JPAAgent2Repository(dbServiceRepository)
        val responses = jpaAgentSRepo.findAll()
        assertThat(responses).isNotNull()
        assertThat(responses.size).isEqualTo(3)
        assertThat(responses.map { agent -> agent.id }).containsAll(listOf(1, 2, 3))
    }

    @Test
    fun `execute should disabled agent`() {
        Mockito.`when`(dbServiceRepository.findById(1)).thenReturn(Optional.of(agents[0]))
        jpaAgentSRepo = JPAAgent2Repository(dbServiceRepository)
        jpaAgentSRepo.disabledById(id = 1)

        // Verify with assertArg - cleaner and more readable
        verify(dbServiceRepository).save(assertArg { agent ->
            assertThat(agent.disabledAt).isNotNull()
        })
    }
}
