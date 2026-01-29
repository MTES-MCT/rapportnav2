package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.crew.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetCrewByServiceId
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant


@SpringBootTest(classes = [GetCrewByServiceId::class])
@ContextConfiguration(classes = [GetCrewByServiceId::class])
class GetCrewByServiceIdTest {

    @MockitoBean
    lateinit var agentRepo: IAgent2Repository

    @Test
    fun `should get crew in priority order`() {
         val agents: List<AgentModel2> = listOf(
            AgentModel2(
                id = 1,
                lastName = "lastName1",
                firstName = "firstName1",
                role = AgentRoleModel(id = 1, title = "", priority = 3),
                service = ServiceEntityMock.create(id = 1, name = "service 1").toServiceModel()
            ), AgentModel2(
                id = 2,
                lastName = "lastName2",
                firstName = "firstName2",
                disabledAt = Instant.now(),
                role = AgentRoleModel(id = 1, title = "", priority = 2),
                service = ServiceEntityMock.create(id = 1, name = "service 1").toServiceModel()
            ),
            AgentModel2(
                id = 3,
                lastName = "lastName3",
                firstName = "firstName3",
                role = AgentRoleModel(id = 1, title = "", priority = 1),
                service = ServiceEntityMock.create(id = 1, name = "service 1").toServiceModel()
            )
        )
        Mockito.`when`(agentRepo.findByServiceId(1)).thenReturn(agents)
        val responses = GetCrewByServiceId(agentRepo = agentRepo).execute(serviceId = 1)

        assertThat(responses).isNotNull()
        assertThat(responses.size).isEqualTo(3)
        assertThat(responses.get(0).id).isEqualTo(agents.find { it.role?.priority == 1 }?.id)
    }

    @Test
    fun `should return empty list when no agents found`() {
        Mockito.`when`(agentRepo.findByServiceId(1)).thenReturn(emptyList())
        val responses = GetCrewByServiceId(agentRepo = agentRepo).execute(serviceId = 1)

        assertThat(responses).isEmpty()
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val internalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Database error")
        )
        Mockito.`when`(agentRepo.findByServiceId(1)).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            GetCrewByServiceId(agentRepo = agentRepo).execute(serviceId = 1)
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }
}
