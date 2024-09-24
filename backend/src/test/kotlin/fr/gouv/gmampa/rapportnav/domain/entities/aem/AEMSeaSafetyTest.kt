package fr.gouv.gmampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMSeaSafety
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [AEMSeaSafety::class])
class AEMSeaSafetyTest {

    @Test
    fun `Should init Sea safety with different values`() {
        val nbrOfHourAtSea = 14.0;
        val nbrOfHourPublicOrder = 5.0;
        val nbrOfPublicOrderOperation = 2.0;

        val actions = navActionEntities();
        val seaSafety = AEMSeaSafety(navActions = actions);

        assertThat(seaSafety).isNotNull();
        assertThat(seaSafety.nbrOfHourAtSea).isEqualTo(nbrOfHourAtSea);
        assertThat(seaSafety.nbrOfHourPublicOrder).isEqualTo(nbrOfHourPublicOrder);
        assertThat(seaSafety.nbrOfPublicOrderOperation).isEqualTo(nbrOfPublicOrderOperation);
    }

    private fun navActionEntities(): List<NavActionEntity> {
        val actions = listOf(
            NavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.ILLEGAL_IMMIGRATION,
                startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                baaemPermanenceAction = ActionBAAEMPermanenceEntity(
                    id = UUID.randomUUID(),
                    missionId = 761,
                    startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                    endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                ),
                vigimerAction = ActionVigimerEntity(
                    id = UUID.randomUUID(),
                    missionId = 761,
                    startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                    endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                ),
                nauticalEventAction = ActionNauticalEventEntity(
                    id = UUID.randomUUID(),
                    missionId = 761,
                    startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                    endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                )
            ),
            NavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.ILLEGAL_IMMIGRATION,
                startDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                publicOrderAction = ActionPublicOrderEntity(
                    missionId = 761,
                    id = UUID.randomUUID(),
                    startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                    endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                    observations = "",
                )
            ),
            NavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.RESCUE,
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                publicOrderAction = ActionPublicOrderEntity(
                    missionId = 761,
                    id = UUID.randomUUID(),
                    endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                    startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                    observations = ""

                )
            )
        );
        return actions
    }
}
