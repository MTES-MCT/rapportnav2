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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [UpdateMissionService2::class])
class UpdateMissionService2Test {
    @Autowired
    private lateinit var updateMissionService: UpdateMissionService2

    @MockitoBean
    private lateinit var processMissionCrew: ProcessMissionCrew

    @MockitoBean
    private lateinit var getActiveCrewForService: GetActiveCrewForService

    private val serviceId = 123
    private val missionId = 456

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
        assertEquals(result, newMissionCrew)
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
        assertThat(result!!).isEqualTo(false)
       // verify(getActiveCrewForService, times(1)).execute(serviceId)
        verify(processMissionCrew, times(1)).execute(eq(missionId), anyList())
    }


    @Test
    fun `execute update mission service and mission crew member for missionId uuid`() {
        val missionIdUUID = UUID.randomUUID()
        Mockito.`when`(getActiveCrewForService.execute(serviceId = 3)).thenReturn(crewFromService)
        Mockito.`when`(processMissionCrew.execute(any<UUID>(), any<List<MissionCrewEntity>>())).thenReturn(newMissionCrew)

        val result = updateMissionService.execute(serviceId = 3, missionIdUUID = missionIdUUID)

        // Then
        assertEquals(result, newMissionCrew)
        verify(getActiveCrewForService, times(1)).execute(3)
        verify(processMissionCrew, times(1)).execute(any<UUID>(), any<List<MissionCrewEntity>>())
    }
}
