package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentRole
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.MissionCrewModel
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

        val missionId = 1
        val missionIdUUID = UUID.randomUUID()

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
            MissionCrewEntity(role = chefMecano, agent = janeDoe, missionId = missionId, id = 1, missionIdUUID = missionIdUUID),
            MissionCrewEntity(role = secondCapitaine, agent = johnDoe, missionId = missionId, id = 2, missionIdUUID = missionIdUUID),
            MissionCrewEntity(role = cuisinier, agent = alfredDeMusset, missionId = missionId, id = 3, missionIdUUID = missionIdUUID),
            MissionCrewEntity(role = commandant, agent = guyDeMaupassant, missionId = missionId, id = 4, missionIdUUID = missionIdUUID),
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

    @Test
    fun `execute should sort crew with agents before crew with only fullName`() {
        val missionId = 1

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

        val sortedCrew = getAgentsCrewByMissionId.execute(missionId, commentDefaultsToString = false)

        // Crew with agents should come first, sorted by role priority
        Assertions.assertEquals(3, sortedCrew[0].id) // agent + Second capitaine
        Assertions.assertEquals(4, sortedCrew[1].id) // agent + Cuisinier
        // Then crew without agents, sorted by role priority
        Assertions.assertEquals(2, sortedCrew[2].id) // fullName + Commandant
        Assertions.assertEquals(1, sortedCrew[3].id) // fullName + Cuisinier
    }
}
