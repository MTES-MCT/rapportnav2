package fr.gouv.gmampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMEnvTraffic
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.EnvActionEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [AEMEnvTraffic::class])
class AEMEnvTrafficTest {

    @Test
    fun `Should init Env traffic with different values`() {
        val actions = extendedEnvActionEntities()
        val envTraffic = AEMEnvTraffic(envActions = actions);

        assertThat(envTraffic).isNotNull();
        assertThat(envTraffic.nbrOfHourAtSea).isNotNull()
        assertThat(envTraffic.nbrOfHourAtSea).isEqualTo(2.0);
        assertThat(envTraffic.nbrOfSeizure).isEqualTo(0.0);
        assertThat(envTraffic.nbrOfRedirectShip).isEqualTo(0.0);
    }

    private fun extendedEnvActionEntities(): List<EnvActionEntity> {
        val actions = listOf(
            EnvActionEntity(
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                controlPlans = listOf(EnvActionControlPlanEntity(themeId = 101)),
                startDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            ),
            EnvActionEntity(
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.SURVEILLANCE,
                controlPlans = listOf(EnvActionControlPlanEntity(themeId = 103)),
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
            )
        );
        return actions
    }
}
