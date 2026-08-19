package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionList
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissions
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntity2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.UUID

@SpringBootTest(classes = [GetMissionList::class])
class GetMissionListTest {

    @Autowired
    private lateinit var getMissionList: GetMissionList

    @MockitoBean
    private lateinit var getMissions: GetMissions

    private val start = Instant.parse("2025-01-01T00:00:00Z")

    @Test
    fun `delegates to GetMissions and projects each mission to a light MissionListItem`() {
        val navUuid = UUID.randomUUID()
        val envMission = MissionEntity(
            id = 1,
            data = EnvMissionMock.create(id = 1),
            actions = listOf(MissionNavActionEntityMock.create()),
            generalInfos = MissionGeneralInfoEntity2Mock.create()
        )
        val navMission = MissionEntity(
            idUUID = navUuid,
            data = EnvMissionMock.create(id = null),
            actions = listOf(MissionNavActionEntityMock.create(), MissionNavActionEntityMock.create()),
            generalInfos = MissionGeneralInfoEntity2Mock.create()
        )

        whenever(getMissions.execute(start, null)).thenReturn(listOf(envMission, navMission))

        val result = getMissionList.execute(startDateTimeUtc = start)

        // it delegates to the same full compute orchestration (no behavior change, just a lighter payload)
        verify(getMissions).execute(start, null)

        assertEquals(2, result.size)

        val envItem = result.first { it.id == 1 }
        assertEquals(1, envItem.actionCount)
        // completeness is computed on the fly, identical to the full Mission response
        assertEquals(envMission.isCompleteForStats(), envItem.completenessForStats)

        val navItem = result.first { it.idUUID == navUuid.toString() }
        assertEquals(2, navItem.actionCount)
        assertEquals(navMission.isCompleteForStats(), navItem.completenessForStats)
    }
}
