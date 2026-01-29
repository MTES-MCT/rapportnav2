package fr.gouv.gmampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.DeleteAgentRole
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.doNothing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [DeleteAgentRole::class])
@ContextConfiguration(classes = [DeleteAgentRole::class])
class DeleteAgentRoleTest {

    @Autowired
    private lateinit var deleteAgentRole: DeleteAgentRole

    @MockitoBean
    private lateinit var agentRoleRepository: IAgentRoleRepository

    @Test
    fun `should call repository deleteById`() {
        val roleId = 1

        doNothing().`when`(agentRoleRepository).deleteById(roleId)

        deleteAgentRole.execute(roleId)

        verify(agentRoleRepository).deleteById(roleId)
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val roleId = 1
        val exception = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Cause")
        )

        `when`(agentRoleRepository.deleteById(roleId)).thenAnswer { throw exception }

        val thrown = assertThrows<BackendInternalException> {
            deleteAgentRole.execute(roleId)
        }
        assertThat(thrown.message).isEqualTo("Repository error")
    }
}
