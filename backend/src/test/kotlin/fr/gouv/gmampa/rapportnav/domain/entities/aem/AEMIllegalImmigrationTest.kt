package fr.gouv.gmampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMIllegalImmigration
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionIllegalImmigrationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.ZonedDateTime
import java.util.*

@SpringBootTest(classes = [AEMIllegalImmigration::class])
class AEMIllegalImmigrationTest {

    @Test
    fun `Should init illegal immigration with different values`() {
        val nbrOfHourAtSea = 3;
        val nbrOfInterceptedVessel = 5;
        val nbrOfInterceptedMigrant = 14;
        val nbrOfSuspectedSmuggler = 2;
        val actions = navActionEntities()
        val illegalImmigration = AEMIllegalImmigration(navActions = actions);

        assertThat(illegalImmigration).isNotNull();
        assertThat(illegalImmigration.nbrOfHourAtSea).isEqualTo(nbrOfHourAtSea);
        assertThat(illegalImmigration.nbrOfInterceptedVessel).isEqualTo(nbrOfInterceptedVessel);
        assertThat(illegalImmigration.nbrOfInterceptedMigrant).isEqualTo(nbrOfInterceptedMigrant);
        assertThat(illegalImmigration.nbrOfSuspectedSmuggler).isEqualTo(nbrOfSuspectedSmuggler);
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
                illegalImmigrationAction = ActionIllegalImmigrationEntity(
                    missionId = 761,
                    id = UUID.randomUUID(),
                    startDateTimeUtc = ZonedDateTime.parse("2019-09-09T00:00:00.000+01:00"),
                    endDateTimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00"),
                    nbOfInterceptedVessels = 1,
                    nbOfInterceptedMigrants = 5,
                    nbOfSuspectedSmugglers = 0,
                )
            ),
            NavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.ILLEGAL_IMMIGRATION,
                startDateTimeUtc = ZonedDateTime.parse("2019-09-09T02:00:00.000+01:00"),
                endDateTimeUtc = ZonedDateTime.parse("2019-09-09T04:00:00.000+01:00"),
                illegalImmigrationAction = ActionIllegalImmigrationEntity(
                    missionId = 761,
                    id = UUID.randomUUID(),
                    endDateTimeUtc = ZonedDateTime.parse("2019-09-09T04:00:00.000+01:00"),
                    startDateTimeUtc = ZonedDateTime.parse("2019-09-09T02:00:00.000+01:00"),
                    nbOfInterceptedVessels = 4,
                    nbOfInterceptedMigrants = 9,
                    nbOfSuspectedSmugglers = 2,
                )
            )
        );
        return actions
    }
}
