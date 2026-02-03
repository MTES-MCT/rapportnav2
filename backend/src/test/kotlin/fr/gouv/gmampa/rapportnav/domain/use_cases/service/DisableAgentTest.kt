package fr.gouv.gmampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.DisableAgent
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

@SpringBootTest(classes = [DisableAgent::class])
@ContextConfiguration(classes = [DisableAgent::class])
class DisableAgentTest {

    @Autowired
    private lateinit var disableAgent: DisableAgent

    @MockitoBean
    private lateinit var agentRepository: IAgent2Repository

    @Test
    fun `should call repository disabledById`() {
        val agentId = 1

        doNothing().`when`(agentRepository).disabledById(agentId)

        disableAgent.execute(agentId)

        verify(agentRepository).disabledById(agentId)
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val agentId = 1
        val exception = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Cause")
        )

        `when`(agentRepository.disabledById(agentId)).thenAnswer { throw exception }

        val thrown = assertThrows<BackendInternalException> {
            disableAgent.execute(agentId)
        }
        assertThat(thrown.message).isEqualTo("Repository error")
    }
}
