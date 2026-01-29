package fr.gouv.gmampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.DeleteAgent
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

@SpringBootTest(classes = [DeleteAgent::class])
@ContextConfiguration(classes = [DeleteAgent::class])
class DeleteAgentTest {

    @Autowired
    private lateinit var deleteAgent: DeleteAgent

    @MockitoBean
    private lateinit var agentRepository: IAgent2Repository

    @Test
    fun `should call repository deleteById`() {
        val agentId = 1

        doNothing().`when`(agentRepository).deleteById(agentId)

        deleteAgent.execute(agentId)

        verify(agentRepository).deleteById(agentId)
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val agentId = 1
        val exception = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Cause")
        )

        `when`(agentRepository.deleteById(agentId)).thenAnswer { throw exception }

        val thrown = assertThrows<BackendInternalException> {
            deleteAgent.execute(agentId)
        }
        assertThat(thrown.message).isEqualTo("Repository error")
    }
}
