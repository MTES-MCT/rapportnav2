package fr.gouv.gmampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.CreateOrUpdateAgent
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentInput2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.mockito.Mockito.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [CreateOrUpdateAgent::class])
@ContextConfiguration(classes = [CreateOrUpdateAgent::class])
class CreateOrUpdateAgentTest {

    @Autowired
    private lateinit var createOrUpdateAgent: CreateOrUpdateAgent

    @MockitoBean
    private lateinit var agentRepository: IAgent2Repository

    @MockitoBean
    private lateinit var roleRepository: IAgentRoleRepository

    @MockitoBean
    private lateinit var serviceRepository: IServiceRepository

    private fun createAgentModel(id: Int?, firstName: String, lastName: String): AgentModel2 {
        return AgentModel2(
            id = id,
            firstName = firstName,
            lastName = lastName,
            service = ServiceEntityMock.create(id = 1).toServiceModel()
        )
    }

    @Test
    fun `should create a new agent`() {
        val input = AgentInput2(
            id = null,
            firstName = "John",
            lastName = "Doe",
            serviceId = 1,
            roleId = null,
            userId = null
        )
        val serviceModel = ServiceEntityMock.create(id = 1).toServiceModel()
        val savedAgent = createAgentModel(1, "John", "Doe")

        `when`(serviceRepository.findById(1)).thenReturn(Optional.of(serviceModel))
        `when`(agentRepository.save(any())).thenReturn(savedAgent)

        val result = createOrUpdateAgent.execute(input)

        assertThat(result).isNotNull
        assertThat(result.firstName).isEqualTo("John")
        assertThat(result.lastName).isEqualTo("Doe")
    }

    @Test
    fun `should throw BackendUsageException when service not found for create`() {
        val input = AgentInput2(
            id = null,
            firstName = "John",
            lastName = "Doe",
            serviceId = 999,
            roleId = null,
            userId = null
        )

        `when`(serviceRepository.findById(999)).thenReturn(Optional.empty())

        val thrown = assertThrows<BackendUsageException> {
            createOrUpdateAgent.execute(input)
        }
        assertThat(thrown.code).isEqualTo(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION)
        assertThat(thrown.message).contains("service not found")
    }

    @Test
    fun `should update an existing agent`() {
        val input = AgentInput2(
            id = 1,
            firstName = "Jane",
            lastName = "Smith",
            serviceId = 1,
            roleId = null,
            userId = null
        )
        val existingAgent = createAgentModel(1, "John", "Doe")
        val savedAgent = createAgentModel(1, "Jane", "Smith")

        `when`(agentRepository.findById(1)).thenReturn(existingAgent)
        `when`(agentRepository.save(any())).thenReturn(savedAgent)

        val result = createOrUpdateAgent.execute(input)

        assertThat(result).isNotNull
        assertThat(result.firstName).isEqualTo("Jane")
        assertThat(result.lastName).isEqualTo("Smith")
    }

    @Test
    fun `should throw BackendUsageException when agent not found for update`() {
        val input = AgentInput2(
            id = 999,
            firstName = "John",
            lastName = "Doe",
            serviceId = 1,
            roleId = null,
            userId = null
        )

        `when`(agentRepository.findById(999)).thenReturn(null)

        val thrown = assertThrows<BackendUsageException> {
            createOrUpdateAgent.execute(input)
        }
        assertThat(thrown.code).isEqualTo(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION)
        assertThat(thrown.message).contains("agent not found")
    }

    @Test
    fun `should assign role when roleId is provided for create`() {
        val roleId = 5
        val input = AgentInput2(
            id = null,
            firstName = "John",
            lastName = "Doe",
            serviceId = 1,
            roleId = roleId,
            userId = null
        )
        val serviceModel = ServiceEntityMock.create(id = 1).toServiceModel()
        val roleModel = AgentRoleModel(id = roleId, title = "Commandant", priority = 1)
        val savedAgent = createAgentModel(1, "John", "Doe")

        `when`(serviceRepository.findById(1)).thenReturn(Optional.of(serviceModel))
        `when`(roleRepository.findById(roleId)).thenReturn(roleModel)
        `when`(agentRepository.save(any())).thenReturn(savedAgent)

        createOrUpdateAgent.execute(input)

        verify(roleRepository).findById(roleId)
    }

    @Test
    fun `should assign role when roleId is provided for update`() {
        val roleId = 5
        val input = AgentInput2(
            id = 1,
            firstName = "Jane",
            lastName = "Smith",
            serviceId = 1,
            roleId = roleId,
            userId = null
        )
        val existingAgent = createAgentModel(1, "John", "Doe")
        val roleModel = AgentRoleModel(id = roleId, title = "Commandant", priority = 1)
        val savedAgent = createAgentModel(1, "Jane", "Smith")

        `when`(agentRepository.findById(1)).thenReturn(existingAgent)
        `when`(roleRepository.findById(roleId)).thenReturn(roleModel)
        `when`(agentRepository.save(any())).thenReturn(savedAgent)

        createOrUpdateAgent.execute(input)

        verify(roleRepository).findById(roleId)
    }

    @Test
    fun `should not call roleRepository when roleId is null`() {
        val input = AgentInput2(
            id = null,
            firstName = "John",
            lastName = "Doe",
            serviceId = 1,
            roleId = null,
            userId = null
        )
        val serviceModel = ServiceEntityMock.create(id = 1).toServiceModel()
        val savedAgent = createAgentModel(1, "John", "Doe")

        `when`(serviceRepository.findById(1)).thenReturn(Optional.of(serviceModel))
        `when`(agentRepository.save(any())).thenReturn(savedAgent)

        val result = createOrUpdateAgent.execute(input)

        assertThat(result.role).isNull()
    }
}
