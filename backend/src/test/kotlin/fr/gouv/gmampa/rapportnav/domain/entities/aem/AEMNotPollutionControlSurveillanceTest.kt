package fr.gouv.gmampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMNotPollutionControlSurveillance
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionSurveillanceEntity
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ExtendedEnvActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [AEMNotPollutionControlSurveillance::class])
class AEMNotPollutionControlSurveillanceTest {

    @Test
    fun `Should init not pollution control surveillance with different values`() {
        val nbrOfHourAtSea = 4.5;
        val nbrOfInfraction = 6.0;
        val nbrOfInfractionWithNotice = 2.0;

        val actions = extendedEnvActionEntities()
        val notPollution = AEMNotPollutionControlSurveillance(envActions = actions);

        assertThat(notPollution).isNotNull();
        assertThat(notPollution.nbrOfHourAtSea).isEqualTo(nbrOfHourAtSea);
        assertThat(notPollution.nbrOfInfraction).isEqualTo(nbrOfInfraction);
        assertThat(notPollution.nbrOfInfractionWithNotice).isEqualTo(nbrOfInfractionWithNotice);
    }

    private fun extendedEnvActionEntities(): List<ExtendedEnvActionEntity> {
        val actions = listOf(
            ExtendedEnvActionEntityMock.create(
                controlAction = ExtendedEnvActionControlEntity(
                    action = EnvActionControlEntity(
                        UUID.randomUUID(),
                        controlPlans = listOf(EnvActionControlPlanEntity(themeId = 101)),
                        actionStartDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                        actionEndDateTimeUtc = Instant.parse("2019-09-09T04:30:00.000+01:00"),
                        infractions = listOf(
                            InfractionEntity(
                                id = "",
                                toProcess = false,
                                formalNotice = FormalNoticeEnum.YES,
                                infractionType = InfractionTypeEnum.WITH_REPORT,
                                natinf = listOf("natinf-1", "natinf-2", "natinf-3"),
                            ),
                            InfractionEntity(
                                id = "",
                                toProcess = false,
                                formalNotice = FormalNoticeEnum.NO,
                                infractionType = InfractionTypeEnum.WITHOUT_REPORT,
                                natinf = listOf("natinf-1")

                            ),
                            InfractionEntity(
                                id = "",
                                toProcess = false,
                                formalNotice = FormalNoticeEnum.YES,
                                infractionType = InfractionTypeEnum.WITH_REPORT,
                                natinf = listOf("natinf-1", "natinf-2")
                            )
                        )
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
                        controlPlans = listOf(EnvActionControlPlanEntity(themeId = 102)),
                        actionStartDateTimeUtc = Instant.parse("2019-09-09T12:00:00.000+01:00"),
                        actionEndDateTimeUtc = Instant.parse("2019-09-09T16:00:00.000+01:00"),
                    )
                )
            )

        );
        return actions
    }
}
