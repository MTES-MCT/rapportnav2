package fr.gouv.gmampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMSovereignProtect
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.themes.ThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [AEMSovereignProtect::class])
class AEMSovereignProtectTest {

    @Test
    fun `Should init sovereign protect with different values`() {
        val nbrOfHourAtSea = 147.0;
        val nbrOfRecognizedVessel = 0.0;
        val nbrOfControlledVessel = 6.0;

        val navActions = getNavActions();
        val envActions = getEnvActions();
        val fishActions = getFishActions();
        val missionEndDate = Instant.parse("2019-09-15T01:00:00.000+01:00");

        val sovereign = AEMSovereignProtect(navActions, envActions, fishActions, missionEndDate);

        assertThat(sovereign).isNotNull();
        assertThat(sovereign.nbrOfHourAtSea).isEqualTo(nbrOfHourAtSea);
        assertThat(sovereign.nbrOfRecognizedVessel).isEqualTo(nbrOfRecognizedVessel);
        assertThat(sovereign.nbrOfControlledVessel).isEqualTo(nbrOfControlledVessel);
    }

    private fun getNavActions(): List<MissionNavActionEntity> {
        val actions = listOf(
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.CONTROL,
                endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                controlMethod = ControlMethod.SEA
            ),
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.STATUS,
                startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                status = ActionStatusType.ANCHORED
            ),
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.STATUS,
                endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                status = ActionStatusType.NAVIGATING
            )
        );
        return actions
    }

    private fun getEnvActions(): List<MissionEnvActionEntity> {
        val actions = listOf(
            MissionEnvActionEntity(
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                themes = listOf(ThemeEntity(id = 102, name = "Pollution")),
                startDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                vehicleType = VehicleTypeEnum.VEHICLE_LAND,
                actionNumberOfControls = 1
            ),
            MissionEnvActionEntity(
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                themes = listOf(ThemeEntity(id = 102, name = "Pollution")),
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                vehicleType = VehicleTypeEnum.VESSEL,
                actionNumberOfControls = 3
            )
        );
        return actions
    }

    private fun getFishActions(): List<MissionFishActionEntity> {
        val actions = listOf(
            MissionFishActionEntity(
                missionId = 761,
                id = 234,
                fishActionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                actionEndDatetimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
            ),
            MissionFishActionEntity(
                missionId = 761,
                id = 234,
                fishActionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                actionEndDatetimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            )
        );
        return actions
    }
}