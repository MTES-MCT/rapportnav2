package fr.gouv.gmampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.CreateOrUpdateAgent
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.MigrateAgent
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentInput2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [MigrateAgent::class])
@ContextConfiguration(classes = [MigrateAgent::class])
class MigrateAgentTest {

    @Autowired
    private lateinit var migrateAgent: MigrateAgent

    @MockitoBean
    private lateinit var agentRepository: IAgentRepository

    @MockitoBean
    private lateinit var serviceRepository: IServiceRepository

    @MockitoBean
    private lateinit var createOrUpdateAgent: CreateOrUpdateAgent

    private fun createAgentModel(id: Int, firstName: String, lastName: String): AgentModel {
        return AgentModel(
            id = id,
            firstName = firstName,
            lastName = lastName,
            service = ServiceEntityMock.create(id = 1).toServiceModel()
        )
    }

    @Test
    fun `should throw BackendUsageException when agent id is null`() {
        val input = AgentInput2(
            id = null,
            firstName = "John",
            lastName = "Doe",
            serviceId = 2,
            roleId = null,
            userId = null
        )

        val thrown = assertThrows<BackendUsageException> {
            migrateAgent.execute(input)
        }
        assertThat(thrown.code).isEqualTo(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION)
        assertThat(thrown.message).contains("agent id is required")
    }

    @Test
    fun `should throw BackendUsageException when agent not found`() {
        val input = AgentInput2(
            id = 999,
            firstName = "John",
            lastName = "Doe",
            serviceId = 2,
            roleId = null,
            userId = null
        )

        `when`(agentRepository.findById(999)).thenReturn(null)

        val thrown = assertThrows<BackendUsageException> {
            migrateAgent.execute(input)
        }
        assertThat(thrown.code).isEqualTo(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION)
        assertThat(thrown.message).contains("agent not found")
    }

    @Test
    fun `should throw BackendUsageException when service not found`() {
        val input = AgentInput2(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            serviceId = 999,
            roleId = null,
            userId = null
        )

        val existingAgent = createAgentModel(1, "John", "Doe")
        `when`(agentRepository.findById(1)).thenReturn(existingAgent)
        `when`(serviceRepository.findById(999)).thenReturn(Optional.empty())

        val thrown = assertThrows<BackendUsageException> {
            migrateAgent.execute(input)
        }
        assertThat(thrown.code).isEqualTo(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION)
        assertThat(thrown.message).contains("service not found")
    }

    @Test
    fun `should migrate agent to new service`() {
        val input = AgentInput2(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            serviceId = 2,
            roleId = null,
            userId = null
        )

        val existingAgent = createAgentModel(1, "John", "Doe")
        val newService = ServiceEntityMock.create(id = 2).toServiceModel()
        val migratedAgentModel = AgentModel(
            id = 2,
            firstName = "John",
            lastName = "Doe",
            service = newService
        )
        val migratedEntity = AgentEntity.fromAgentModel(migratedAgentModel)

        `when`(agentRepository.findById(1)).thenReturn(existingAgent)
        `when`(serviceRepository.findById(2)).thenReturn(Optional.of(newService))
        doNothing().`when`(agentRepository).disabledById(1)
        `when`(createOrUpdateAgent.execute(any())).thenReturn(migratedEntity)

        val result = migrateAgent.execute(input)

        verify(agentRepository).disabledById(1)
        assertThat(result).isNotNull
        assertThat(result.firstName).isEqualTo("John")
        assertThat(result.lastName).isEqualTo("Doe")
    }
}
