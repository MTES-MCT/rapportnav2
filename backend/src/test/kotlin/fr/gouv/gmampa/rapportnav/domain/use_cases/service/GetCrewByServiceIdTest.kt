package fr.gouv.gmampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetCrewByServiceId
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetCrewByServiceId::class])
@ContextConfiguration(classes = [GetCrewByServiceId::class])
class GetCrewByServiceIdTest {

    @Autowired
    private lateinit var getCrewByServiceId: GetCrewByServiceId

    @MockitoBean
    private lateinit var agentRepository: IAgentRepository

    private fun createAgentModel(
        id: Int,
        firstName: String,
        lastName: String,
        rolePriority: Int? = null,
        roleTitle: String? = null
    ): AgentModel {
        val role = if (rolePriority != null || roleTitle != null) {
            AgentRoleModel(id = id, title = roleTitle ?: "Role $id", priority = rolePriority)
        } else null

        return AgentModel(
            id = id,
            firstName = firstName,
            lastName = lastName,
            service = ServiceEntityMock.create(id = 1).toServiceModel(),
            role = role
        )
    }

    @Test
    fun `should return crew sorted by role priority`() {
        val serviceId = 1
        val agents = listOf(
            createAgentModel(1, "John", "Doe", rolePriority = 7, roleTitle = "Mécanicien"),
            createAgentModel(2, "Jane", "Smith", rolePriority = 1, roleTitle = "Commandant"),
            createAgentModel(3, "Bob", "Wilson", rolePriority = 4, roleTitle = "Chef mécanicien")
        )

        `when`(agentRepository.findByServiceId(serviceId)).thenReturn(agents)

        val result = getCrewByServiceId.execute(serviceId)

        assertThat(result).hasSize(3)
        // Sorted by role priority: 1, 4, 7
        assertThat(result[0].firstName).isEqualTo("Jane")  // priority 1 - Commandant
        assertThat(result[1].firstName).isEqualTo("Bob")   // priority 4 - Chef mécanicien
        assertThat(result[2].firstName).isEqualTo("John")  // priority 7 - Mécanicien
    }

    @Test
    fun `should sort crew by numeric role priority order`() {
        val serviceId = 1
        // Create agents in random order with different priorities
        val agents = listOf(
            createAgentModel(1, "Agent1", "Last1", rolePriority = 14, roleTitle = "Cuisinier"),
            createAgentModel(2, "Agent2", "Last2", rolePriority = 1, roleTitle = "Commandant"),
            createAgentModel(3, "Agent3", "Last3", rolePriority = 8, roleTitle = "Chef de quart"),
            createAgentModel(4, "Agent4", "Last4", rolePriority = 2, roleTitle = "Second capitaine"),
            createAgentModel(5, "Agent5", "Last5", rolePriority = 13, roleTitle = "Électricien"),
            createAgentModel(6, "Agent6", "Last6", rolePriority = 9, roleTitle = "Maître d'équipage"),
            createAgentModel(7, "Agent7", "Last7", rolePriority = 3, roleTitle = "Second"),
        )

        `when`(agentRepository.findByServiceId(serviceId)).thenReturn(agents)

        val result = getCrewByServiceId.execute(serviceId)

        assertThat(result).hasSize(7)
        // Verify the order matches ascending priority
        assertThat(result[0].role?.priority).isEqualTo(1)   // Commandant
        assertThat(result[1].role?.priority).isEqualTo(2)   // Second capitaine
        assertThat(result[2].role?.priority).isEqualTo(3)   // Second
        assertThat(result[3].role?.priority).isEqualTo(8)   // Chef de quart
        assertThat(result[4].role?.priority).isEqualTo(9)   // Maître d'équipage
        assertThat(result[5].role?.priority).isEqualTo(13)  // Électricien
        assertThat(result[6].role?.priority).isEqualTo(14)  // Cuisinier
    }

    @Test
    fun `should place agents with null priority at the beginning when sorted`() {
        val serviceId = 1
        val agents = listOf(
            createAgentModel(1, "John", "Doe", rolePriority = 1, roleTitle = "Commandant"),
            createAgentModel(2, "Jane", "Smith", rolePriority = null, roleTitle = "Unknown Role"),
            createAgentModel(3, "Bob", "Wilson", rolePriority = 14, roleTitle = "Cuisinier")
        )

        `when`(agentRepository.findByServiceId(serviceId)).thenReturn(agents)

        val result = getCrewByServiceId.execute(serviceId)

        assertThat(result).hasSize(3)
        // null priority comes first when sorted
        assertThat(result[0].role?.priority).isNull()
        assertThat(result[0].firstName).isEqualTo("Jane")
        assertThat(result[1].role?.priority).isEqualTo(1)
        assertThat(result[2].role?.priority).isEqualTo(14)
    }

    @Test
    fun `should place agents without roles at the beginning when sorted`() {
        val serviceId = 1
        val agents = listOf(
            createAgentModel(1, "John", "Doe", rolePriority = 1, roleTitle = "Commandant"),
            createAgentModel(2, "Jane", "Smith", rolePriority = null, roleTitle = null),
            createAgentModel(3, "Bob", "Wilson", rolePriority = 14, roleTitle = "Cuisinier")
        )

        `when`(agentRepository.findByServiceId(serviceId)).thenReturn(agents)

        val result = getCrewByServiceId.execute(serviceId)

        assertThat(result).hasSize(3)
        // Agent without role comes first (null priority)
        assertThat(result[0].role).isNull()
        assertThat(result[0].firstName).isEqualTo("Jane")
        assertThat(result[1].role?.title).isEqualTo("Commandant")
        assertThat(result[2].role?.title).isEqualTo("Cuisinier")
    }

    @Test
    fun `should return empty list when no crew for service`() {
        val serviceId = 999

        `when`(agentRepository.findByServiceId(serviceId)).thenReturn(emptyList())

        val result = getCrewByServiceId.execute(serviceId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val serviceId = 1
        val exception = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Cause")
        )

        `when`(agentRepository.findByServiceId(serviceId)).thenAnswer { throw exception }

        val thrown = assertThrows<BackendInternalException> {
            getCrewByServiceId.execute(serviceId)
        }
        assertThat(thrown.message).isEqualTo("Repository error")
    }
}
