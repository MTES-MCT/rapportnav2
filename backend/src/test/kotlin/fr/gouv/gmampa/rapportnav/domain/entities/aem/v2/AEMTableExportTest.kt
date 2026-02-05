package fr.gouv.gmampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMTableExport
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [AEMTableExport::class])
class AEMTableExportTest {

    @Test
    fun `Should init Table export with different values from mission entity with nbrOfVesselRecognized`() {
        val nbrOfRecognizedVessel = 9;
        val envActions = listOf(
            MissionEnvActionEntity(
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                controlPlans = listOf(EnvActionControlPlanEntity(themeId = 101)),
                startDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            )
        )
        val navActions = listOf(
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.ILLEGAL_IMMIGRATION,
                startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            )
        )
        val fishActions = listOf(MissionFishActionEntity(
            missionId = 761,
            id = 234,
            fishActionType = MissionActionType.SEA_CONTROL,
            actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
            actionEndDatetimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
        ))

        val export = AEMTableExport.fromMissionAction(
            fishActions = fishActions,
            navActions = navActions,
            envActions = envActions,
            nbrOfRecognizedVessel = nbrOfRecognizedVessel.toDouble(),
            missionEndDateTimeUtc =  Instant.parse("2019-09-17T01:00:00.000+01:00")
        )
        assertThat(export).isNotNull();
        assertThat(export.seaSafety).isNotNull();
        assertThat(export.envTraffic).isNotNull();
        assertThat(export.illegalFish).isNotNull();
        assertThat(export.vesselRescue).isNotNull();
        assertThat(export.migrationRescue).isNotNull();
        assertThat(export.sovereignProtect).isNotNull();
        assertThat(export.culturalMaritime).isNotNull();
        assertThat(export.illegalImmigration).isNotNull();
        assertThat(export.outOfMigrationRescue).isNotNull();
        assertThat(export.pollutionControlSurveillance).isNotNull();
        assertThat(export.notPollutionControlSurveillance).isNotNull();
        assertThat(export.sovereignProtect?.nbrOfRecognizedVessel).isEqualTo(nbrOfRecognizedVessel.toDouble());
    }
}
