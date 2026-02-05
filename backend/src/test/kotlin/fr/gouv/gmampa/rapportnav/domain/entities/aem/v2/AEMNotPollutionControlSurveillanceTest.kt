package fr.gouv.gmampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMNotPollutionControlSurveillance
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [AEMNotPollutionControlSurveillance::class])
class AEMNotPollutionControlSurveillanceTest {

    @Test
    fun `Should init not pollution control surveillance with different values`() {
        val nbrOfHourAtSea = 4.5
        val nbrOfInfraction = 6.0
        val nbrOfInfractionWithNotice = 2.0

        val actions = extendedEnvActionEntities()
        val notPollution = AEMNotPollutionControlSurveillance(envActions = actions)

        assertThat(notPollution).isNotNull()
        assertThat(notPollution.nbrOfHourAtSea).isEqualTo(nbrOfHourAtSea)
        assertThat(notPollution.nbrOfInfraction).isEqualTo(nbrOfInfraction)
        assertThat(notPollution.nbrOfInfractionWithNotice).isEqualTo(nbrOfInfractionWithNotice)
    }

    private fun extendedEnvActionEntities(): List<MissionEnvActionEntity> {
        val actions = listOf(
            MissionEnvActionEntity(
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                controlPlans = listOf(EnvActionControlPlanEntity(themeId = 101)),
                startDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T04:30:00.000+01:00"),
                envInfractions = listOf(
                    InfractionEnvEntity(
                        id = "",
                        toProcess = false,
                        formalNotice = FormalNoticeEnum.YES,
                        infractionType = InfractionTypeEnum.WITH_REPORT,
                        natinf = listOf("natinf-1", "natinf-2", "natinf-3"),
                    ),
                    InfractionEnvEntity(
                        id = "",
                        toProcess = false,
                        formalNotice = FormalNoticeEnum.NO,
                        infractionType = InfractionTypeEnum.WITHOUT_REPORT,
                        natinf = listOf("natinf-1")

                    ),
                    InfractionEnvEntity(
                        id = "",
                        toProcess = false,
                        formalNotice = FormalNoticeEnum.YES,
                        infractionType = InfractionTypeEnum.WITH_REPORT,
                        natinf = listOf("natinf-1", "natinf-2")
                    )
                )
            ),
            MissionEnvActionEntity(
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.SURVEILLANCE,
                controlPlans = listOf(EnvActionControlPlanEntity(themeId = 19)),
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                ),
            MissionEnvActionEntity(
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.SURVEILLANCE,
                controlPlans = listOf(EnvActionControlPlanEntity(themeId = 102)),
                startDateTimeUtc = Instant.parse("2019-09-09T12:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T16:00:00.000+01:00"),
            )

        )
        return actions
    }
}
