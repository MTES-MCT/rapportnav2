package fr.gouv.gmampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMSovereignProtect
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.themes.ThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [AEMSovereignProtect::class])
class AEMSovereignProtectTest {

    @Test
    fun `Should set last status action endDateTimeUtc to missionEndDateTime`() {
        val navActions = listOf(
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.STATUS,
                startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
                status = ActionStatusType.NAVIGATING
            )
        )
        val missionEndDate = Instant.parse("2019-09-10T00:00:00.000+01:00")

        val result = AEMSovereignProtect.getNbrHourAtSea(navActions, missionEndDate)

        assertThat(navActions.first().endDateTimeUtc).isEqualTo(missionEndDate)
        assertThat(result).isGreaterThan(0.0)
    }

    @Test
    fun `Should return 0 for getNbrHourAtSea when no status actions`() {
        val navActions = listOf(
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.CONTROL,
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                controlMethod = ControlMethod.SEA
            )
        )
        val result = AEMSovereignProtect.getNbrHourAtSea(navActions, Instant.now())
        assertThat(result).isEqualTo(0.0)
    }

    @Test
    fun `Should default to 0 when matching actions have null targets`() {
        val timestamp = Instant.parse("2019-09-09T02:00:00.000+01:00")
        val target = { id: String -> TargetEntity(id = UUID.randomUUID(), actionId = id, targetType = TargetType.VEHICLE) }

        fun navAction(targets: List<TargetEntity>? = null) = MissionNavActionEntity(
            id = UUID.randomUUID(), missionId = 761, actionType = ActionType.CONTROL,
            startDateTimeUtc = timestamp, controlMethod = ControlMethod.SEA, targets = targets
        )
        fun envAction(actionNumberOfControls: Int? = null) = MissionEnvActionEntity(
            missionId = 761, id = UUID.randomUUID(), envActionType = ActionTypeEnum.CONTROL,
            startDateTimeUtc = timestamp, vehicleType = VehicleTypeEnum.VESSEL, actionNumberOfControls = actionNumberOfControls
        )
        fun fishAction(targets: List<TargetEntity>? = null) = MissionFishActionEntity(
            missionId = 761, id = 1, fishActionType = MissionActionType.SEA_CONTROL,
            actionDatetimeUtc = timestamp, targets = targets
        )

        // fish null targets, others with values
        var result = AEMSovereignProtect.getNbrOfControlledVessel(
            listOf(navAction(listOf(target("n1")))), listOf(envAction(2)), listOf(fishAction())
        )
        assertThat(result).isEqualTo(3.0) // 1 nav + 2 env + 0 fish

        // nav null targets, others with values
        result = AEMSovereignProtect.getNbrOfControlledVessel(
            listOf(navAction()), listOf(envAction(2)), listOf(fishAction(listOf(target("f1"))))
        )
        assertThat(result).isEqualTo(3.0) // 0 nav + 2 env + 1 fish

        // env null actionNumberOfControls, others with targets
        result = AEMSovereignProtect.getNbrOfControlledVessel(
            listOf(navAction(listOf(target("n1")))), listOf(envAction()), listOf(fishAction(listOf(target("f1"))))
        )
        assertThat(result).isEqualTo(2.0) // 1 nav + 0 env + 1 fish

        // all null/empty
        result = AEMSovereignProtect.getNbrOfControlledVessel(
            listOf(navAction()), listOf(envAction()), listOf(fishAction())
        )
        assertThat(result).isEqualTo(0.0)

        // null elements in fish and env lists
        result = AEMSovereignProtect.getNbrOfControlledVessel(
            listOf(navAction(listOf(target("n1")))), listOf(null, envAction(3)), listOf(null, fishAction(listOf(target("f1"))))
        )
        assertThat(result).isEqualTo(5.0) // 1 nav + 3 env + 1 fish
    }

    @Test
    fun `Should count controlled vessels from fish targets`() {
        val fishActions = listOf(
            MissionFishActionEntity(
                missionId = 761,
                id = 1,
                fishActionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                targets = listOf(
                    TargetEntity(id = UUID.randomUUID(), actionId = "f1", targetType = TargetType.VEHICLE),
                    TargetEntity(id = UUID.randomUUID(), actionId = "f1", targetType = TargetType.VEHICLE)
                )
            ),
            MissionFishActionEntity(
                missionId = 761,
                id = 2,
                fishActionType = MissionActionType.LAND_CONTROL,
                actionDatetimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                targets = listOf(
                    TargetEntity(id = UUID.randomUUID(), actionId = "f2", targetType = TargetType.VEHICLE)
                )
            )
        )
        val result = AEMSovereignProtect.getNbrOfControlledVessel(listOf(), listOf(), fishActions)
        assertThat(result).isEqualTo(2.0)
    }

    @Test
    fun `Should count controlled vessels from nav targets`() {
        val navActions = listOf(
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.CONTROL,
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                controlMethod = ControlMethod.SEA,
                targets = listOf(
                    TargetEntity(id = UUID.randomUUID(), actionId = "n1", targetType = TargetType.VEHICLE),
                    TargetEntity(id = UUID.randomUUID(), actionId = "n1", targetType = TargetType.VEHICLE),
                    TargetEntity(id = UUID.randomUUID(), actionId = "n1", targetType = TargetType.VEHICLE)
                )
            ),
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.STATUS,
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                status = ActionStatusType.NAVIGATING
            )
        )
        val result = AEMSovereignProtect.getNbrOfControlledVessel(navActions, listOf(), listOf())
        assertThat(result).isEqualTo(3.0)
    }

    @Test
    fun `Should count controlled vessels from env actions with VESSEL type, splitting CONTROL and SURVEILLANCE`() {
        val envActions = listOf(
            MissionEnvActionEntity( // VESSEL + CONTROL with actionNumberOfControls = 5 -> 5
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                vehicleType = VehicleTypeEnum.VESSEL,
                actionNumberOfControls = 5
            ),
            MissionEnvActionEntity( // VESSEL + SURVEILLANCE -> counts as 1
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.SURVEILLANCE,
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                vehicleType = VehicleTypeEnum.VESSEL
            ),
            MissionEnvActionEntity( // VEHICLE_LAND + CONTROL -> filtered out (not VESSEL)
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                vehicleType = VehicleTypeEnum.VEHICLE_LAND,
                actionNumberOfControls = 10
            )
        )
        val result = AEMSovereignProtect.getNbrOfControlledVessel(listOf(), envActions, listOf())
        assertThat(result).isEqualTo(6.0) // 5 from CONTROL + 1 from SURVEILLANCE
    }

    @Test
    fun `Should init sovereign protect with different values`() {
        val nbrOfHourAtSea = 147.0
        val nbrOfRecognizedVessel = 0.0
        val nbrOfControlledVessel = 6.0

        val navActions = getNavActions()
        val envActions = getEnvActions()
        val fishActions = getFishActions()
        val missionEndDate = Instant.parse("2019-09-15T01:00:00.000+01:00")

        val sovereign = AEMSovereignProtect(navActions, envActions, fishActions, missionEndDate)

        assertThat(sovereign).isNotNull()
        assertThat(sovereign.nbrOfHourAtSea).isEqualTo(nbrOfHourAtSea)
        assertThat(sovereign.nbrOfRecognizedVessel).isEqualTo(nbrOfRecognizedVessel)
        assertThat(sovereign.nbrOfControlledVessel).isEqualTo(nbrOfControlledVessel)
    }

    private fun getNavActions(): List<MissionNavActionEntity> {
        val actions = listOf(
            MissionNavActionEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionType = ActionType.CONTROL,
                endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                controlMethod = ControlMethod.SEA,
                targets = listOf(
                    TargetEntity(id = UUID.randomUUID(), actionId = "nav-1", targetType = TargetType.VEHICLE)
                )
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
        )
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
                actionNumberOfControls = 3,
                targets = listOf(
                    TargetEntity(id = UUID.randomUUID(), actionId = "env-1", targetType = TargetType.VEHICLE),
                    TargetEntity(id = UUID.randomUUID(), actionId = "env-1", targetType = TargetType.VEHICLE),
                    TargetEntity(id = UUID.randomUUID(), actionId = "env-1", targetType = TargetType.VEHICLE)
                )
            )
        )
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
                targets = listOf(
                    TargetEntity(id = UUID.randomUUID(), actionId = "fish-1", targetType = TargetType.VEHICLE)
                )
            ),
            MissionFishActionEntity(
                missionId = 761,
                id = 234,
                fishActionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                actionEndDatetimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
                targets = listOf(
                    TargetEntity(id = UUID.randomUUID(), actionId = "fish-2", targetType = TargetType.VEHICLE)
                )
            )
        )
        return actions
    }
}
