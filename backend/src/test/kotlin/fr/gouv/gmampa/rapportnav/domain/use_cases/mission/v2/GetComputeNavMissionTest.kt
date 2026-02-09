package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeNavMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetGeneralInfo
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetNavMissionById2
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionNavEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.UUID

@SpringBootTest(classes = [GetComputeNavMission::class])
class GetComputeNavMissionTest {

    @Autowired
    private lateinit var useCase: GetComputeNavMission

    @MockitoBean
    private lateinit var getGeneralInfo: GetGeneralInfo
    @MockitoBean
    private lateinit var getNavMissionById2: GetNavMissionById2
    @MockitoBean
    private lateinit var getComputeNavActionListByMissionId: GetComputeNavActionListByMissionId

    @Test
    fun `throws BackendUsageException when missionId and navMission are both null`() {
        assertThrows(BackendUsageException::class.java) {
            useCase.execute(null, null)
        }
        verifyNoInteractions(getGeneralInfo, getNavMissionById2, getComputeNavActionListByMissionId)
    }

    @Test
    fun `loads mission by ID when navMission is null`() {
        val missionId = UUID.randomUUID()
        val missionNav = MissionNavEntityMock.create(id = missionId)
        val generalInfo = mock<MissionGeneralInfoEntity>()
        val actions = listOf(mock<NavActionEntity>())

        whenever(getNavMissionById2.execute(missionId)).thenReturn(missionNav)
        whenever(getGeneralInfo.execute(missionIdUUID = missionId)).thenReturn(generalInfo)
        whenever(getComputeNavActionListByMissionId.execute(ownerId = missionId)).thenReturn(actions)

        val result = useCase.execute(missionId = missionId)

        assertNotNull(result)
        assertEquals(missionId, result.idUUID)
        assertEquals(actions, result.actions)
        assertEquals(generalInfo, result.generalInfos)

        verify(getNavMissionById2).execute(missionId)
        verify(getGeneralInfo).execute(missionIdUUID = missionId)
        verify(getComputeNavActionListByMissionId).execute(ownerId = missionId)
    }

    @Test
    fun `uses navMission directly when provided`() {
        val missionId = UUID.randomUUID()
        val missionNav = MissionNavEntityMock.create(id = missionId)
        val generalInfo = mock<MissionGeneralInfoEntity>()
        val actions = listOf(mock<NavActionEntity>())

        whenever(getGeneralInfo.execute(missionIdUUID = missionId, serviceId = missionNav.serviceId)).thenReturn(generalInfo)
        whenever(getComputeNavActionListByMissionId.execute(ownerId = missionId)).thenReturn(actions)

        val result = useCase.execute(navMission = missionNav)

        assertNotNull(result)
        assertEquals(missionId, result.idUUID)
        assertEquals(actions, result.actions)
        assertEquals(generalInfo, result.generalInfos)

        verifyNoInteractions(getNavMissionById2)
        verify(getGeneralInfo).execute(missionIdUUID = missionId, serviceId = missionNav.serviceId)
        verify(getComputeNavActionListByMissionId).execute(ownerId = missionId)
    }

    @Test
    fun `returns complete MissionEntity2`() {
        val missionId = UUID.randomUUID()
        val missionNav = MissionNavEntityMock.create(id = missionId)
        val generalInfo = mock<MissionGeneralInfoEntity>()
        val actions = listOf(mock<NavActionEntity>())
        val converted = MissionEnvEntity.fromMissionNavEntity(missionNav)

        whenever(getNavMissionById2.execute(missionId)).thenReturn(missionNav)
        whenever(getGeneralInfo.execute(missionIdUUID = missionId)).thenReturn(generalInfo)
        whenever(getComputeNavActionListByMissionId.execute(ownerId = missionId)).thenReturn(actions)

        val result = useCase.execute(missionId)

        assertEquals(MissionEntity(
            idUUID = missionId,
            generalInfos = generalInfo,
            actions = actions,
            data = converted
        ), result)
    }
}
