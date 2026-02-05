package fr.gouv.gmampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetActiveAgent
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

@SpringBootTest(classes = [GetActiveAgent::class])
@ContextConfiguration(classes = [GetActiveAgent::class])
class GetActiveAgentTest {

    @Autowired
    private lateinit var getActiveAgent: GetActiveAgent

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
    fun `should return list of active agents`() {
        val agents = listOf(
            createAgentModel(1, "John", "Doe"),
            createAgentModel(2, "Jane", "Smith")
        )

        `when`(agentRepository.findAllActive()).thenReturn(agents)

        val result = getActiveAgent.execute()

        assertThat(result).hasSize(2)
        assertThat(result[0].firstName).isEqualTo("John")
        assertThat(result[1].firstName).isEqualTo("Jane")
    }

    @Test
    fun `should return empty list when no active agents`() {
        `when`(agentRepository.findAllActive()).thenReturn(emptyList())

        val result = getActiveAgent.execute()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val exception = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Cause")
        )

        `when`(agentRepository.findAllActive()).thenAnswer { throw exception }

        val thrown = assertThrows<BackendInternalException> {
            getActiveAgent.execute()
        }
        assertThat(thrown.message).isEqualTo("Repository error")
    }
}
