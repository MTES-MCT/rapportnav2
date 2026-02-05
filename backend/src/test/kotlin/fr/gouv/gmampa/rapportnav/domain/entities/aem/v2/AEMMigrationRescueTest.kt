package fr.gouv.gmampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMMigrationRescue
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [AEMMigrationRescue::class])
class AEMMigrationRescueTest {

    @Test
    fun `Should init migration rescue with different values`() {
        val nbrOfHourAtSea = 2.0;
        val nbrOfOperation = 1.0;
        val nbrPersonsRescued = 2.0;
        val nbrAssistedVesselsReturningToShore = 1.0;
        val nbrOfVesselsTrackedWithoutIntervention = 5.0;

        val actions = navActionEntities()
        val migrationRescue = AEMMigrationRescue(navActions = actions);

        assertThat(migrationRescue).isNotNull();
        assertThat(migrationRescue.nbrOfHourAtSea).isEqualTo(nbrOfHourAtSea);
        assertThat(migrationRescue.nbrPersonsRescued).isEqualTo(nbrPersonsRescued);
        assertThat(migrationRescue.nbrOfOperation).isEqualTo(nbrOfOperation);
        assertThat(migrationRescue.nbrAssistedVesselsReturningToShore).isEqualTo(nbrAssistedVesselsReturningToShore);
        assertThat(migrationRescue.nbrOfVesselsTrackedWithoutIntervention).isEqualTo(
            nbrOfVesselsTrackedWithoutIntervention
        );
    }

    private fun navActionEntities(): List<MissionNavActionEntity> {
        val actions = listOf(
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.ILLEGAL_IMMIGRATION,
                startDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00")
            ),
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.ILLEGAL_IMMIGRATION,
                startDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                observations = "",
                numberPersonsRescued = 0,
                numberOfDeaths = 0,
                isMigrationRescue = false
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
                isMigrationRescue = true,
                nbAssistedVesselsReturningToShore = 1,
                nbOfVesselsTrackedWithoutIntervention = 5
            )
        );
        return actions
    }
}
