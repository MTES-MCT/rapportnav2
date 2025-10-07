package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.NavActionInfoEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.GetInfoAboutNavAction2
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionEnvActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant

@SpringBootTest(classes = [GetInfoAboutNavAction2::class, ComputeDurations::class])

class GetInfoAboutAction2Tests {

    @Autowired
    private lateinit var getInfoAboutNavAction: GetInfoAboutNavAction2

    private val actions = listOf<MissionActionEntity>(
        MissionNavActionEntityMock.create(
            actionType = ActionType.VIGIMER,
            startDateTimeUtc = Instant.parse("2022-01-02T12:00:00Z"),
            endDateTimeUtc = Instant.parse("2022-01-02T13:00:00Z")
        ),
        MissionNavActionEntityMock.create(
            actionType = ActionType.BAAEM_PERMANENCE,
            startDateTimeUtc = Instant.parse("2022-01-02T13:00:00Z"),
            endDateTimeUtc = Instant.parse("2022-01-02T14:00:00Z")
        ),
        MissionNavActionEntityMock.create(
            actionType = ActionType.RESCUE,
            startDateTimeUtc = Instant.parse("2022-01-02T14:00:00Z"),
            endDateTimeUtc = Instant.parse("2022-01-02T15:00:00Z")
        ),
        MissionEnvActionEntityMock.create(
            envActionType = ActionTypeEnum.SURVEILLANCE,
            startDateTimeUtc = Instant.parse("2022-01-02T14:00:00Z"),
            endDateTimeUtc = Instant.parse("2022-01-02T15:00:00Z")
        ),
        MissionEnvActionEntityMock.create(
            envActionType = ActionTypeEnum.CONTROL,
            startDateTimeUtc = Instant.parse("2022-01-02T14:00:00Z"),
            endDateTimeUtc = Instant.parse("2022-01-02T15:00:00Z")
        ),
      )

    @Test
    fun `should return null when arg actions is null`() {
        Assertions.assertThat(
            getInfoAboutNavAction.execute(
                null,
                listOf(ActionType.VIGIMER),
                MissionSourceEnum.RAPPORTNAV
            )
        ).isNull()
    }

    @Test
    fun `should return null when arg actions is empty`() {
        Assertions.assertThat(
            getInfoAboutNavAction.execute(
                emptyList(),
                listOf(ActionType.VIGIMER),
                MissionSourceEnum.RAPPORTNAV
            )
        ).isNull()
    }

    @Test
    fun `should return null when arg actionTypes is empty`() {
        Assertions.assertThat(getInfoAboutNavAction.execute(actions, listOf(), MissionSourceEnum.RAPPORTNAV)).isNull()
    }

    @Test
    fun `should return the correct amount for 1 actionType`() {
        Assertions.assertThat(
            getInfoAboutNavAction.execute(
                actions,
                listOf(ActionType.RESCUE),
                MissionSourceEnum.RAPPORTNAV
            )
        ).isEqualTo(
            NavActionInfoEntity(count = 1, durationInHours = 1.0, amountOfInterrogatedShips = null)
        )
    }

    @Test
    fun `should return the correct amount for 2 actionTypes`() {
        Assertions.assertThat(
            getInfoAboutNavAction.execute(
                actions,
                listOf(ActionType.BAAEM_PERMANENCE, ActionType.VIGIMER),
                MissionSourceEnum.RAPPORTNAV
            )
        ).isEqualTo(
            NavActionInfoEntity(count = 2, durationInHours = 2.0, amountOfInterrogatedShips = null)
        )
    }
    @Test
    fun `should return one Env Surveillance`() {
        Assertions.assertThat(
            getInfoAboutNavAction.execute(
                actions,
                listOf(ActionType.SURVEILLANCE),
                MissionSourceEnum.MONITORENV
            )
        ).isEqualTo(
            NavActionInfoEntity(count = 1, durationInHours = 1.0, amountOfInterrogatedShips = null)
        )
    }
    @Test
    fun `should return one Env Control`() {
        Assertions.assertThat(
            getInfoAboutNavAction.execute(
                actions,
                listOf(ActionType.CONTROL),
                MissionSourceEnum.MONITORENV
            )
        ).isEqualTo(
            NavActionInfoEntity(count = 1, durationInHours = 1.0, amountOfInterrogatedShips = null)
        )
    }
}
