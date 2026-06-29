package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeNavMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetNavMissionById2
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionNavEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [GetComputeMission::class])
class GetComputeMissionTest {

    @Autowired
    private lateinit var getComputeMission: GetComputeMission

    @MockitoBean
    private lateinit var getNavMissionById2: GetNavMissionById2

    @MockitoBean
    private lateinit var getComputeEnvMission: GetComputeEnvMission

    @MockitoBean
    private lateinit var getComputeNavMission: GetComputeNavMission

    @Test
    fun `should throw exception when nav mission not found`() {
        val id = UUID.randomUUID()
        `when`(getNavMissionById2.execute(id = id)).thenReturn(null)

        val exception = assertThrows<BackendUsageException> {
            getComputeMission.execute(id = id)
        }
        assertEquals(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION, exception.code)
        verifyNoInteractions(getComputeEnvMission)
        verifyNoInteractions(getComputeNavMission)
    }

    @Test
    fun `should delegate to getComputeEnvMission when externalId exists`() {
        val id = UUID.randomUUID()
        val navMission = MissionNavEntityMock.create(id = id, externalId = "123")
        val expectedMission = MissionEntityMock.create(id = id)

        `when`(getNavMissionById2.execute(id = id)).thenReturn(navMission)
        `when`(getComputeEnvMission.execute(externalId = 123)).thenReturn(expectedMission)

        val result = getComputeMission.execute(id = id)

        assertEquals(expectedMission, result)
        verify(getComputeEnvMission).execute(externalId = 123)
        verifyNoInteractions(getComputeNavMission)
    }

    @Test
    fun `should delegate to getComputeNavMission when externalId is null`() {
        val id = UUID.randomUUID()
        val navMission = MissionNavEntityMock.create(id = id, externalId = null)
        val expectedMission = MissionEntityMock.create(id = id)

        `when`(getNavMissionById2.execute(id = id)).thenReturn(navMission)
        `when`(getComputeNavMission.execute(navMission = navMission)).thenReturn(expectedMission)

        val result = getComputeMission.execute(id = id)

        assertEquals(expectedMission, result)
        verify(getComputeNavMission).execute(navMission = navMission)
        verifyNoInteractions(getComputeEnvMission)
    }

    @Test
    fun `should delegate to getComputeNavMission when externalId is not a valid integer`() {
        val id = UUID.randomUUID()
        val navMission = MissionNavEntityMock.create(id = id, externalId = "not-a-number")
        val expectedMission = MissionEntityMock.create(id = id)

        `when`(getNavMissionById2.execute(id = id)).thenReturn(navMission)
        `when`(getComputeNavMission.execute(navMission = navMission)).thenReturn(expectedMission)

        val result = getComputeMission.execute(id = id)

        assertEquals(expectedMission, result)
        verify(getComputeNavMission).execute(navMission = navMission)
        verifyNoInteractions(getComputeEnvMission)
    }
}
