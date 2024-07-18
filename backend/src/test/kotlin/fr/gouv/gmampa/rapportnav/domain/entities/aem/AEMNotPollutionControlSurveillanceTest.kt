package fr.gouv.gmampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMNotPollutionControlSurveillance
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ExtendedEnvActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.ZonedDateTime
import java.util.*

@SpringBootTest(classes = [AEMNotPollutionControlSurveillance::class])
class AEMNotPollutionControlSurveillanceTest {

    @Test
    fun `Should init not pollution control surveillance with different values`() {
        val nbrOfHourInSea = 7;
        val nbrOfInfraction = 3;
        val nbrOfInfractionWithNotice = 2;

        val actions = extendedEnvActionEntities()
        val notPollution = AEMNotPollutionControlSurveillance(envActions = actions);

        assertThat(notPollution).isNotNull();
        assertThat(notPollution.nbrOfHourInSea).isEqualTo(nbrOfHourInSea);
        assertThat(notPollution.nbrOfInfraction).isEqualTo(nbrOfInfraction);
        assertThat(notPollution.nbrOfInfractionWithNotice).isEqualTo(nbrOfInfractionWithNotice);
    }

    private fun extendedEnvActionEntities(): List<ExtendedEnvActionEntity> {
        val actions = listOf(
            ExtendedEnvActionEntityMock.create(
                controlAction = ExtendedEnvActionControlEntity(
                    action = EnvActionControlEntity(
                        UUID.randomUUID(),
                        controlPlans = listOf(EnvActionControlPlanEntity(themeId = 19)),
                        actionStartDateTimeUtc = ZonedDateTime.parse("2019-09-09T00:00:00.000+01:00"),
                        actionEndDateTimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00"),
                        infractions = listOf(
                            InfractionEntity(
                                id = "",
                                toProcess = false,
                                formalNotice = FormalNoticeEnum.YES,
                                infractionType = InfractionTypeEnum.WITH_REPORT
                            ),
                            InfractionEntity(
                                id = "",
                                toProcess = false,
                                formalNotice = FormalNoticeEnum.NO,
                                infractionType = InfractionTypeEnum.WITH_REPORT
                            ),
                            InfractionEntity(
                                id = "",
                                toProcess = false,
                                formalNotice = FormalNoticeEnum.YES,
                                infractionType = InfractionTypeEnum.WITH_REPORT
                            )
                        )
                    )
                )
            ),
            ExtendedEnvActionEntityMock.create(
                surveillanceAction = ExtendedEnvActionSurveillanceEntity(
                    action = EnvActionSurveillanceEntity(
                        UUID.randomUUID(),
                        controlPlans = listOf(EnvActionControlPlanEntity(themeId = 101)),
                        actionStartDateTimeUtc = ZonedDateTime.parse("2019-09-09T02:00:00.000+01:00"),
                        actionEndDateTimeUtc = ZonedDateTime.parse("2019-09-09T04:00:00.000+01:00"),

                        )
                )
            ),
            ExtendedEnvActionEntityMock.create(
                surveillanceAction = ExtendedEnvActionSurveillanceEntity(
                    action = EnvActionSurveillanceEntity(
                        UUID.randomUUID(),
                        controlPlans = listOf(EnvActionControlPlanEntity(themeId = 102)),
                        actionStartDateTimeUtc = ZonedDateTime.parse("2019-09-09T12:00:00.000+01:00"),
                        actionEndDateTimeUtc = ZonedDateTime.parse("2019-09-09T16:00:00.000+01:00"),
                    )
                )
            )

        );
        return actions
    }
}
