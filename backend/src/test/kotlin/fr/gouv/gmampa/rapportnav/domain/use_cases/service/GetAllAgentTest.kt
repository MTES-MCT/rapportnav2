package fr.gouv.gmampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetAllAgent
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetAllAgent::class])
@ContextConfiguration(classes = [GetAllAgent::class])
class GetAllAgentTest {

    @Autowired
    private lateinit var getAllAgent: GetAllAgent

    @MockitoBean
    private lateinit var agentRepository: IAgentRepository

    private fun createAgentModel(id: Int, firstName: String, lastName: String): AgentModel {
        return AgentModel(
            id = id,
            firstName = firstName,
            lastName = lastName,
            service = ServiceEntityMock.create(id = 1).toServiceModel()
        )
    }

    @Test
    fun `should return list of all agents`() {
        val agents = listOf(
            createAgentModel(1, "John", "Doe"),
            createAgentModel(2, "Jane", "Smith"),
            createAgentModel(3, "Bob", "Wilson")
        )

        `when`(agentRepository.findAll()).thenReturn(agents)

        val result = getAllAgent.execute()

        assertThat(result).hasSize(3)
        assertThat(result[0].firstName).isEqualTo("John")
        assertThat(result[1].firstName).isEqualTo("Jane")
        assertThat(result[2].firstName).isEqualTo("Bob")
    }

    @Test
    fun `should return empty list when no agents`() {
        `when`(agentRepository.findAll()).thenReturn(emptyList())

        val result = getAllAgent.execute()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val exception = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Cause")
        )

        `when`(agentRepository.findAll()).thenAnswer { throw exception }

        val thrown = assertThrows<BackendInternalException> {
            getAllAgent.execute()
        }
        assertThat(thrown.message).isEqualTo("Repository error")
    }
}
