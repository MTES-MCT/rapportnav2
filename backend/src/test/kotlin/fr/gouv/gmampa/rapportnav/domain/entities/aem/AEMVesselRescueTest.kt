package fr.gouv.gmampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMVesselRescue
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [AEMVesselRescue::class])
class AEMVesselRescueTest {

    @Test
    fun `Should init vessel rescue with different values`() {
        val nbrOfHourAtSea = 5;
        val nbrOfTowedVessel = 2;
        val nbrOfNoticedVessel = 1;
        val nbrOfRescuedOperation = 2;

        val actions = navActionEntities()
        val vesselREscue = AEMVesselRescue(navActions = actions);

        assertThat(vesselREscue).isNotNull();
        assertThat(vesselREscue.nbrOfHourAtSea).isEqualTo(nbrOfHourAtSea);
        assertThat(vesselREscue.nbrOfTowedVessel).isEqualTo(nbrOfTowedVessel);
        assertThat(vesselREscue.nbrOfNoticedVessel).isEqualTo(nbrOfNoticedVessel);
        assertThat(vesselREscue.nbrOfRescuedOperation).isEqualTo(nbrOfRescuedOperation);
    }

    private fun navActionEntities(): List<NavActionEntity> {
        val actions = listOf(
            NavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.ILLEGAL_IMMIGRATION,
                startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00")
            ),
            NavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.ILLEGAL_IMMIGRATION,
                startDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                rescueAction = ActionRescueEntity(
                    missionId = 761,
                    id = UUID.randomUUID(),
                    startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                    endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                    observations = "",
                    numberPersonsRescued = 4,
                    numberOfDeaths = 0,
                    isVesselRescue = true,
                    isVesselTowed = true,
                    isVesselNoticed = true
                )
            ),
            NavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.RESCUE,
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                rescueAction = ActionRescueEntity(
                    missionId = 761,
                    id = UUID.randomUUID(),
                    endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                    startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                    observations = "",
                    numberPersonsRescued = 2,
                    numberOfDeaths = 0,
                    isVesselRescue = true,
                    isVesselTowed = true,
                    isVesselNoticed = false
                )
            )
        );
        return actions
    }
}
