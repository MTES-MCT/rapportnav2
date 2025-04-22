package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.MissionCrewModel
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetAgentsCrewByMissionId::class])
class GetAgentsCrewByMissionIdTest {

    @Autowired
    private lateinit var getAgentsCrewByMissionId: GetAgentsCrewByMissionId

    @MockitoBean
    private lateinit var agentCrewRepository: IMissionCrewRepository


    @Test
    fun `execute should return sorted list of crew members by role priority`() {

        val missionId = "1"

        val johnDoe = AgentModel(
            firstName = "John",
            lastName = "Doe",
            id = 1
        )

        val janeDoe = AgentModel(
            firstName = "Jane",
            lastName = "Doe",
            id = 2
        )

        val alfredDeMusset = AgentModel(
            firstName = "Alfred",
            lastName = "de Musset",
            id = 3
        )

        val guyDeMaupassant = AgentModel(
            firstName = "Guy",
            lastName = "de Maupassant",
            id = 4
        )

        val chefMecano = AgentRoleModel(
            title = "Chef mécanicien",
            id = 1
        )

        val secondCapitaine = AgentRoleModel(
            title = "Second capitaine",
            id = 2
        )

        val cuisinier = AgentRoleModel(
            title = "Cuisinier",
            id = 3
        )

        val commandant = AgentRoleModel(
            title = "Commandant",
            id = 4
        )

        val crewMembers = listOf(
            MissionCrewModel(role = chefMecano, agent = janeDoe, missionId = missionId, id = 1),
            MissionCrewModel(role = secondCapitaine, agent = johnDoe, missionId = missionId, id = 2),
            MissionCrewModel(role = cuisinier, agent = alfredDeMusset, missionId = missionId, id = 3),
            MissionCrewModel(role = commandant, agent = guyDeMaupassant, missionId = missionId, id = 4),
        )

        `when`(agentCrewRepository.findByMissionId(missionId)).thenReturn(crewMembers)

        val sortedCrew = getAgentsCrewByMissionId.execute(missionId, commentDefaultsToString = false)

        // Assert
        val expectedRoles = listOf("Commandant", "Second capitaine", "Chef mécanicien", "Cuisinier")
        assertEquals(expectedRoles, sortedCrew.map { it.role?.title })
    }
}
