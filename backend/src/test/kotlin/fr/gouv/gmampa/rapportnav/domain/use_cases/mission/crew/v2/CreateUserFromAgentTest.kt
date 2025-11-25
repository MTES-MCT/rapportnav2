package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.crew.v2

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.CreateUserFromAgent
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.CreateUser
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.AuthRegisterDataInput
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean


@SpringBootTest(classes = [CreateUserFromAgent::class])
@ContextConfiguration(classes = [CreateUserFromAgent::class])
class CreateUserFromAgentTest {

    @MockitoBean
    private lateinit var agentRepo: IAgent2Repository

    @MockitoBean
    private lateinit var createUser: CreateUser

    @Captor
    lateinit var disabledCaptor: ArgumentCaptor<Int>

    private lateinit var createUserFromAgent: CreateUserFromAgent

    val input =  AuthRegisterDataInput(
        id = null,
        email = "user@example.com",
        password = "StrongPassword1!!!",
        firstName = "John",
        lastName = "Doe",
        serviceId = null,
        roles = null
    )
    val user = UserMock.create()
    val agent = AgentModel2(
        id = 1,
        role = AgentRoleModel(id = 1, title = "", priority = 3),
        service = ServiceModel(id = 1, name = "service 1"),
        lastName = "lastName1",
        firstName = "firstName1"
    )

    @Test
    fun `should throw exception when agent is null  service is null`() {
        Mockito.`when`(agentRepo.findById(1)).thenReturn(null)
        createUserFromAgent = CreateUserFromAgent(
            agentRepo = agentRepo,
            createUser = createUser
        )

        val exception = assertThrows<Exception> {
            createUserFromAgent.execute(agentId = 1, input)
        }
        assertThat(exception.message).isEqualTo("Unknown agent or user already exists")
    }

    @Test
    fun `should throw exception when agent already has userId`() {
        agent.userId = 2
        Mockito.`when`(agentRepo.findById(1)).thenReturn(agent)
        createUserFromAgent = CreateUserFromAgent(
            agentRepo = agentRepo,
            createUser = createUser
        )

        val exception = assertThrows<Exception> {
            createUserFromAgent.execute(agentId = 1,input)
        }
        assertThat(exception.message).isEqualTo("Unknown agent or user already exists")
    }

    @Test
    fun `should execute disabled of an agent`() {
        Mockito.`when`(agentRepo.findById(1)).thenReturn(agent)
        Mockito.`when`(agentRepo.save(any())).thenReturn(agent)
        Mockito.`when`(createUser.execute(any())).thenReturn(user)

        createUserFromAgent = CreateUserFromAgent(
            agentRepo = agentRepo,
            createUser = createUser
        )

        val response = createUserFromAgent.execute(agentId = 1, input)

        assertThat(response).isNotNull
        assertThat(response?.userId).isEqualTo(user.id)
    }
}
