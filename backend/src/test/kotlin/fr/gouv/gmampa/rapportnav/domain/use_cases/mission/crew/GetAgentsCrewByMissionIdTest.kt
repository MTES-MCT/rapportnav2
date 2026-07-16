package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.AgentRoleEntityMock
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

        val missionId = UUID.randomUUID()

        val johnDoe = AgentEntity(
            firstName = "John",
            lastName = "Doe",
            id = 1,
            service = ServiceEntity(
                id = 1,
                name = "Service1",
                serviceType = ServiceTypeEnum.PAM
            )
        )

        val janeDoe = AgentEntity(
            firstName = "Jane",
            lastName = "Doe",
            id = 2,
            service = ServiceEntity(
                id = 1,
                name = "Service1",
                serviceType = ServiceTypeEnum.PAM
            )
        )

        val alfredDeMusset = AgentEntity(
            firstName = "Alfred",
            lastName = "de Musset",
            id = 3,
            service = ServiceEntity(
                id = 1,
                name = "Service1",
                serviceType = ServiceTypeEnum.PAM
            )
        )

        val guyDeMaupassant = AgentEntity(
            firstName = "Guy",
            lastName = "de Maupassant",
            id = 4,
            service = ServiceEntity(
                id = 1,
                name = "Service1",
                serviceType = ServiceTypeEnum.PAM
            )
        )

        val chefMecano = AgentRoleEntityMock.create(
            title = "Chef mécanicien",
            id = 1
        )

        val secondCapitaine = AgentRoleEntityMock.create(
            title = "Second capitaine",
            id = 2
        )

        val cuisinier = AgentRoleEntityMock.create(
            title = "Cuisinier",
            id = 3
        )

        val commandant = AgentRoleEntityMock.create(
            title = "Commandant",
            id = 4
        )

        val crewMembers = listOf(
            MissionCrewEntity(role = chefMecano, agent = janeDoe, id = 1),
            MissionCrewEntity(role = secondCapitaine, agent = johnDoe, id = 2),
            MissionCrewEntity(role = cuisinier, agent = alfredDeMusset, id = 3),
            MissionCrewEntity(role = commandant, agent = guyDeMaupassant, id = 4),
        )

        `when`(agentCrewRepository.findByMissionId(missionId)).thenReturn(crewMembers)


        val sortedCrew = getAgentsCrewByMissionId.execute(missionId)

        // Assert
        val expectedRoles = listOf("Commandant", "Second capitaine", "Chef mécanicien", "Cuisinier")
        Assertions.assertEquals(expectedRoles, sortedCrew.map { it.role?.title })

        `when`(agentCrewRepository.findByMissionId(missionId)).thenReturn(crewMembers)
        val sortedCrewUUID = getAgentsCrewByMissionId.execute(missionId)
        Assertions.assertEquals(expectedRoles, sortedCrewUUID.map { it.role?.title })

    }

    @Test
    fun `execute should sort crew with agents before crew with only fullName`() {
        val missionId = UUID.randomUUID()

        val agent = AgentEntity(
            firstName = "John",
            lastName = "Doe",
            id = 1,
            service = ServiceEntity(
                id = 1,
                name = "Service1",
                serviceType = ServiceTypeEnum.PAM
            )
        )

        val commandant = AgentRoleEntityMock.create(title = "Commandant", id = 1)
        val cuisinier = AgentRoleEntityMock.create(title = "Cuisinier", id = 2)
        val secondCapitaine = AgentRoleEntityMock.create(title = "Second capitaine", id = 3)

        val crewMembers = listOf(
            MissionCrewEntity(id = 1, role = cuisinier, agent = null, fullName = "FullName Only Cuisinier", missionId = missionId),
            MissionCrewEntity(id = 2, role = commandant, agent = null, fullName = "FullName Only Commandant", missionId = missionId),
            MissionCrewEntity(id = 3, role = secondCapitaine, agent = agent, missionId = missionId),
            MissionCrewEntity(id = 4, role = cuisinier, agent = agent, missionId = missionId),
        )

        `when`(agentCrewRepository.findByMissionId(missionId)).thenReturn(crewMembers)

        val sortedCrew = getAgentsCrewByMissionId.execute(missionId)

        // Crew with agents should come first, sorted by role priority
        Assertions.assertEquals(3, sortedCrew[0].id) // agent + Second capitaine
        Assertions.assertEquals(4, sortedCrew[1].id) // agent + Cuisinier
        // Then crew without agents, sorted by role priority
        Assertions.assertEquals(2, sortedCrew[2].id) // fullName + Commandant
        Assertions.assertEquals(1, sortedCrew[3].id) // fullName + Cuisinier
    }
}
