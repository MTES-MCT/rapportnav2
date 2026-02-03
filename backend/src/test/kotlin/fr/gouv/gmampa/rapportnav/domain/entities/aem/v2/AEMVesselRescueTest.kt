package fr.gouv.gmampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMVesselRescue
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [AEMVesselRescue::class])
class AEMVesselRescueTest {

    @Test
    fun `Should init vessel rescue with different values`() {
        val nbrOfHourAtSea = 5.0;
        val nbrOfTowedVessel = 2.0;
        val nbrOfNoticedVessel = 1.0;
        val nbrOfRescuedOperation = 2.0;

        val actions = getNavActions()
        val vesselRescue = AEMVesselRescue(navActions = actions);

        assertThat(vesselRescue).isNotNull();
        assertThat(vesselRescue.nbrOfHourAtSea).isEqualTo(nbrOfHourAtSea);
        assertThat(vesselRescue.nbrOfTowedVessel).isEqualTo(nbrOfTowedVessel);
        assertThat(vesselRescue.nbrOfNoticedVessel).isEqualTo(nbrOfNoticedVessel);
        assertThat(vesselRescue.nbrOfRescuedOperation).isEqualTo(nbrOfRescuedOperation);
    }

    private fun getNavActions(): List<MissionNavActionEntity> {
        val actions = listOf(
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.ILLEGAL_IMMIGRATION,
                startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00")
            ),
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.RESCUE,
                startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                observations = "",
                numberPersonsRescued = 4,
                numberOfDeaths = 0,
                isVesselRescue = true,
                isVesselTowed = true,
                isVesselNoticed = true
            ),
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.RESCUE,
                endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                observations = "",
                numberPersonsRescued = 2,
                numberOfDeaths = 0,
                isVesselRescue = true,
                isVesselTowed = true,
                isVesselNoticed = false
            )
        );
        return actions
    }
}
