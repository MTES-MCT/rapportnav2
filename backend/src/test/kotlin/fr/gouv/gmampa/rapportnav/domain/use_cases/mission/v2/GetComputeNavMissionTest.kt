package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeNavMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetGeneralInfo
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetNavMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.SyncMissionValidation
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionNavEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.never
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
    private lateinit var getNavMissionById: GetNavMissionById
    @MockitoBean
    private lateinit var getComputeNavActionListByMissionId: GetComputeNavActionListByMissionId
    @MockitoBean
    private lateinit var syncMissionValidation: SyncMissionValidation

    @Test
    fun `throws BackendUsageException when missionId and navMission are both null`() {
        assertThrows(BackendUsageException::class.java) {
            useCase.execute(null, null)
        }
        verifyNoInteractions(getGeneralInfo, getNavMissionById, getComputeNavActionListByMissionId)
    }

    @Test
    fun `loads mission by ID when navMission is null`() {
        val missionId = UUID.randomUUID()
        val missionNav = MissionNavEntityMock.create(id = missionId)
        val generalInfo = mock<MissionGeneralInfoEntity2>()
        val actions = listOf(mock<MissionNavActionEntity>())

        whenever(getNavMissionById.execute(missionId)).thenReturn(missionNav)
        whenever(getGeneralInfo.execute(missionIdUUID = missionId)).thenReturn(generalInfo)
        whenever(getComputeNavActionListByMissionId.execute(ownerId = missionId)).thenReturn(actions)

        val result = useCase.execute(missionId = missionId)

        assertNotNull(result)
        assertEquals(missionId, result.idUUID)
        assertEquals(actions, result.actions)
        assertEquals(generalInfo, result.generalInfos)

        verify(getNavMissionById).execute(missionId)
        verify(getGeneralInfo).execute(missionIdUUID = missionId)
        verify(getComputeNavActionListByMissionId).execute(ownerId = missionId)
        verify(syncMissionValidation).execute(result)
    }

    @Test
    fun `uses navMission directly when provided`() {
        val missionId = UUID.randomUUID()
        val missionNav = MissionNavEntityMock.create(id = missionId)
        val generalInfo = mock<MissionGeneralInfoEntity2>()
        val actions = listOf(mock<MissionNavActionEntity>())

        whenever(getGeneralInfo.execute(missionIdUUID = missionId, serviceId = missionNav.serviceId)).thenReturn(generalInfo)
        whenever(getComputeNavActionListByMissionId.execute(ownerId = missionId)).thenReturn(actions)

        val result = useCase.execute(navMission = missionNav)

        assertNotNull(result)
        assertEquals(missionId, result.idUUID)
        assertEquals(actions, result.actions)
        assertEquals(generalInfo, result.generalInfos)

        verifyNoInteractions(getNavMissionById)
        verify(getGeneralInfo).execute(missionIdUUID = missionId, serviceId = missionNav.serviceId)
        verify(getComputeNavActionListByMissionId).execute(ownerId = missionId)
    }

    @Test
    fun `does not re-validate or persist when the mission is already complete for stats (sticky)`() {
        val missionId = UUID.randomUUID()
        val missionNav = MissionNavEntityMock.create(id = missionId).copy(isCompleteForStats = true)
        val generalInfo = mock<MissionGeneralInfoEntity2>()
        val actions = listOf(mock<MissionNavActionEntity>())

        whenever(getGeneralInfo.execute(missionIdUUID = missionId, serviceId = missionNav.serviceId)).thenReturn(generalInfo)
        whenever(getComputeNavActionListByMissionId.execute(ownerId = missionId, bypassValidation = true)).thenReturn(actions)

        useCase.execute(navMission = missionNav)

        // actions fetched with the validation shortcut, and the stored completeness is left untouched.
        verify(getComputeNavActionListByMissionId).execute(ownerId = missionId, bypassValidation = true)
        verify(syncMissionValidation, never()).execute(any())
    }

    @Test
    fun `re-validates and persists on the force path even when already complete`() {
        val missionId = UUID.randomUUID()
        val missionNav = MissionNavEntityMock.create(id = missionId).copy(isCompleteForStats = true)
        val generalInfo = mock<MissionGeneralInfoEntity2>()
        val actions = listOf(mock<MissionNavActionEntity>())

        whenever(getGeneralInfo.execute(missionIdUUID = missionId, serviceId = missionNav.serviceId)).thenReturn(generalInfo)
        whenever(getComputeNavActionListByMissionId.execute(ownerId = missionId, bypassValidation = false)).thenReturn(actions)

        val result = useCase.execute(navMission = missionNav, forceComputeValidation = true)

        verify(getComputeNavActionListByMissionId).execute(ownerId = missionId, bypassValidation = false)
        verify(syncMissionValidation).execute(result)
    }

    @Test
    fun `returns complete MissionEntity2`() {
        val missionId = UUID.randomUUID()
        val missionNav = MissionNavEntityMock.create(id = missionId)
        val generalInfo = mock<MissionGeneralInfoEntity2>()
        val actions = listOf(mock<MissionNavActionEntity>())
        val converted = MissionEnvEntity.fromMissionNavEntity(missionNav)

        whenever(getNavMissionById.execute(missionId)).thenReturn(missionNav)
        whenever(getGeneralInfo.execute(missionIdUUID = missionId)).thenReturn(generalInfo)
        whenever(getComputeNavActionListByMissionId.execute(ownerId = missionId)).thenReturn(actions)

        val result = useCase.execute(missionId)

        assertEquals(MissionEntity(
            idUUID = missionId,
            generalInfos = generalInfo,
            actions = actions,
            data = converted,
            // resolved by the use case (fresh compute here, since this mission is not stored-complete)
            completenessForStats = result.completenessForStats
        ), result)
    }
}
