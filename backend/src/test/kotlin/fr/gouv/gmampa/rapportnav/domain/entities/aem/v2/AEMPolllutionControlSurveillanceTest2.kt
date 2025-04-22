package fr.gouv.gmampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.aem.v2.AEMPollutionControlSurveillance2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [AEMPollutionControlSurveillance2::class])
class AEMPolllutionControlSurveillanceTest2 {

    @Test
    fun `Should init  pollution control surveillance with different values`() {
        val nbrOfHourAtSea = 8.0;
        val nbrOfInfraction = 5.0;
        val nbrOfInfractionWithNotice = 1.0;
        val nbrOfDiversionCarriedOut = 1.0;
        val nbrOfSimpleBrewingOperation = 2.0;
        val nbrOfAntiPolDeviceDeployed = 1.0;
        val nbrOfPollutionObservedByAuthorizedAgent = 1.0;

        val actions = navActionEntities();
        val extendedActions = extendedEnvActionEntities();
        val pollutionControl = AEMPollutionControlSurveillance2(navActions = actions, envActions = extendedActions);

        assertThat(pollutionControl).isNotNull();
        assertThat(pollutionControl.nbrOfHourAtSea).isEqualTo(nbrOfHourAtSea);
        assertThat(pollutionControl.nbrOfInfraction).isEqualTo(nbrOfInfraction);
        assertThat(pollutionControl.nbrOfInfractionWithNotice).isEqualTo(nbrOfInfractionWithNotice);
        assertThat(pollutionControl.nbrOfDiversionCarriedOut).isEqualTo(nbrOfDiversionCarriedOut);
        assertThat(pollutionControl.nbrOfSimpleBrewingOperation).isEqualTo(nbrOfSimpleBrewingOperation);
        assertThat(pollutionControl.nbrOfAntiPolDeviceDeployed).isEqualTo(nbrOfAntiPolDeviceDeployed);
        assertThat(pollutionControl.nbrOfPollutionObservedByAuthorizedAgent).isEqualTo(
            nbrOfPollutionObservedByAuthorizedAgent
        );
    }

    private fun extendedEnvActionEntities(): List<MissionEnvActionEntity> {
        val actions = listOf(
            MissionEnvActionEntity(
                missionId = "761",
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                controlPlans = listOf(EnvActionControlPlanEntity(themeId = 102)),
                startDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                envInfractions = listOf(
                    InfractionEntity(
                        id = "",
                        infractionType = InfractionTypeEnum.WITH_REPORT,
                        formalNotice = FormalNoticeEnum.YES,
                        toProcess = false,
                        natinf = listOf("natinf-1", "natinf-2", "natinf-3"),
                    ),
                    InfractionEntity(
                        id = "",
                        infractionType = InfractionTypeEnum.WITHOUT_REPORT,
                        formalNotice = FormalNoticeEnum.NO,
                        toProcess = false,
                        natinf = listOf("natinf-1", "natinf-2"),
                    )
                ),
            ),
            MissionEnvActionEntity(
                missionId = "761",
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.SURVEILLANCE,
                controlPlans = listOf(EnvActionControlPlanEntity(themeId = 19)),
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
            ),
            MissionEnvActionEntity(
                missionId = "761",
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.SURVEILLANCE,
                controlPlans = listOf(EnvActionControlPlanEntity(themeId = 100)),
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
            )
        );
        return actions
    }

    private fun navActionEntities(): List<MissionNavActionEntity> {
        val actions = listOf(
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = "761",
                actionType = ActionType.ILLEGAL_IMMIGRATION,
                startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00")
            ),
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = "761",
                actionType = ActionType.ANTI_POLLUTION,
                startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                observations = "",
                isAntiPolDeviceDeployed = true,
                isSimpleBrewingOperationDone = true,
                diversionCarriedOut = true
            ),
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = "761",
                actionType = ActionType.ANTI_POLLUTION,
                endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                observations = "",
                isSimpleBrewingOperationDone = true,
                pollutionObservedByAuthorizedAgent = true
            )
        );
        return actions
    }
}
