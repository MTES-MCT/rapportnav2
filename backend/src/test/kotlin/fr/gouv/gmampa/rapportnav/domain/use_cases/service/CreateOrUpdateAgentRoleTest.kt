package fr.gouv.gmampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.CreateOrUpdateAgentRole
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [CreateOrUpdateAgentRole::class])
@ContextConfiguration(classes = [CreateOrUpdateAgentRole::class])
class CreateOrUpdateAgentRoleTest {

    @Autowired
    private lateinit var createOrUpdateAgentRole: CreateOrUpdateAgentRole

    @MockitoBean
    private lateinit var agentRoleRepository: IAgentRoleRepository

    @Test
    fun `should create a new agent role`() {
        val entity = AgentRoleEntity(id = null, title = "Commandant", priority = 1)
        val savedModel = AgentRoleModel(id = 1, title = "Commandant", priority = 1)

        `when`(agentRoleRepository.save(any())).thenReturn(savedModel)

        val result = createOrUpdateAgentRole.execute(entity)

        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(1)
        assertThat(result.title).isEqualTo("Commandant")
        assertThat(result.priority).isEqualTo(1)
    }

    @Test
    fun `should update an existing agent role`() {
        val entity = AgentRoleEntity(id = 1, title = "Chef mécanicien", priority = 2)
        val savedModel = AgentRoleModel(id = 1, title = "Chef mécanicien", priority = 2)

        `when`(agentRoleRepository.save(any())).thenReturn(savedModel)

        val result = createOrUpdateAgentRole.execute(entity)

        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(1)
        assertThat(result.title).isEqualTo("Chef mécanicien")
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val entity = AgentRoleEntity(id = null, title = "Test Role")
        val exception = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Cause")
        )

        `when`(agentRoleRepository.save(any())).thenAnswer { throw exception }

        val thrown = assertThrows<BackendInternalException> {
            createOrUpdateAgentRole.execute(entity)
        }
        assertThat(thrown.message).isEqualTo("Repository error")
    }
}
