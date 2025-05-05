package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetMissionAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetGeneralInfo2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMission2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetNavMissionById2
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfo2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntity2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetMission2::class])
class GetMission2Test {

    @Autowired
    private lateinit var getMission2: GetMission2

    @MockitoBean
    private lateinit var getGeneralInfos2: GetGeneralInfo2

    @MockitoBean
    private lateinit var getEnvMissionById2: GetEnvMissionById2

    @MockitoBean
    private lateinit var getMissionAction: GetMissionAction

    @MockitoBean
    private lateinit var getNavMissionById2: GetNavMissionById2

    @Test
    fun `should return null when both missionId and envMission are null`() {
        val result = getMission2.execute(null, null)
        assertNull(result)
    }

    @Test
    fun `should return null when missionId is null and envMission has null id`() {
        val envMission = EnvMissionMock.create(id = null)
        val result = getMission2.execute(null, envMission)
        assertNull(result)
    }

    @Test
    fun `should return missionEntity2 when envMission is provided with valid id`() {
        val envMission = EnvMissionMock.create(id = 1)

        val actions = listOf(MissionNavActionEntityMock.create())
        val generalInfos = MissionGeneralInfo2Mock.create().toMissionGeneralInfoEntity(missionId = "1")
        val generalInfos2 = MissionGeneralInfoEntity2Mock.create(data = generalInfos)

        `when`(getMissionAction.execute(missionId = 1)).thenReturn(actions)
        `when`(getGeneralInfos2.execute(missionId = "1", controlUnits = listOf())).thenReturn(generalInfos2)

        val result = getMission2.execute(envMission = envMission)

        assertNotNull(result)
        assertEquals(1, result?.id)
        assertEquals(envMission, result?.data)
        assertEquals(actions, result?.actions)
        assertEquals(generalInfos2, result?.generalInfos)
    }

    @Test
    fun `should retrieve mission by ID and return missionEntity2`() {
        val mission = EnvMissionMock.create(id = 2)

        val actions = listOf(MissionNavActionEntityMock.create())
        val generalInfos = MissionGeneralInfo2Mock.create().toMissionGeneralInfoEntity(missionId = "2")
        val generalInfos2 = MissionGeneralInfoEntity2Mock.create(data = generalInfos)

        `when`(getEnvMissionById2.execute(2)).thenReturn(mission)
        `when`(getMissionAction.execute(missionId = 2)).thenReturn(actions)
        `when`(getGeneralInfos2.execute(missionId = "2", controlUnits = listOf())).thenReturn(generalInfos2)

        val result = getMission2.execute(missionId = "2")

        assertNotNull(result)
        assertEquals(2, result?.id)
        assertEquals(mission, result?.data)
        assertEquals(actions, result?.actions)
        assertEquals(generalInfos2, result?.generalInfos)
    }

    @Test
    fun `should return null when mission retrieved by ID has null id`() {
        val mission = EnvMissionMock.create(id = null)

        `when`(getEnvMissionById2.execute(3)).thenReturn(mission)

        val result = getMission2.execute(missionId = "3")

        assertNull(result)
    }
}
