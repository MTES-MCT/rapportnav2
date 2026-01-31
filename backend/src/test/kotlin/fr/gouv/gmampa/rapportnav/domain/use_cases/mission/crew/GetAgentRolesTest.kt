package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentRoles
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetAgentRoles::class])
class GetAgentRolesTest {

    @Autowired
    private lateinit var getAgentRoles: GetAgentRoles

    @MockitoBean
    private lateinit var agentRoleRepository: IAgentRoleRepository

    @Test
    fun `should return all agent roles`() {
        val roles = listOf(
            AgentRoleModel(id = 1, title = "Commandant", priority = 1),
            AgentRoleModel(id = 2, title = "Second capitaine", priority = 2),
            AgentRoleModel(id = 3, title = "Mécanicien", priority = 10)
        )

        `when`(agentRoleRepository.findAll()).thenReturn(roles)

        val result = getAgentRoles.execute()

        assertThat(result).hasSize(3)
        assertThat(result[0].id).isEqualTo(1)
        assertThat(result[0].title).isEqualTo("Commandant")
        assertThat(result[1].id).isEqualTo(2)
        assertThat(result[2].id).isEqualTo(3)
    }

    @Test
    fun `should return empty list when no roles`() {
        `when`(agentRoleRepository.findAll()).thenReturn(emptyList())

        val result = getAgentRoles.execute()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val internalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Database error")
        )

        `when`(agentRoleRepository.findAll()).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            getAgentRoles.execute()
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }
}
