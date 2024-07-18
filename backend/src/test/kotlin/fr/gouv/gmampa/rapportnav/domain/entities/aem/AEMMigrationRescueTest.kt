package fr.gouv.gmampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMMigrationRescue
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.ZonedDateTime
import java.util.*


@SpringBootTest(classes = [AEMMigrationRescue::class])
class AEMMigrationRescueTest {

    @Test
    fun `Should init migration rescue with different values`() {
        val nbrOfHourAtSea = 2;
        val nbrOfOperation = 1;
        val nbrPersonsRescued = 2;
        val nbrAssistedVesselsReturningToShore = 1;
        val nbrOfVesselsTrackedWithoutIntervention = 5;

        val actions = navActionEntities()
        val migrationRescue = AEMMigrationRescue(navActions = actions);

        assertThat(migrationRescue).isNotNull();
        assertThat(migrationRescue.nbrOfHourAtSea).isEqualTo(nbrOfHourAtSea);
        assertThat(migrationRescue.nbrPersonsRescued).isEqualTo(nbrPersonsRescued);
        assertThat(migrationRescue.nbrOfOperation).isEqualTo(nbrOfOperation);
        assertThat(migrationRescue.nbrAssistedVesselsReturningToShore).isEqualTo(nbrAssistedVesselsReturningToShore);
        assertThat(migrationRescue.nbrOfVesselsTrackedWithoutIntervention).isEqualTo(nbrOfVesselsTrackedWithoutIntervention);
    }

    private fun navActionEntities(): List<NavActionEntity> {
        val actions = listOf(
            NavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.ILLEGAL_IMMIGRATION,
                startDateTimeUtc = ZonedDateTime.parse("2019-09-09T00:00:00.000+01:00"),
                endDateTimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00")
            ),
            NavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.ILLEGAL_IMMIGRATION,
                startDateTimeUtc = ZonedDateTime.parse("2019-09-09T00:00:00.000+01:00"),
                endDateTimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00"),
                rescueAction = ActionRescueEntity(
                    missionId = 761,
                    id = UUID.randomUUID(),
                    startDateTimeUtc = ZonedDateTime.parse("2019-09-09T00:00:00.000+01:00"),
                    endDateTimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00"),
                    observations = "",
                    numberPersonsRescued = 0,
                    numberOfDeaths = 0,
                    isMigrationRescue = false
                )
            ),
            NavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.RESCUE,
                startDateTimeUtc = ZonedDateTime.parse("2019-09-09T02:00:00.000+01:00"),
                endDateTimeUtc = ZonedDateTime.parse("2019-09-09T04:00:00.000+01:00"),
                rescueAction = ActionRescueEntity(
                    missionId = 761,
                    id = UUID.randomUUID(),
                    endDateTimeUtc = ZonedDateTime.parse("2019-09-09T04:00:00.000+01:00"),
                    startDateTimeUtc = ZonedDateTime.parse("2019-09-09T02:00:00.000+01:00"),
                    observations = "",
                    numberPersonsRescued = 2,
                    numberOfDeaths = 0,
                    isMigrationRescue = true,
                    nbAssistedVesselsReturningToShore = 1,
                    nbOfVesselsTrackedWithoutIntervention = 5
                )
            )
        );
        return actions
    }
}
