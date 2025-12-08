package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.crew.v2

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.CreateOrUpdateAgent2
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentInput2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*


@SpringBootTest(classes = [CreateOrUpdateAgent2::class])
@ContextConfiguration(classes = [CreateOrUpdateAgent2::class])
class CreateOrUpdateAgent2Test {

    @MockitoBean
    private lateinit var agentRepo: IAgent2Repository

    @MockitoBean
    private lateinit var roleRepo: IAgentRoleRepository

    @MockitoBean
    private lateinit var serviceRepo: IServiceRepository

    private lateinit var createOrUpdateAgent2: CreateOrUpdateAgent2

    val service = ServiceEntityMock.create(id = 1, name = "service 1").toServiceModel()
    val role = AgentRoleModel(id = 1, title = "", priority = 3)

    val input = AgentInput2(
        id = 1,
        userId = 2,
        roleId = 1,
        serviceId = 1,
        lastName = "lastName2",
        firstName = "firstName2",
    )

    val agent = AgentModel2(
        id = 1,
        userId = 2,
        role = role,
        service = service,
        lastName = "lastName1",
        firstName = "firstName1"
    )

    @Test
    fun `should create a new agent`() {
        input.id = null
        Mockito.`when`(roleRepo.findById(1)).thenReturn(role)
        Mockito.`when`(agentRepo.findById(1)).thenReturn(agent)
        Mockito.`when`(agentRepo.save(any())).thenReturn(agent)
        Mockito.`when`(serviceRepo.findById(1)).thenReturn(Optional.of(service))

        createOrUpdateAgent2 = CreateOrUpdateAgent2(
            roleRepo = roleRepo,
            agentRepo = agentRepo,
            serviceRepo = serviceRepo
        )

        val response = createOrUpdateAgent2.execute(input = input)
        assertThat(response).isNotNull()
    }

    @Test
    fun `should throw exception when service is null`() {
        input.id = null
        Mockito.`when`(roleRepo.findById(1)).thenReturn(role)
        Mockito.`when`(agentRepo.findById(1)).thenReturn(agent)
        Mockito.`when`(agentRepo.save(any())).thenReturn(agent)
        Mockito.`when`(serviceRepo.findById(1)).thenReturn(Optional.ofNullable(null))

        createOrUpdateAgent2 = CreateOrUpdateAgent2(
            roleRepo = roleRepo,
            agentRepo = agentRepo,
            serviceRepo = serviceRepo
        )

        val exception = assertThrows<Exception> {
            createOrUpdateAgent2.execute(input = input)
        }
        assertThat(exception.message).isEqualTo("Create Agent: Service not found")
    }
}
