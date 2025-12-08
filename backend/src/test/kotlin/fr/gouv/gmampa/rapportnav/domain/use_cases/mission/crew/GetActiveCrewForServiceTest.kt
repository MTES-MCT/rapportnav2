package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentServiceRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetActiveCrewForService
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentServiceModel
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.AgentEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.AgentRoleEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.AgentServiceEntityMock
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
        AgentServiceEntityMock.create(
            id = 3,
            agent = AgentEntityMock.create(),
            role = AgentRoleEntityMock.create(),
        ).toAgentServiceModel(),
        AgentServiceEntityMock.create(
            id = 4,
            agent = AgentEntityMock.create(),
            role = AgentRoleEntityMock.create(),
            disabledAt = Instant.now()
        ).toAgentServiceModel()
    )

    @Test
    fun `execute getActiveCrewForService returns only active crew`() {
        Mockito.`when`(agentServiceRepo.findByServiceId(serviceId)).thenReturn(newMissionCrew)

        val response = getActiveCrewForService.execute(serviceId)
        assertThat(response.size).isEqualTo(1)
    }

}
