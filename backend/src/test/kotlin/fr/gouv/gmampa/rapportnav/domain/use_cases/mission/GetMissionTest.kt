package fr.gouv.gmampa.rapportnav.domain.use_cases.mission


import fr.gouv.dgampa.rapportnav.domain.entities.mission.ExtendedEnvMissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetFishActionsByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetNavMissionById
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.FishMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.NavMissionMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class GetMissionTest {

    private lateinit var getEnvMissionById: GetEnvMissionById
    private lateinit var getNavMissionById: GetNavMissionById
    private lateinit var getFishActionsByMissionId: GetFishActionsByMissionId
    private lateinit var getMission: GetMission

    @BeforeEach
    fun setUp() {
        getEnvMissionById = mock(GetEnvMissionById::class.java)
        getNavMissionById = mock(GetNavMissionById::class.java)
        getFishActionsByMissionId = mock(GetFishActionsByMissionId::class.java)
        getMission = GetMission(
            getEnvMissionById,
            getNavMissionById,
            getFishActionsByMissionId
        )
    }

    @Test
    fun `execute should throw exception when missionId is null`() {
        val missionId = null
        val envMission = EnvMissionMock.create(id = missionId)

        val exception = assertThrows<IllegalArgumentException> {
            getMission.execute(missionId, envMission)
        }

        assertEquals("GetMission - missionId cannot be null", exception.message)
    }

    @Test
    fun `execute should throw exception when getEnvMissionById returns null`() {
        val missionId = 1
        val envMission = EnvMissionMock.create()
        `when`(getEnvMissionById.execute(missionId, envMission)).thenReturn(null)

        val exception = assertThrows<IllegalArgumentException> {
            getMission.execute(missionId, envMission)
        }

        assertEquals("GetMission - ExtendedEnvMissionEntity cannot be null", exception.message)
    }

    @Test
    fun `execute should return MissionEntity with extended data`() {
        val missionId = 1
        val envMission = EnvMissionMock.create()
        val extendedEnvMission = ExtendedEnvMissionEntity.fromEnvMission(envMission)
        val fishActions: List<ExtendedFishActionEntity> =
            FishMissionMock.create().actions.map { ExtendedFishActionEntity.fromMissionAction(it) }
        val navMission = NavMissionMock.create()

        `when`(getEnvMissionById.execute(missionId, envMission)).thenReturn(extendedEnvMission)
        `when`(getFishActionsByMissionId.execute(missionId)).thenReturn(fishActions)
        `when`(getNavMissionById.execute(missionId, envMission.controlUnits)).thenReturn(navMission)

        val result = getMission.execute(missionId, envMission)

        assertNotNull(result)
    }
}
