package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentServiceRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetActiveCrewForService
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentServiceModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant

@SpringBootTest(classes = [GetActiveCrewForService::class])
class GetActiveCrewForServiceTest {

    @Autowired
    private lateinit var getActiveCrewForService: GetActiveCrewForService

    @MockitoBean
    private lateinit var agentServiceRepo: IAgentServiceRepository

    private val serviceId = 3

    private val newMissionCrew: List<AgentServiceModel> = listOf(
        AgentServiceModel(
            id = 3,
            service = ServiceModel(id = serviceId, name = "Service1"),
            agent = AgentModel(id = 1, firstName = "", lastName = ""),
            role = AgentRoleModel(id = 1, title = ""),
        ),
        AgentServiceModel(
            id = 4,
            service = ServiceModel(id = serviceId, name = "Service1"),
            agent = AgentModel(id = 1, firstName = "", lastName = ""),
            role = AgentRoleModel(id = 1, title = ""),
            disabledAt = Instant.now()
        )
    )

    @Test
    fun `execute getActiveCrewForService returns only active crew`() {
        Mockito.`when`(agentServiceRepo.findByServiceId(serviceId)).thenReturn(newMissionCrew)

        val response = getActiveCrewForService.execute(serviceId)
        assertThat(response.size).isEqualTo(1)
    }

}
