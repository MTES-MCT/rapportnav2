package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionSurveillanceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.SpeciesInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GroupActionByDate
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.MapEnvActionControlPlans
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.FormatActionsForTimeline
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.EncodeSpecialChars
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
import java.time.Instant
import java.time.LocalDate

@SpringBootTest(classes = [FormatActionsForTimeline::class, FormatDateTime::class, FormatGeoCoords::class, EncodeSpecialChars::class])
class FormatActionsForTimelineTests {

    @Autowired
    private lateinit var formatActionsForTimeline: FormatActionsForTimeline

    @MockBean
    private lateinit var groupActionByDate: GroupActionByDate

    @MockBean
    private lateinit var mapEnvActionControlPlans: MapEnvActionControlPlans

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
    private val navFreeNote =
        MissionActionEntity.NavAction(
            ActionMockFactory.create<ActionFreeNoteEntity>(
                additionalParams = mapOf(
                    "startDateTimeUtc" to Instant.parse("2022-01-02T12:06:00Z"),
                    "endDateTimeUtc" to null,
                    "observations" to "Largué, appareillé"
                )
            ).toNavActionEntity()
        )

    @BeforeEach
    fun setUp() {
        // Set up mock behavior for GroupActionByDate
        val groupActionByDateData: Map<LocalDate, List<MissionActionEntity>> = mapOf(
            LocalDate.of(2022, 1, 1) to listOf(envControl, fishControl, envSurveillance),
            LocalDate.of(2022, 1, 2) to listOf(navControl, navStatus, navFreeNote)
        )
        Mockito.`when`(groupActionByDate.execute(any())).thenReturn(groupActionByDateData)
        val mapEnvActionControlPlansData = ControlPlanMock().createListWithFirst()
        Mockito.`when`(mapEnvActionControlPlans.execute(any())).thenReturn(mapEnvActionControlPlansData)
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
                    "13:00 / 15:00 - Contrôle Environnement - AMP sans réglementation particulière",
                    "13:00 - Contrôle Pêche - (DD): 52.14,14.30 - Le Pi - LR 314 - Infractions: sans PV - RAS",
                    "13:00 / 15:00 - Surveillance Environnement - AMP sans réglementation particulière",
                ),
                LocalDate.of(2022, 1, 2) to listOf(
                    "13:00 / 15:00 - Contrôle administratif ",
                    "13:00 - Navigation - observations",
                    "13:06 - Largué, appareillé"
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
                    TimelineActionItem(observations = "13:00 / 15:00 - Contrôle Environnement - AMP sans réglementation particulière"),
                    TimelineActionItem(observations = "13:00 - Contrôle Pêche - (DD): 52.14,14.30 - Le Pi - LR 314 - Infractions: sans PV - RAS"),
                    TimelineActionItem(observations = "13:00 / 15:00 - Surveillance Environnement - AMP sans réglementation particulière"),
                )
            ),
            TimelineActions(
                date = "2022-01-02",
                freeNote = listOf<TimelineActionItem>(
                    TimelineActionItem(observations = "13:00 / 15:00 - Contrôle administratif "),
                    TimelineActionItem(observations = "13:00 - Navigation - observations"),
                    TimelineActionItem(observations = "13:06 - Largué, appareillé"),
                )
            ),
        )
        assertThat(formatActionsForTimeline.formatForRapportNav1(data)).isEqualTo(expected)
    }

    @Test
    fun `formatEnvControl should return basic formatted string`() {
        val action: EnvActionControlEntity = EnvActionControlMock.create()
        assertThat(formatActionsForTimeline.formatEnvControl(action)).isEqualTo("13:00 / 15:00 - Contrôle Environnement - AMP sans réglementation particulière")
    }

    @Test
    fun `formatEnvSurveillance should return basic formatted string`() {
        val action: EnvActionSurveillanceEntity = EnvActionSurveillanceMock.create()
        assertThat(formatActionsForTimeline.formatEnvSurveillance(action)).isEqualTo("13:00 / 15:00 - Surveillance Environnement - AMP sans réglementation particulière")
    }

    @Test
    fun `formatEnvControl should return formatted string with facade`() {
        val action: EnvActionControlEntity = EnvActionControlMock.create(facade = "Golfe de Gascogne")
        assertThat(formatActionsForTimeline.formatEnvControl(action)).isEqualTo("13:00 / 15:00 - Contrôle Environnement - Golfe de Gascogne - AMP sans réglementation particulière")
    }

    @Test
    fun `formatEnvControl should return formatted string with themes`() {
        val controlPlans = EnvActionControlPlanMock().createList()
        val action: EnvActionControlEntity = EnvActionControlMock.create(controlPlans = controlPlans)
        assertThat(formatActionsForTimeline.formatEnvControl(action)).isEqualTo("13:00 / 15:00 - Contrôle Environnement - AMP sans réglementation particulière")
    }

    @Test
    fun `formatEnvControl should return formatted string with amount of controls`() {
        val action: EnvActionControlEntity = EnvActionControlMock.create(actionNumberOfControls = 2)
        assertThat(formatActionsForTimeline.formatEnvControl(action)).isEqualTo("13:00 / 15:00 - Contrôle Environnement - AMP sans réglementation particulière - 2 contrôle(s)")
    }

    @Test
    fun `formatEnvControl should return the complete formatted string`() {
        val action: EnvActionControlEntity = EnvActionControlMock.create(
            actionNumberOfControls = 2,
            facade = "Golfe de Gascogne",
            controlPlans = listOf(EnvActionControlPlanMock().controlPlan1)
        )
        assertThat(formatActionsForTimeline.formatEnvControl(action)).isEqualTo("13:00 / 15:00 - Contrôle Environnement - Golfe de Gascogne - AMP sans réglementation particulière - 2 contrôle(s)")
    }

    @Test
    fun `formatEnvControl should return null if action is null`() {
        val action = null
        assertThat(formatActionsForTimeline.formatEnvControl(action)).isNull()
    }

    @Test
    fun `formatFishControl should return the formatted string`() {
        val action: MissionAction = FishActionControlMock.create()
        assertThat(formatActionsForTimeline.formatFishControl(action)).isEqualTo("13:00 - Contrôle Pêche - (DD): 52.14,14.30 - Le Pi - LR 314 - Infractions: sans PV - RAS")
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
        assertThat(formatActionsForTimeline.formatFishControl(action)).isEqualTo("13:00 - Contrôle Pêche - (DD): 52.14,14.30 - Le Pi - LR 314 - Infractions: 1 PV - NATINF: 123 + 456")
    }

    @Test
    fun `formatFishControl should return null if action is null`() {
        val action = null
        assertThat(formatActionsForTimeline.formatFishControl(action)).isNull()
    }

    @Test
    fun `formatNavStatus should return formatted string`() {
        val action: ActionStatusEntity = NavActionStatusMock.createActionStatusEntity()
        assertThat(formatActionsForTimeline.formatNavStatus(action)).isEqualTo("13:00 - Navigation - observations")
    }

    @Test
    fun `formatNavStatus should return formatted observations`() {
        val action: ActionStatusEntity = NavActionStatusMock.createActionStatusEntity(
            observations = "3 adultes & 2 enfants <> RAS"
        )
        assertThat(formatActionsForTimeline.formatNavStatus(action)).isEqualTo("13:00 - Navigation - 3 adultes &amp; 2 enfants &lt;&gt; RAS")
    }

    @Test
    fun `formatNavStatus should return formatted string without observations`() {
        val action: ActionStatusEntity = NavActionStatusMock.createActionStatusEntity(observations = null)
        assertThat(formatActionsForTimeline.formatNavStatus(action)).isEqualTo("13:00 - Navigation ")
    }

    @Test
    fun `formatNavStatus should return null if action is null`() {
        val action = null
        assertThat(formatActionsForTimeline.formatNavStatus(action)).isNull()
    }

    @Test
    fun `formatNavControl should return formatted string`() {
        val action: ActionControlEntity = NavActionControlMock.createActionControlEntity()
        assertThat(formatActionsForTimeline.formatNavControl(action)).isEqualTo("13:00 / 15:00 - Contrôle administratif ")
    }

    @Test
    fun `formatNavControl should return null if action is null`() {
        val action = null
        assertThat(formatActionsForTimeline.formatNavControl(action)).isNull()
    }

    @Test
    fun `formatNavNote should return formatted string`() {
        val action = MissionActionEntity.NavAction(
            ActionMockFactory.create<ActionFreeNoteEntity>(
                observations = "Largué, appareillé",
                endDateTimeUtc = null
            ).toNavActionEntity()
        )
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("13:00 - Largué, appareillé")
    }

    @Test
    fun `formatNavActionCommon for IllegalImmigration should return a correctly formatted string`() {
        val action = MissionActionEntity.NavAction(
            ActionMockFactory.create<ActionIllegalImmigrationEntity>().toNavActionEntity()
        )
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("13:00 / 15:00 - Lutte contre l'immigration illégale")
    }

    @Test
    fun `formatNavActionCommon for Representation should return a correctly formatted string`() {
        val action = MissionActionEntity.NavAction(
            ActionMockFactory.create<ActionRepresentationEntity>().toNavActionEntity()
        )
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("13:00 / 15:00 - Représentation")
    }

    @Test
    fun `formatNavActionCommon for Vigimer should return a correctly formatted string`() {
        val action = MissionActionEntity.NavAction(
            ActionMockFactory.create<ActionVigimerEntity>().toNavActionEntity()
        )
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("13:00 / 15:00 - Permanence Vigimer")
    }

    @Test
    fun `formatNavActionCommon for BAAEM should return a correctly formatted string`() {
        val action = MissionActionEntity.NavAction(
            ActionMockFactory.create<ActionBAAEMPermanenceEntity>().toNavActionEntity()
        )
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("13:00 / 15:00 - Permanence BAAEM")
    }


    @Test
    fun `formatNavActionCommon for PublicOrder should return a correctly formatted string`() {
        val action = MissionActionEntity.NavAction(
            ActionMockFactory.create<ActionPublicOrderEntity>().toNavActionEntity()
        )
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("13:00 / 15:00 - Maintien de l'ordre public")
    }

    @Test
    fun `formatNavActionCommon for NauticalEvent should return a correctly formatted string`() {
        val action = MissionActionEntity.NavAction(
            ActionMockFactory.create<ActionNauticalEventEntity>(
                observations = "RAS"
            ).toNavActionEntity()
        )
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("13:00 / 15:00 - Sécu de manifestation nautique - RAS")
    }

    @Test
    fun `formatNavActionCommon for Rescue should return a correctly formatted string`() {
        val action = MissionActionEntity.NavAction(
            ActionMockFactory.create<ActionRescueEntity>(
                observations = "RAS"
            ).toNavActionEntity()
        )
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("13:00 / 15:00 - Assistance et sauvetage - RAS")
    }


    @Test
    fun `formatNavActionCommon for format special chars in observations`() {
        val action = MissionActionEntity.NavAction(
            ActionMockFactory.create<ActionRescueEntity>(
                observations = "3 adultes & 2 enfants <> RAS"
            ).toNavActionEntity()
        )
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("13:00 / 15:00 - Assistance et sauvetage - 3 adultes &amp; 2 enfants &lt;&gt; RAS")
    }


}
