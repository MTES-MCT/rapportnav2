package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionBAAEMPermanenceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionVigimerEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.GetInfoAboutNavAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ActionMockFactory
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

@SpringBootTest(classes = [GetInfoAboutNavAction::class, ComputeDurations::class])

class GetInfoAboutActionTests {

    @Autowired
    private lateinit var getInfoAboutNavAction: GetInfoAboutNavAction

    private val actions = listOf<MissionActionEntity>(
        MissionActionEntity.NavAction(
            ActionMockFactory.create<ActionVigimerEntity>(
                additionalParams = mapOf(
                    "startDateTimeUtc" to ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 12, 0), ZoneOffset.UTC),
                    "endDateTimeUtc" to ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 13, 0), ZoneOffset.UTC),
                )
            ).toNavActionEntity()
        ),
        MissionActionEntity.NavAction(
            ActionMockFactory.create<ActionBAAEMPermanenceEntity>(
                additionalParams = mapOf(
                    "startDateTimeUtc" to ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 13, 0), ZoneOffset.UTC),
                    "endDateTimeUtc" to ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 14, 0), ZoneOffset.UTC),
                )
            ).toNavActionEntity()
        ),
        MissionActionEntity.NavAction(
            ActionMockFactory.create<ActionRescueEntity>(
                additionalParams = mapOf(
                    "startDateTimeUtc" to ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 14, 0), ZoneOffset.UTC),
                    "endDateTimeUtc" to ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 15, 0), ZoneOffset.UTC),
                )
            ).toNavActionEntity()
        )
    )

    @Test
    fun `should return null when arg actions is null`() {
        Assertions.assertThat(getInfoAboutNavAction.execute(null, listOf(ActionType.VIGIMER))).isNull()
    }

    @Test
    fun `should return null when arg actions is empty`() {
        Assertions.assertThat(getInfoAboutNavAction.execute(emptyList(), listOf(ActionType.VIGIMER))).isNull()
    }

    @Test
    fun `should return null when arg actionTypes is empty`() {
        Assertions.assertThat(getInfoAboutNavAction.execute(actions, listOf())).isNull()
    }

    @Test
    fun `should return the correct amount for 1 actionType`() {
        Assertions.assertThat(getInfoAboutNavAction.execute(actions, listOf(ActionType.RESCUE))).isEqualTo(
            GetInfoAboutNavAction.NavActionInfo(count = 1, durationInHours = 1.0, amountOfInterrogatedShips = null)
        )
    }

    @Test
    fun `should return the correct amount for 2 actionTypes`() {
        Assertions.assertThat(
            getInfoAboutNavAction.execute(
                actions,
                listOf(ActionType.BAAEM_PERMANENCE, ActionType.VIGIMER)
            )
        ).isEqualTo(
            GetInfoAboutNavAction.NavActionInfo(count = 2, durationInHours = 2.0, amountOfInterrogatedShips = null)
        )
    }
}
