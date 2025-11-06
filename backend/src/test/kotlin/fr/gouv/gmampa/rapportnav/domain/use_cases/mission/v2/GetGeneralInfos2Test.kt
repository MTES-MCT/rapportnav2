package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsByServiceId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetServiceByControlUnit
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.CreateGeneralInfos
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetGeneralInfo2
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.MissionCrewEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetGeneralInfo2::class])
class GetGeneralInfos2Test {

    @Autowired
    private lateinit var getGeneralInfo2: GetGeneralInfo2

    @MockitoBean
    private lateinit var createGeneralInfos: CreateGeneralInfos
    @MockitoBean
    private lateinit var getServiceByControlUnit: GetServiceByControlUnit
    @MockitoBean
    private lateinit var getAgentsCrewByMissionId: GetAgentsCrewByMissionId
    @MockitoBean
    private lateinit var getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId
    @MockitoBean
    private lateinit var getAgentsByServiceId: GetAgentsByServiceId

    @BeforeEach
    fun setUp() {
        createGeneralInfos  = mock()
        getServiceByControlUnit  = mock()
        getAgentsCrewByMissionId  = mock()
        getMissionGeneralInfoByMissionId  = mock()
        getAgentsByServiceId = mock()
        getGeneralInfo2 = GetGeneralInfo2(
            createGeneralInfos,
            getServiceByControlUnit,
            getAgentsCrewByMissionId,
            getMissionGeneralInfoByMissionId,
            getAgentsByServiceId
        )

    }

    @Test
    fun `should return existing general info for missionId`() {
        val missionId = 10
        val controlUnits = listOf(LegacyControlUnitEntity(id = 1))
        val services = listOf(ServiceEntity(id = 123, name = "Customs"))
        val generalInfo = MissionGeneralInfoEntity(id = 50, missionId = missionId)
        val crew = listOf(MissionCrewEntityMock.create())

        whenever(getServiceByControlUnit.execute(controlUnits)).thenReturn(services)
        whenever(getMissionGeneralInfoByMissionId.execute(missionId)).thenReturn(generalInfo)
        whenever(getAgentsCrewByMissionId.execute(missionId)).thenReturn(crew)

        val result = getGeneralInfo2.execute(missionId, controlUnits)

        assertEquals(services, result.services)
        assertEquals(crew, result.crew)
        assertEquals(generalInfo, result.data)
        verify(createGeneralInfos, never()).execute(any(), any(), any())
    }

    @Test
    fun `should create general info when not found`() {
        val missionId = 42
        val controlUnits = listOf(LegacyControlUnitEntity(id = 1))
        val services = listOf(ServiceEntity(id = 99, name = "Navy"))
        val created = MissionGeneralInfoEntity(id = 77, missionId = missionId)
        val crew = listOf(MissionCrewEntityMock.create())

        whenever(getServiceByControlUnit.execute(controlUnits)).thenReturn(services)
        whenever(getMissionGeneralInfoByMissionId.execute(missionId)).thenReturn(null)
        whenever(createGeneralInfos.execute(anyOrNull(), anyOrNull(), any())).thenReturn(MissionGeneralInfoEntity2(data = created))
        whenever(getAgentsCrewByMissionId.execute(missionId)).thenReturn(crew)

        val result = getGeneralInfo2.execute(missionId, controlUnits)

        assertEquals(services.first().id, result.services?.first()?.id)
        verify(createGeneralInfos, times(1)).execute(anyOrNull(), anyOrNull(), any())
    }

    @Test
    fun `should return empty crew when crew fetching fails`() {
        val missionId = 5
        val services = listOf(ServiceEntity(id = 1, name = "Navy"))
        val generalInfo = MissionGeneralInfoEntity(id = 99, missionId = missionId)

        whenever(getServiceByControlUnit.execute(null)).thenReturn(services)
        whenever(getMissionGeneralInfoByMissionId.execute(missionId)).thenReturn(generalInfo)
        whenever(getAgentsCrewByMissionId.execute(missionId)).thenThrow(RuntimeException("DB error"))

        val result = getGeneralInfo2.execute(missionId)

        assertTrue(result.crew!!.isEmpty())
        verify(getAgentsCrewByMissionId).execute(missionId)
    }

    @Test
    fun `should return empty services when fetch fails`() {
        val missionId = 123
        whenever(getServiceByControlUnit.execute(null)).thenThrow(RuntimeException("network"))
        whenever(getMissionGeneralInfoByMissionId.execute(missionId)).thenReturn(MissionGeneralInfoEntity(id = 1))

        val result = getGeneralInfo2.execute(missionId)

        assertTrue(result.services?.isEmpty() == true)
    }

}
