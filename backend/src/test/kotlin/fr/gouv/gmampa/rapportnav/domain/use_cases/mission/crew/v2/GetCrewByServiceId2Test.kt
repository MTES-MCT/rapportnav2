package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.crew.v2

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetCrewByServiceId2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant


@SpringBootTest(classes = [GetCrewByServiceId2::class])
@ContextConfiguration(classes = [GetCrewByServiceId2::class])
class GetCrewByServiceId2Test {

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
        val responses = GetCrewByServiceId2(agentRepo = agentRepo).execute(serviceId = 1)

        assertThat(responses).isNotNull()
        assertThat(responses.size).isEqualTo(3)
        assertThat(responses.get(0).id).isEqualTo(agents.find { it.role?.priority == 1 }?.id)
    }
}
