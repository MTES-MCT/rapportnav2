package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionBAAEMPermanenceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionVigimerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.NavActionInfoEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.GetInfoAboutNavAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ActionMockFactory
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant

@SpringBootTest(classes = [GetInfoAboutNavAction::class, ComputeDurations::class])

class GetInfoAboutActionTests {

    @Autowired
    private lateinit var getInfoAboutNavAction: GetInfoAboutNavAction

    private val actions = listOf<MissionActionEntity>(
        MissionActionEntity.NavAction(
            ActionMockFactory.create<ActionVigimerEntity>(
                additionalParams = mapOf(
                    "startDateTimeUtc" to Instant.parse("2022-01-02T12:00:00Z"),
                    "endDateTimeUtc" to Instant.parse("2022-01-02T13:00:00Z"),
                )
            ).toNavActionEntity()
        ),
        MissionActionEntity.NavAction(
            ActionMockFactory.create<ActionBAAEMPermanenceEntity>(
                additionalParams = mapOf(
                    "startDateTimeUtc" to Instant.parse("2022-01-02T13:00:00Z"),
                    "endDateTimeUtc" to Instant.parse("2022-01-02T14:00:00Z"),
                )
            ).toNavActionEntity()
        ),
        MissionActionEntity.NavAction(
            ActionMockFactory.create<ActionRescueEntity>(
                additionalParams = mapOf(
                    "startDateTimeUtc" to Instant.parse("2022-01-02T14:00:00Z"),
                    "endDateTimeUtc" to Instant.parse("2022-01-02T15:00:00Z"),
                )
            ).toNavActionEntity()
        )
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
}
