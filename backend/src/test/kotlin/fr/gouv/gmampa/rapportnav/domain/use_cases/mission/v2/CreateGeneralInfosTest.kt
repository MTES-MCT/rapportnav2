package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.CreateGeneralInfos
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.MissionCrew
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew.JPAMissionCrewRepository
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfo2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.MissionCrewEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.UUID

@SpringBootTest(classes = [CreateGeneralInfos::class])
class CreateGeneralInfosTest {

    @Autowired
    private lateinit var createGeneralInfos: CreateGeneralInfos

    @MockitoBean
    private lateinit var generalInfoRepository: IMissionGeneralInfoRepository

    @MockitoBean
    private lateinit var crewRepository: JPAMissionCrewRepository

    @BeforeEach
    fun setUp() {
        generalInfoRepository = mock()
        crewRepository = mock()
        createGeneralInfos = CreateGeneralInfos(generalInfoRepository, crewRepository)
    }

    @Test
    fun `should save general info and crew`() {
        // Given
        val missionUuid = UUID.randomUUID()

        val crewEntity = MissionCrewEntityMock.create(agent = AgentEntity(
            firstName = "Alice",
            lastName = "",
        ))
        val crewModel = crewEntity.toMissionCrewModel()

        val generalInfoInput = MissionGeneralInfo2Mock.create(crew = listOf(MissionCrew.fromMissionCrewEntity(crewEntity)))

        val savedGeneralInfoModel = MissionGeneralInfoEntity(
            id = 100, missionId = 42, missionIdUUID = missionUuid
        )

        // Mock behavior
        whenever(generalInfoRepository.save(any())).thenReturn(savedGeneralInfoModel.toMissionGeneralInfoModel())
        whenever(crewRepository.save(any())).thenReturn(crewModel)

        // When
        val result = createGeneralInfos.execute(
            missionId = 42,
            missionIdUUID = missionUuid,
            generalInfo2 = generalInfoInput
        )

        // Then
        assertNotNull(result)
        assertEquals(100, result?.data?.id)
        assertEquals(missionUuid, result?.data?.missionIdUUID)
        assertEquals(1, result?.crew?.size)
        assertEquals("Alice", result?.crew?.first()?.agent?.firstName)

        verify(generalInfoRepository, times(1)).save(any())
        verify(crewRepository, times(1)).save(any())
    }

    @Test
    fun `should save general info even if crew is null`() {
        val missionId = 1234
        val generalInfoInput = MissionGeneralInfo2(crew = null)
        val savedGeneralInfoModel = MissionGeneralInfoEntity(id = missionId)

        whenever(generalInfoRepository.save(any())).thenReturn(savedGeneralInfoModel.toMissionGeneralInfoModel())

        val result = createGeneralInfos.execute(missionId = missionId, generalInfo2 = generalInfoInput)

        assertEquals(missionId, result?.data?.id)
        assertEquals(null, result?.crew)

        verify(generalInfoRepository, times(1)).save(any())
        verify(crewRepository, never()).save(any())
    }

    @Test
    fun `should return null when general info fails to save`() {
        val generalInfoInput = MissionGeneralInfo2Mock.create()
        whenever(generalInfoRepository.save(any())).thenReturn(null)

        val result = createGeneralInfos.execute(generalInfo2 = generalInfoInput)

        assertNull(result)
        verify(generalInfoRepository, times(1)).save(any())
        verify(crewRepository, never()).save(any())
    }

    @Test
    fun `should skip crew member if saving crew fails`() {
        val missionUuid = UUID.randomUUID()
        val crewEntity = MissionCrewEntityMock.create(agent = AgentEntity(firstName = "Bob", lastName = ""))
        val generalInfoInput = MissionGeneralInfo2Mock.create(crew = listOf(MissionCrew.fromMissionCrewEntity(crewEntity)))
        val savedGeneralInfoModel = MissionGeneralInfoEntity(id = 200, missionId = 99, missionIdUUID = missionUuid)

        whenever(generalInfoRepository.save(any())).thenReturn(savedGeneralInfoModel.toMissionGeneralInfoModel())
        whenever(crewRepository.save(any())).thenThrow(RuntimeException("DB error"))

        val result = createGeneralInfos.execute(missionId = 99, missionIdUUID = missionUuid, generalInfo2 = generalInfoInput)

        assertNotNull(result)
        assertEquals(200, result?.data?.id)
        assertTrue(result?.crew?.isEmpty() == true)

        verify(generalInfoRepository, times(1)).save(any())
        verify(crewRepository, times(1)).save(any())
    }

    @Test
    fun `should return null when top-level exception is thrown`() {
        val generalInfoInput = MissionGeneralInfo2Mock.create()
        whenever(generalInfoRepository.save(any())).thenThrow(RuntimeException("Unexpected failure"))

        val result = createGeneralInfos.execute(generalInfo2 = generalInfoInput)

        assertNull(result)
        verify(generalInfoRepository, times(1)).save(any())
        verify(crewRepository, never()).save(any())
    }
}
