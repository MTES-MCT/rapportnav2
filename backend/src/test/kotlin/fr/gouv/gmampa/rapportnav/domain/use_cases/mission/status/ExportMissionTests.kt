package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.status

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurationsThroughoutMission
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.ZoneId
import java.time.ZonedDateTime


@SpringBootTest(classes = [GetStatusDurationsThroughoutMission::class])

class ExportMissionTests {

    @Autowired
    private lateinit var getStatusDurationsThroughoutMission: GetStatusDurationsThroughoutMission

    @Test
    fun `execute should return XXX when no statuses`() {
        val missionStartDateTime = ZonedDateTime.of(2023, 6, 19, 10, 0, 0, 0, ZoneId.of("Europe/Berlin"))
        val missionEndDateTime = ZonedDateTime.of(2023, 6, 30, 10, 0, 0, 0, ZoneId.of("Europe/Berlin"))
        val statuses = listOf<ActionStatusEntity>()

        val values = getStatusDurationsThroughoutMission.execute(missionStartDateTime, missionEndDateTime,  statuses);
        assertThat(values).isEqualTo(null);
    }
}
