package fr.gouv.gmampa.rapportnav.domain.utils

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionSurveillanceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionPublicOrderEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionSurveillanceEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ExtendedEnvActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.ZonedDateTime
import java.util.*

@SpringBootTest(classes = [AEMUtils::class])
class AEMUtilsTest {
    @Test
    fun `Should compute duration of a list of Base action`() {
        val nbrOfHour = 2009.5;
        val actions = listOf(
            ActionPublicOrderEntity(
                missionId = 761,
                id = UUID.randomUUID(),
                endDateTimeUtc = ZonedDateTime.parse("2024-01-11T14:30:00Z"),
                startDateTimeUtc = ZonedDateTime.parse("2024-01-09T10:00:00Z")
            ),
            ActionPublicOrderEntity(
                missionId = 763,
                id = UUID.randomUUID(),
                endDateTimeUtc = ZonedDateTime.parse("2024-02-11T15:30:00Z"),
                startDateTimeUtc = ZonedDateTime.parse("2024-01-10T09:00:00Z")
            ),
            ActionPublicOrderEntity(
                missionId = 763,
                id = UUID.randomUUID(),
                endDateTimeUtc = ZonedDateTime.parse("2024-03-30T15:30:00Z"),
                startDateTimeUtc = ZonedDateTime.parse("2024-02-10T09:00:00Z")
            )
        )
        assertThat(AEMUtils.getDurationInHours(actions)).isEqualTo(nbrOfHour);
    }

    @Test
    fun `Should compute duration of a list of env actions`() {
        val nbrOfHour = 7.0;
        val actions = listOf(
            ExtendedEnvActionEntityMock.create(
                controlAction = ExtendedEnvActionControlEntity(
                    action = EnvActionControlEntity(
                        UUID.randomUUID(),
                        actionStartDateTimeUtc = ZonedDateTime.parse("2019-09-09T00:00:00.000+01:00"),
                        actionEndDateTimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00"),
                    )
                )
            ),
            ExtendedEnvActionEntityMock.create(
                surveillanceAction = ExtendedEnvActionSurveillanceEntity(
                    action = EnvActionSurveillanceEntity(
                        UUID.randomUUID(),
                        actionStartDateTimeUtc = ZonedDateTime.parse("2019-09-09T02:00:00.000+01:00"),
                        actionEndDateTimeUtc = ZonedDateTime.parse("2019-09-09T04:00:00.000+01:00"),
                    )
                )
            ),
            ExtendedEnvActionEntityMock.create(
                surveillanceAction = ExtendedEnvActionSurveillanceEntity(
                    action = EnvActionSurveillanceEntity(
                        UUID.randomUUID(),
                        actionStartDateTimeUtc = ZonedDateTime.parse("2019-09-09T12:00:00.000+01:00"),
                        actionEndDateTimeUtc = ZonedDateTime.parse("2019-09-09T16:00:00.000+01:00"),
                    )
                )
            )
        );
        assertThat(AEMUtils.getEnvDurationInHours(actions)).isEqualTo(nbrOfHour);
    }
}
