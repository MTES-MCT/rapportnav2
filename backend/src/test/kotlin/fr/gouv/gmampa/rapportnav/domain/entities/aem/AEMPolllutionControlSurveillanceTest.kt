package fr.gouv.gmampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMPollutionControlSurveillance
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ExtendedEnvActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [AEMPollutionControlSurveillance::class])
class AEMPolllutionControlSurveillanceTest {

    @Test
    fun `Should init  pollution control surveillance with different values`() {
        val nbrOfHourAtSea = 8.0;
        val nbrOfInfraction = 2.0;
        val nbrOfInfractionWithNotice = 1.0;
        val nbrOfDiversionCarriedOut = 1.0;
        val nbrOfSimpleBrewingOperation = 2.0;
        val nbrOfAntiPolDeviceDeployed = 1.0;
        val nbrOfPollutionObservedByAuthorizedAgent = 1.0;

        val actions = navActionEntities();
        val extendedActions = extendedEnvActionEntities();
        val pollutionControl = AEMPollutionControlSurveillance(navActions = actions, envActions = extendedActions);

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

    private fun extendedEnvActionEntities(): List<ExtendedEnvActionEntity> {
        val actions = listOf(
            ExtendedEnvActionEntityMock.create(
                controlAction = ExtendedEnvActionControlEntity(
                    action = EnvActionControlEntity(
                        UUID.randomUUID(),
                        controlPlans = listOf(EnvActionControlPlanEntity(themeId = 102)),
                        actionStartDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                        actionEndDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                        infractions = listOf(
                            InfractionEntity(
                                id = "",
                                infractionType = InfractionTypeEnum.WITH_REPORT,
                                formalNotice = FormalNoticeEnum.YES,
                                toProcess = false
                            ),
                            InfractionEntity(
                                id = "",
                                infractionType = InfractionTypeEnum.WITHOUT_REPORT,
                                formalNotice = FormalNoticeEnum.NO,
                                toProcess = false
                            )
                        ),
                    )
                )
            ),
            ExtendedEnvActionEntityMock.create(
                surveillanceAction = ExtendedEnvActionSurveillanceEntity(
                    action = EnvActionSurveillanceEntity(
                        UUID.randomUUID(),
                        controlPlans = listOf(EnvActionControlPlanEntity(themeId = 19)),
                        actionStartDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                        actionEndDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                    )
                )
            ),
            ExtendedEnvActionEntityMock.create(
                surveillanceAction = ExtendedEnvActionSurveillanceEntity(
                    action = EnvActionSurveillanceEntity(
                        UUID.randomUUID(),
                        controlPlans = listOf(EnvActionControlPlanEntity(themeId = 100)),
                        actionStartDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                        actionEndDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                    )
                )
            )
        );
        return actions
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
                antiPollutionAction = ActionAntiPollutionEntity(
                    missionId = 761,
                    id = UUID.randomUUID(),
                    startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                    endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                    observations = "",
                    isAntiPolDeviceDeployed = true,
                    isSimpleBrewingOperationDone = true,
                    diversionCarriedOut = true
                )
            ),
            NavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.RESCUE,
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                antiPollutionAction = ActionAntiPollutionEntity(
                    missionId = 761,
                    id = UUID.randomUUID(),
                    endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                    startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                    observations = "",
                    isSimpleBrewingOperationDone = true,
                    pollutionObservedByAuthorizedAgent = true

                )
            )
        );
        return actions
    }
}
