package fr.gouv.gmampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.aem.v2.AEMCulturalMaritime2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [AEMCulturalMaritime2::class])
class AEMCulturalMaritimeTest2 {

    @Test
    fun `Should init Cultural Maritime with different values`() {
        val actions = extendedEnvActionEntities()
        val culturalMaritime = AEMCulturalMaritime2(envActions = actions);

        assertThat(culturalMaritime).isNotNull();
        assertThat(culturalMaritime.nbrOfHourAtSea).isNotNull()
        assertThat(culturalMaritime.nbrOfHourAtSea).isEqualTo(4.0);
        assertThat(culturalMaritime.nbrOfBCMPoliceOperation).isEqualTo(2.0);
        assertThat(culturalMaritime.nbrOfScientificOperation).isEqualTo(1.0);
    }

    private fun extendedEnvActionEntities(): List<MissionEnvActionEntity> {
        val actions = listOf(
            MissionEnvActionEntity(
                missionId = "761",
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                controlPlans = listOf(EnvActionControlPlanEntity(themeId = 104, subThemeIds = listOf(143))),
                startDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            ),
            MissionEnvActionEntity(
                missionId = "761",
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.SURVEILLANCE,
                controlPlans = listOf(EnvActionControlPlanEntity(themeId = 101, subThemeIds = listOf(165))),
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
            ),
            MissionEnvActionEntity(
                missionId = "761",
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.SURVEILLANCE,
                controlPlans = listOf(EnvActionControlPlanEntity(themeId = 104, subThemeIds = listOf(67))),
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T05:00:00.000+01:00"),
            )
        );
        return actions
    }
}
