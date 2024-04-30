package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionSurveillanceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.SpeciesInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GroupActionByDate
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.FormatActionsForTimeline
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatGeoCoords
import fr.gouv.dgampa.rapportnav.infrastructure.rapportnav1.adapters.inputs.TimelineActionItem
import fr.gouv.dgampa.rapportnav.infrastructure.rapportnav1.adapters.inputs.TimelineActions
import fr.gouv.gmampa.rapportnav.mocks.mission.action.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDate

@SpringBootTest(classes = [FormatActionsForTimeline::class, FormatDateTime::class, FormatGeoCoords::class])
class FormatActionsForTimelineTests {

    @Autowired
    private lateinit var formatActionsForTimeline: FormatActionsForTimeline

    @MockBean
    private lateinit var groupActionByDate: GroupActionByDate

    private val envControl =
        MissionActionEntity.EnvAction(ExtendedEnvActionEntity.fromEnvActionEntity(EnvActionControlMock.create()))
    private val envSurveillance =
        MissionActionEntity.EnvAction(ExtendedEnvActionEntity.fromEnvActionEntity(EnvActionSurveillanceMock.create()))
    private val fishControl =
        MissionActionEntity.FishAction(ExtendedFishActionEntity.fromMissionAction(FishActionControlMock.create()))
    private val navControl =
        MissionActionEntity.NavAction(NavActionControlMock.createActionControlEntity().toNavActionEntity())
    private val navStatus =
        MissionActionEntity.NavAction(NavActionStatusMock.createActionStatusEntity().toNavActionEntity())
    private val navFreeNote = MissionActionEntity.NavAction(NavActionFreeNoteMock.create().toNavActionEntity())

    @BeforeEach
    fun setUp() {
        // Set up mock behavior for GroupActionByDate
        val data: Map<LocalDate, List<MissionActionEntity>> = mapOf(
            LocalDate.of(2022, 1, 1) to listOf(envControl, fishControl, envSurveillance),
            LocalDate.of(2022, 1, 2) to listOf(navControl, navStatus, navFreeNote)
        )
        Mockito.`when`(groupActionByDate.execute(any())).thenReturn(data)
    }

    @Test
    fun `formatTimeline should return null when null given`() {
        assertThat(formatActionsForTimeline.formatTimeline(null)).isNull()
    }

    @Test
    fun `formatTimeline should return null when empty list given`() {
        assertThat(formatActionsForTimeline.formatTimeline(emptyList())).isNull()
    }

    @Test
    fun `formatTimeline should return the correct structure and text`() {
        assertThat(
            formatActionsForTimeline.formatTimeline(
                listOf(
                    envControl,
                    fishControl,
                    envSurveillance,
                    navControl,
                    navStatus,
                    navFreeNote
                )
            )
        ).isEqualTo(
            mapOf(
                LocalDate.of(2022, 1, 1) to listOf(
                    "12:00 / 14:00 - Contrôle Environnement",
                    "12:00 - Contrôle Pêche - (DD): 52.14,14.30 - Le Pi - LR 314 - Infractions: sans PV - RAS",
                    "12:00 / 14:00 - Surveillance Environnement",
                ),
                LocalDate.of(2022, 1, 2) to listOf(
                    "12:00 / 14:00 - Contrôle administratif ",
                    "12:00 - Navigation - observations",
                    "12:06 - Largué, appareillé"
                )
            )
        )
    }

    @Test
    fun `formatForRapportNav1 should return empty list when actions are null`() {
        assertThat(formatActionsForTimeline.formatForRapportNav1(null)).isEmpty()
    }

    @Test
    fun `formatForRapportNav1 should return empty list when actions are empty`() {
        assertThat(formatActionsForTimeline.formatForRapportNav1(emptyMap())).isEmpty()
    }

    @Test
    fun `formatForRapportNav1 should return the correct list`() {
        val data = formatActionsForTimeline.formatTimeline(
            listOf(
                envControl,
                fishControl,
                envSurveillance,
                navControl,
                navStatus,
                navFreeNote
            )
        )
        val expected: List<TimelineActions> = listOf(
            TimelineActions(
                date = "2022-01-01",
                freeNote = listOf<TimelineActionItem>(
                    TimelineActionItem(observations = "12:00 / 14:00 - Contrôle Environnement"),
                    TimelineActionItem(observations = "12:00 - Contrôle Pêche - (DD): 52.14,14.30 - Le Pi - LR 314 - Infractions: sans PV - RAS"),
                    TimelineActionItem(observations = "12:00 / 14:00 - Surveillance Environnement"),
                )
            ),
            TimelineActions(
                date = "2022-01-02",
                freeNote = listOf<TimelineActionItem>(
                    TimelineActionItem(observations = "12:00 / 14:00 - Contrôle administratif "),
                    TimelineActionItem(observations = "12:00 - Navigation - observations"),
                    TimelineActionItem(observations = "12:06 - Largué, appareillé"),
                )
            ),
        )
        assertThat(formatActionsForTimeline.formatForRapportNav1(data)).isEqualTo(expected)
    }

    @Test
    fun `formatEnvControl should return basic formatted string`() {
        val action: EnvActionControlEntity = EnvActionControlMock.create()
        assertThat(formatActionsForTimeline.formatEnvControl(action)).isEqualTo("12:00 / 14:00 - Contrôle Environnement")
    }

    @Test
    fun `formatEnvSurveillance should return basic formatted string`() {
        val action: EnvActionSurveillanceEntity = EnvActionSurveillanceMock.create()
        assertThat(formatActionsForTimeline.formatEnvSurveillance(action)).isEqualTo("12:00 / 14:00 - Surveillance Environnement")
    }

    @Test
    fun `formatEnvControl should return formatted string with facade`() {
        val action: EnvActionControlEntity = EnvActionControlMock.create(facade = "Golfe de Gascogne")
        assertThat(formatActionsForTimeline.formatEnvControl(action)).isEqualTo("12:00 / 14:00 - Contrôle Environnement - Golfe de Gascogne")
    }

    @Test
    fun `formatEnvControl should return formatted string with themes`() {
        val themes = listOf(
            ThemeEntity(
                theme = "Rejets illicites",
            ),
            ThemeEntity(
                theme = "Natura 2000",
            )
        )
        val action: EnvActionControlEntity = EnvActionControlMock.create(themes = themes)
        assertThat(formatActionsForTimeline.formatEnvControl(action)).isEqualTo("12:00 / 14:00 - Contrôle Environnement - Rejets illicites + Natura 2000")
    }

    @Test
    fun `formatEnvControl should return formatted string with amount of controls`() {
        val action: EnvActionControlEntity = EnvActionControlMock.create(actionNumberOfControls = 2)
        assertThat(formatActionsForTimeline.formatEnvControl(action)).isEqualTo("12:00 / 14:00 - Contrôle Environnement - 2 contrôle(s)")
    }

    @Test
    fun `formatEnvControl should return the complete formatted string`() {
        val action: EnvActionControlEntity = EnvActionControlMock.create(
            actionNumberOfControls = 2,
            facade = "Golfe de Gascogne",
            themes = listOf(
                ThemeEntity(
                    theme = "Rejets illicites",
                )
            )
        )
        assertThat(formatActionsForTimeline.formatEnvControl(action)).isEqualTo("12:00 / 14:00 - Contrôle Environnement - Golfe de Gascogne - Rejets illicites - 2 contrôle(s)")
    }

    @Test
    fun `formatEnvControl should return null if action is null`() {
        val action = null
        assertThat(formatActionsForTimeline.formatEnvControl(action)).isNull()
    }

    @Test
    fun `formatFishControl should return the formatted string`() {
        val action: MissionAction = FishActionControlMock.create()
        assertThat(formatActionsForTimeline.formatFishControl(action)).isEqualTo("12:00 - Contrôle Pêche - (DD): 52.14,14.30 - Le Pi - LR 314 - Infractions: sans PV - RAS")
    }

    @Test
    fun `formatFishControl should return the formatted string with natinfs`() {
        val action: MissionAction = FishActionControlMock.create(
            speciesInfractions = listOf(
                SpeciesInfraction().apply {
                    infractionType = InfractionType.WITH_RECORD
                    natinf = 123
                },
                SpeciesInfraction().apply {
                    infractionType = InfractionType.WITHOUT_RECORD
                    natinf = 456
                }
            )
        )
        assertThat(formatActionsForTimeline.formatFishControl(action)).isEqualTo("12:00 - Contrôle Pêche - (DD): 52.14,14.30 - Le Pi - LR 314 - Infractions: 1 PV - NATINF: 123 + 456")
    }

    @Test
    fun `formatFishControl should return null if action is null`() {
        val action = null
        assertThat(formatActionsForTimeline.formatFishControl(action)).isNull()
    }

    @Test
    fun `formatNavNote should return formatted string`() {
        val action: ActionFreeNoteEntity = NavActionFreeNoteMock.create()
        assertThat(formatActionsForTimeline.formatNavNote(action)).isEqualTo("12:06 - Largué, appareillé")
    }

    @Test
    fun `formatNavNote should return null if action is null`() {
        val action = null
        assertThat(formatActionsForTimeline.formatNavNote(action)).isNull()
    }

    @Test
    fun `formatNavStatus should return formatted string`() {
        val action: ActionStatusEntity = NavActionStatusMock.createActionStatusEntity()
        assertThat(formatActionsForTimeline.formatNavStatus(action)).isEqualTo("12:00 - Navigation - observations")
    }

    @Test
    fun `formatNavStatus should return formatted string without observations`() {
        val action: ActionStatusEntity = NavActionStatusMock.createActionStatusEntity(observations = null)
        assertThat(formatActionsForTimeline.formatNavStatus(action)).isEqualTo("12:00 - Navigation ")
    }

    @Test
    fun `formatNavStatus should return null if action is null`() {
        val action = null
        assertThat(formatActionsForTimeline.formatNavStatus(action)).isNull()
    }

    @Test
    fun `formatNavControl should return formatted string`() {
        val action: ActionControlEntity = NavActionControlMock.createActionControlEntity()
        assertThat(formatActionsForTimeline.formatNavControl(action)).isEqualTo("12:00 / 14:00 - Contrôle administratif ")
    }

    @Test
    fun `formatNavControl should return null if action is null`() {
        val action = null
        assertThat(formatActionsForTimeline.formatNavControl(action)).isNull()
    }


}
