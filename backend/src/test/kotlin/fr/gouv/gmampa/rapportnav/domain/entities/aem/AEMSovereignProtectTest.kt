package fr.gouv.gmampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMSovereignProtect
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ExtendedEnvActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ExtendedFishActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.ZonedDateTime
import java.util.*


@SpringBootTest(classes = [AEMSovereignProtect::class])
class AEMSovereignProtectTest {

    @Test
    fun `Should init sovereign protect with different values`() {
        val nbrOfHourAtSea = 147;
        val nbrOfRecognizedVessel = 0;
        val nbrOfControlledVessel = 4;

        val navActions = navActionEntities();
        val envActions = extendedEnvActionEntities();
        val fishActions = extendedFishActionEntities();
        val missionEndDate = ZonedDateTime.parse("2019-09-15T01:00:00.000+01:00");

        val sovereign = AEMSovereignProtect(navActions, envActions, fishActions, missionEndDate);

        assertThat(sovereign).isNotNull();
        assertThat(sovereign.nbrOfHourAtSea).isEqualTo(nbrOfHourAtSea);
        assertThat(sovereign.nbrOfRecognizedVessel).isEqualTo(nbrOfRecognizedVessel);
        assertThat(sovereign.nbrOfControlledVessel).isEqualTo(nbrOfControlledVessel);
    }

    private fun navActionEntities(): List<NavActionEntity> {
        val actions = listOf(
            NavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.ILLEGAL_IMMIGRATION,
                startDateTimeUtc = ZonedDateTime.parse("2019-09-08T22:00:00.000+01:00"),
                endDateTimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00"),
                controlAction = ActionControlEntity(
                    missionId = 761,
                    id = UUID.randomUUID(),
                    endDateTimeUtc = ZonedDateTime.parse("2019-09-09T04:00:00.000+01:00"),
                    startDateTimeUtc = ZonedDateTime.parse("2019-09-09T02:00:00.000+01:00"),
                    controlMethod = ControlMethod.SEA
                )
            ),
            NavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.STATUS,
                startDateTimeUtc = ZonedDateTime.parse("2019-09-09T00:00:00.000+01:00"),
                endDateTimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00"),
                statusAction = ActionStatusEntity(
                    missionId = 761,
                    id = UUID.randomUUID(),
                    startDateTimeUtc = ZonedDateTime.parse("2019-09-08T22:00:00.000+01:00"),
                    endDateTimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00"),
                    status = ActionStatusType.ANCHORED
                )
            ),
            NavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.STATUS,
                startDateTimeUtc = ZonedDateTime.parse("2019-09-09T02:00:00.000+01:00"),
                endDateTimeUtc = ZonedDateTime.parse("2019-09-09T04:00:00.000+01:00"),
                statusAction = ActionStatusEntity(
                    missionId = 761,
                    id = UUID.randomUUID(),
                    endDateTimeUtc = ZonedDateTime.parse("2019-09-09T04:00:00.000+01:00"),
                    startDateTimeUtc = ZonedDateTime.parse("2019-09-09T02:00:00.000+01:00"),
                    status = ActionStatusType.NAVIGATING
                )
            )
        );
        return actions
    }

    private fun extendedEnvActionEntities(): List<ExtendedEnvActionEntity> {
        val actions = listOf(
            ExtendedEnvActionEntityMock.create(
                controlAction = ExtendedEnvActionControlEntity(
                    action = EnvActionControlEntity(
                        UUID.randomUUID(),
                        controlPlans = listOf(EnvActionControlPlanEntity(themeId = 102)),
                        actionStartDateTimeUtc = ZonedDateTime.parse("2019-09-09T00:00:00.000+01:00"),
                        actionEndDateTimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00"),
                        vehicleType = VehicleTypeEnum.VEHICLE_LAND
                    )
                )
            ),
            ExtendedEnvActionEntityMock.create(
                controlAction = ExtendedEnvActionControlEntity(
                    action = EnvActionControlEntity(
                        UUID.randomUUID(),
                        controlPlans = listOf(EnvActionControlPlanEntity(themeId = 102)),
                        actionStartDateTimeUtc = ZonedDateTime.parse("2019-09-09T02:00:00.000+01:00"),
                        actionEndDateTimeUtc = ZonedDateTime.parse("2019-09-09T04:00:00.000+01:00"),
                        vehicleType = VehicleTypeEnum.VESSEL
                    )
                )
            )
        );
        return actions
    }

    private fun extendedFishActionEntities(): List<ExtendedFishActionEntity> {
        val actions = listOf(
            ExtendedFishActionEntityMock.create(
                controlAction = ExtendedFishActionControlEntity(
                    action = FishActionControlMock.create(
                        actionDatetimeUtc = ZonedDateTime.parse("2019-09-09T00:00:00.000+01:00"),
                        actionEndDatetimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00"),
                    )
                )
            ),
            ExtendedFishActionEntityMock.create(
                controlAction = ExtendedFishActionControlEntity(
                    action = FishActionControlMock.create(
                        actionDatetimeUtc = ZonedDateTime.parse("2019-09-09T02:00:00.000+01:00"),
                        actionEndDatetimeUtc = ZonedDateTime.parse("2019-09-09T04:00:00.000+01:00"),
                    )
                )
            )
        );
        return actions
    }
}
