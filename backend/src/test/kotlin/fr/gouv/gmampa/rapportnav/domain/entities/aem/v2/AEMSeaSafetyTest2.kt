package fr.gouv.gmampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.aem.v2.AEMSeaSafety2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [AEMSeaSafety2::class])
class AEMSeaSafetyTest2 {

    @Test
    fun `Should init Sea safety with different values`() {
        val nbrOfHourAtSea = 14.0;
        val nbrOfHourPublicOrder = 5.0;
        val nbrOfPublicOrderOperation = 2.0;

        val actions = navActionEntities();
        val seaSafety = AEMSeaSafety2(navActions = actions);

        assertThat(seaSafety).isNotNull();
        assertThat(seaSafety.nbrOfHourAtSea).isEqualTo(nbrOfHourAtSea);
        assertThat(seaSafety.nbrOfHourPublicOrder).isEqualTo(nbrOfHourPublicOrder);
        assertThat(seaSafety.nbrOfPublicOrderOperation).isEqualTo(nbrOfPublicOrderOperation);
    }

    private fun navActionEntities(): List<MissionNavActionEntity> {
        val actions = listOf(
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.BAAEM_PERMANENCE,
                startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00")
            ),
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.VIGIMER,
                startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                ),
                MissionNavActionEntity(
                    id = UUID.randomUUID(),
                    missionId = 761,
                    actionType = ActionType.NAUTICAL_EVENT,
                    startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                    endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                ),
                MissionNavActionEntity(
                    id = UUID.randomUUID(),
                    missionId = 761,
                    actionType = ActionType.PUBLIC_ORDER,
                     startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                    endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                    observations = "",
                ),
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.PUBLIC_ORDER,
                endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                observations = ""
            )
        );
        return actions
    }
}
