package fr.gouv.gmampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMEnvTraffic
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionSurveillanceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionSurveillanceEntity
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ExtendedEnvActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.ZonedDateTime
import java.util.*

@SpringBootTest(classes = [AEMEnvTraffic::class])
class AEMEnvTrafficTest {

    @Test
    fun `Should init Env traffic with different values`() {
        val actions = extendedEnvActionEntities()
        val envTraffic = AEMEnvTraffic(envActions = actions);

        assertThat(envTraffic).isNotNull();
        assertThat(envTraffic.nbrOfHourInSea).isNotNull()
        assertThat(envTraffic.nbrOfHourInSea).isEqualTo(2);
        assertThat(envTraffic.nbrOfSeizure).isEqualTo(0);
        assertThat(envTraffic.nbrOfRedirectShip).isEqualTo(0);
    }

    private fun extendedEnvActionEntities(): List<ExtendedEnvActionEntity> {
        val actions = listOf(
            ExtendedEnvActionEntityMock.create(
                controlAction = ExtendedEnvActionControlEntity(
                    action = EnvActionControlEntity(
                        UUID.randomUUID(),
                        controlPlans = listOf(EnvActionControlPlanEntity(themeId = 101)),
                        actionStartDateTimeUtc = ZonedDateTime.parse("2019-09-09T00:00:00.000+01:00"),
                        actionEndDateTimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00"),
                    )
                )
            ),
            ExtendedEnvActionEntityMock.create(
                surveillanceAction = ExtendedEnvActionSurveillanceEntity(
                    action = EnvActionSurveillanceEntity(
                        UUID.randomUUID(),
                        controlPlans = listOf(EnvActionControlPlanEntity(themeId = 103)),
                        actionStartDateTimeUtc = ZonedDateTime.parse("2019-09-09T02:00:00.000+01:00"),
                        actionEndDateTimeUtc = ZonedDateTime.parse("2019-09-09T04:00:00.000+01:00"),
                    )
                )
            )
        );
        return actions
    }
}
