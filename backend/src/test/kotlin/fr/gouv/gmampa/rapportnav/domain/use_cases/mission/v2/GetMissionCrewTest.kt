package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetActiveCrewForService
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionCrew
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.MissionCrew
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.AgentEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.AgentRoleEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.AgentServiceEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.MissionCrewEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [GetMissionCrew::class])
class GetMissionCrewTest {
    @Autowired
    private lateinit var getMissionCrew: GetMissionCrew

    @MockitoBean
    private lateinit var getActiveCrewForService: GetActiveCrewForService

    val role = AgentRoleEntityMock.create()
    val agent1 = AgentEntityMock.create(firstName = "John", lastName = "Doe")
    val agent2 = AgentEntityMock.create(firstName = "Jane", lastName = "Smith")

    @Test
    fun `execute get service, empty list if new service id is null`() {
        val serviceId = 3
        val result = getMissionCrew.execute(newServiceId = null)

        // Then
        assertThat(result).isEmpty()
        verify(getActiveCrewForService, times(0)).execute(serviceId)
    }

    @Test
    fun `execute get service, in database when new service id and old service id are different`() {
        val serviceId = 3
        val crewFromService = listOf(
            AgentServiceEntityMock.create(agent = agent1, role = role),
            AgentServiceEntityMock.create(agent = agent2, role = role),
        )
        Mockito.`when`(getActiveCrewForService.execute(serviceId)).thenReturn(crewFromService)
        val result = getMissionCrew.execute(newServiceId = serviceId, oldServiceId = 2)

        // Then
        assertThat(result.map { it.agent }.toSet()).isEqualTo(crewFromService.map { it.agent }.toSet())
        verify(getActiveCrewForService, times(1)).execute(serviceId)
    }

    @Test
    fun `execute get service, in general infos when new serviceId and old serviceId are the same`() {
        val serviceId = 3
        val missionId = 761
        val missionIdUUID = UUID.randomUUID()
        val crew = listOf(
            MissionCrewEntityMock.create(
                agent = agent1,
                role = role,
                missionId = missionId,
                missionIdUUID = missionIdUUID
            ),
            MissionCrewEntityMock.create(
                agent = agent2,
                role = role,
                missionId = missionId,
                missionIdUUID = missionIdUUID
            ),
        )
        val generalInfos = MissionGeneralInfo2(
            crew = crew.map { MissionCrew.fromMissionCrewEntity(it) }
        )
        val result = getMissionCrew.execute(
            newServiceId = serviceId,
            oldServiceId = serviceId,
            generalInfo = generalInfos,
            missionId = missionId,
            missionIdUUID = missionIdUUID
        )

        // Then
        assertThat(result.toSet()).isEqualTo(crew.toSet())
        verify(getActiveCrewForService, times(0)).execute(serviceId)
    }

}
