package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetActiveCrewForService
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionCrew
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.UpdateMissionService2
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.AgentEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.AgentRoleEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.AgentServiceEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.MissionCrewEntityMock
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [UpdateMissionService2::class])
class UpdateMissionService2Test {
    @Autowired
    private lateinit var updateMissionService: UpdateMissionService2

    @MockitoBean
    private lateinit var processMissionCrew: ProcessMissionCrew

    @MockitoBean
    private lateinit var getActiveCrewForService: GetActiveCrewForService

    private val serviceId = 123
    private val missionId = "456"

    val role = AgentRoleEntityMock.create()
    val agent1 = AgentEntityMock.create(firstName = "John", lastName = "Doe")
    val agent2 = AgentEntityMock.create(firstName = "Jane", lastName = "Smith")

    private val crewFromService: List<AgentServiceEntity> = listOf(
        AgentServiceEntityMock.create(agent = agent1, role = role),
        AgentServiceEntityMock.create(agent = agent2, role = role),
    )
    private val newMissionCrew: List<MissionCrewEntity> = listOf(
        MissionCrewEntityMock.create(agent = agent1, role = role, missionId = missionId.toInt()),
        MissionCrewEntityMock.create(agent = agent2, role = role, missionId = missionId.toInt()),
    )

    @Test
    fun `execute update mission service and mission crew member`() {

        Mockito.`when`(getActiveCrewForService.execute(3)).thenReturn(crewFromService)
        Mockito.`when`(processMissionCrew.execute(eq(missionId), anyList())).thenReturn(newMissionCrew)

        val result = updateMissionService.execute(serviceId = serviceId, missionId = missionId)

        // Then
        assertTrue(result!!)
        verify(getActiveCrewForService, times(1)).execute(serviceId)
        verify(processMissionCrew, times(1)).execute(eq(missionId), anyList())
    }

    @Test
    fun `execute - should return false when processMissionCrew throws exception`() {
        Mockito.`when`(getActiveCrewForService.execute(3)).thenReturn(crewFromService)
        Mockito.`when`(processMissionCrew.execute(missionId, any<List<MissionCrewEntity>>())).thenThrow(RuntimeException("Database error"))

        /// When
        val result = updateMissionService.execute(serviceId = serviceId, missionId = missionId)

        // Then
        assertFalse(result!!)
       // verify(getActiveCrewForService, times(1)).execute(serviceId)
        verify(processMissionCrew, times(1)).execute(eq(missionId), anyList())
    }

}
