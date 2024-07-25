package fr.gouv.gmampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMTableExport
import fr.gouv.dgampa.rapportnav.domain.entities.mission.ExtendedEnvMissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.GearInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.LogbookInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.OtherInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.NavMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ExtendedEnvActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ExtendedFishActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.ZonedDateTime
import java.util.*


@SpringBootTest(classes = [AEMTableExport::class])
class AEMTableExportTest {

    @Test
    fun `Should init Table export with different values`() {
        val mission = missionEntity()
        val export = AEMTableExport.fromMissionAction(mission.actions!!);
        assertThat(export).isNotNull();
        assertThat(export.seaSafety).isNotNull();
        assertThat(export.envTraffic).isNotNull();
        assertThat(export.illegalFish).isNotNull();
        assertThat(export.vesselRescue).isNotNull();
        assertThat(export.migrationRescue).isNotNull();
        assertThat(export.culturalMaritime).isNotNull();
        assertThat(export.sovereignProtect).isNotNull();
        assertThat(export.illegalImmigration).isNotNull();
        assertThat(export.outOfMigrationRescue).isNotNull();
        assertThat(export.pollutionControlSurveillance).isNotNull();
        assertThat(export.notPollutionControlSurveillance).isNotNull();
    }


    @Test
    fun `Should init Table export with different values from mission entity with nbrOfVesselRecognized`() {

        val nbrOfRecognizedVessel = 9;
        val mission = missionEntity();
        mission.generalInfo?.nbrOfRecognizedVessel = nbrOfRecognizedVessel;
        val export = AEMTableExport.fromMission(mission);
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
        assertThat(export.sovereignProtect?.nbrOfRecognizedVessel).isEqualTo(nbrOfRecognizedVessel);
    }

    private fun missionEntity(): MissionEntity {
        val envMission = EnvMissionMock.create(envActions = listOf());
        val envActions = listOf(
            ExtendedEnvActionEntityMock.create(
                controlAction = ExtendedEnvActionControlEntity(
                    action = EnvActionControlEntity(
                        UUID.randomUUID(),
                        actionStartDateTimeUtc = ZonedDateTime.parse("2019-09-09T00:00:00.000+01:00"),
                        actionEndDateTimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00"),
                        controlPlans = listOf(EnvActionControlPlanEntity(themeId = 104, subThemeIds = listOf(143)))
                    )
                )
            )
        );
        val extendEnvMission = ExtendedEnvMissionEntity(mission = envMission, actions = envActions)
        val navActions = listOf(
            NavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.ILLEGAL_IMMIGRATION,
                startDateTimeUtc = ZonedDateTime.parse("2019-09-09T00:00:00.000+01:00"),
                endDateTimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00"),
                publicOrderAction = ActionPublicOrderEntity(
                    missionId = 761,
                    id = UUID.randomUUID(),
                    startDateTimeUtc = ZonedDateTime.parse("2019-09-08T22:00:00.000+01:00"),
                    endDateTimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00"),
                )
            )
        )
        val navMission = NavMissionMock.create(
            actions = navActions,
            generalInfo = MissionGeneralInfoEntity(id = 1, missionId = 761)
        );
        val fishMissionActions = listOf(
            ExtendedFishActionEntityMock.create(
                controlAction = ExtendedFishActionControlEntity(
                    action = FishActionControlMock.create(
                        speciesInfractions = listOf(),
                        otherInfractions = listOf(OtherInfraction()),
                        actionDatetimeUtc = ZonedDateTime.parse("2019-09-09T00:00:00.000+01:00"),
                        actionEndDatetimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00"),
                        gearInfractions = listOf(GearInfraction(infractionType = InfractionType.WITH_RECORD)),
                        logbookInfractions = listOf(LogbookInfraction(infractionType = InfractionType.WITHOUT_RECORD))
                    )
                )
            )
        );
        val mission = MissionEntity(
            envMission = extendEnvMission,
            navMission = navMission,
            fishMissionActions = fishMissionActions
        );
        return mission
    }
}
