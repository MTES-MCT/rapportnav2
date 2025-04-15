package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetNavMissions
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [GetNavMissions::class])
class GetNavMissionsTest {

    @MockitoBean
    private lateinit var repository: IMissionNavRepository

    @Autowired
    private lateinit var getNavMissions: GetNavMissions

    @Test
    fun `should execute retrieve missions as list of MissionEntity2`()
    {
        val mockMission = MissionModel(
            id = UUID.randomUUID(),
            startDateTimeUtc = Instant.now(),
            endDateTimeUtc = Instant.now(),
            isDeleted = false,
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            controlUnitIdOwner = 1,
            controlUnits = listOf(1),
        )

        Mockito.`when`(repository.findAll(
            startBeforeDateTime = Instant.parse("2025-04-07T09:23:00.912559Z"),
            endBeforeDateTime = Instant.parse("2025-04-07T09:23:00.912559Z")
        )).thenReturn(listOf(mockMission))

        val missions = getNavMissions.execute(
            startDateTimeUtc = Instant.parse("2025-04-07T09:23:00.912559Z"),
            endDateTimeUtc = Instant.parse("2025-04-07T09:23:00.912559Z")
        )

        Assertions.assertNotNull(missions)
        Assertions.assertEquals(1, missions?.size)
        Assertions.assertNotNull(missions?.get(0))
        assertThat(missions?.get(0)).isInstanceOf(MissionEntity::class.java)
    }
}
