package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.CrewModel
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [GetAgentsCrewByMissionId::class])
class GetAgentsCrewByMissionIdTest {

    @Autowired
    private lateinit var getAgentsCrewByMissionId: GetAgentsCrewByMissionId

    @MockitoBean
    private lateinit var agentCrewRepository: IMissionCrewRepository


    @Test
    fun `execute should return sorted list of crew members by role priority`() {

        val missionId = 1
        val missionIdUUID = UUID.randomUUID()

        val johnDoe = AgentModel(
            firstName = "John",
            lastName = "Doe",
            id = 1,
            service = ServiceModel(
                id = 1,
                name = "Service1",
                serviceType = ServiceTypeEnum.PAM
            )
        )

        val janeDoe = AgentModel(
            firstName = "Jane",
            lastName = "Doe",
            id = 2,
            service = ServiceModel(
                id = 1,
                name = "Service1",
                serviceType = ServiceTypeEnum.PAM
            )
        )

        val alfredDeMusset = AgentModel(
            firstName = "Alfred",
            lastName = "de Musset",
            id = 3,
            service = ServiceModel(
                id = 1,
                name = "Service1",
                serviceType = ServiceTypeEnum.PAM
            )
        )

        val guyDeMaupassant = AgentModel(
            firstName = "Guy",
            lastName = "de Maupassant",
            id = 4,
            service = ServiceModel(
                id = 1,
                name = "Service1",
                serviceType = ServiceTypeEnum.PAM
            )
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
            CrewModel(role = chefMecano, agent = janeDoe, missionId = missionId, id = 1, missionIdUUID = missionIdUUID),
            CrewModel(role = secondCapitaine, agent = johnDoe, missionId = missionId, id = 2, missionIdUUID = missionIdUUID),
            CrewModel(role = cuisinier, agent = alfredDeMusset, missionId = missionId, id = 3, missionIdUUID = missionIdUUID),
            CrewModel(role = commandant, agent = guyDeMaupassant, missionId = missionId, id = 4, missionIdUUID = missionIdUUID),
        )

        `when`(agentCrewRepository.findByMissionId(missionId)).thenReturn(crewMembers)


        val sortedCrew = getAgentsCrewByMissionId.execute(missionId, commentDefaultsToString = false)

        // Assert
        val expectedRoles = listOf("Commandant", "Second capitaine", "Chef mécanicien", "Cuisinier")
        Assertions.assertEquals(expectedRoles, sortedCrew.map { it.role?.title })

        `when`(agentCrewRepository.findByMissionIdUUID(missionIdUUID)).thenReturn(crewMembers)
        val sortedCrewUUID = getAgentsCrewByMissionId.execute(missionIdUUID, commentDefaultsToString = false)
        Assertions.assertEquals(expectedRoles, sortedCrewUUID.map { it.role?.title })

    }
}
