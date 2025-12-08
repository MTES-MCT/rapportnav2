package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.crew.v2

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.CreateOrUpdateAgent2
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.MigrateAgent
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentInput2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*


@SpringBootTest(classes = [MigrateAgent::class])
@ContextConfiguration(classes = [MigrateAgent::class])
class MigrateAgentTest {

    @MockitoBean
    private lateinit var agentRepo: IAgent2Repository

    @MockitoBean
    private lateinit var createOrUpdateAgent2: CreateOrUpdateAgent2

    @MockitoBean
    private lateinit var serviceRepo: IServiceRepository

    @Captor
    lateinit var disabledCaptor: ArgumentCaptor<Int>

    private lateinit var migrateAgent: MigrateAgent

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
    fun `should throw exception when agent or service is null`() {
        Mockito.`when`(agentRepo.findById(1)).thenReturn(agent)
        Mockito.`when`(serviceRepo.findById(1)).thenReturn(Optional.ofNullable(null))

        migrateAgent = MigrateAgent(
            agentRepo = agentRepo,
            serviceRepo = serviceRepo,
            createOrUpdateAgent2 = createOrUpdateAgent2
        )

        val exception = assertThrows<Exception> {
            migrateAgent.execute(input = input)
        }
        assertThat(exception.message).isEqualTo("Update Service: Service or agent not found")
    }

    @Test
    fun `should throw exception when input id is null`() {
        input.id = null
        Mockito.`when`(agentRepo.findById(1)).thenReturn(agent)
        Mockito.`when`(agentRepo.save(any())).thenReturn(agent)
        Mockito.`when`(serviceRepo.findById(1)).thenReturn(Optional.ofNullable(null))

        migrateAgent = MigrateAgent(
            agentRepo = agentRepo,
            serviceRepo = serviceRepo,
            createOrUpdateAgent2 = createOrUpdateAgent2
        )

        val exception = assertThrows<Exception> {
            migrateAgent.execute(input = input)
        }
        assertThat(exception.message).isEqualTo("Update Service: at least on id is null")
    }

    @Test
    fun `should execute disabled of an agent`() {
        Mockito.`when`(agentRepo.findById(1)).thenReturn(agent)
        Mockito.`when`(agentRepo.save(any())).thenReturn(agent)
        Mockito.`when`(serviceRepo.findById(1)).thenReturn(Optional.ofNullable(service))

        migrateAgent = MigrateAgent(
            agentRepo = agentRepo,
            serviceRepo = serviceRepo,
            createOrUpdateAgent2 = createOrUpdateAgent2
        )

        migrateAgent.execute(input = input)
        verify(agentRepo).disabledById(disabledCaptor.capture())

        assertThat(disabledCaptor.value).isEqualTo(agent.id)
    }
}
