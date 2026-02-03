package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.FishInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GroupActionByDate
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.FormatActionsForTimeline
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatGeoCoords
import fr.gouv.gmampa.rapportnav.mocks.mission.action.*
import fr.gouv.gmampa.rapportnav.mocks.mission.env.ThemeEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.time.LocalDate

@SpringBootTest(classes = [FormatActionsForTimeline::class, FormatDateTime::class, FormatGeoCoords::class])
class FormatActionsForTimelineTests {

    @Autowired
    private lateinit var formatActionsForTimeline: FormatActionsForTimeline

    @MockitoBean
    private lateinit var groupActionByDate: GroupActionByDate

    private val envControl = MissionEnvActionEntity.fromEnvAction(missionId=1, action=EnvActionControlMock.create())
    private val envSurveillance = MissionEnvActionEntity.fromEnvAction(missionId=1, action=EnvActionSurveillanceMock.create())

    private val fishControl = MissionFishActionEntity.fromFishAction(action = FishActionControlMock.create())

    private val navControl = MissionNavActionEntity.fromMissionActionModel(MissionActionModelMock.create(actionType = ActionType.CONTROL))
    private val navStatus = MissionNavActionEntity.fromMissionActionModel(MissionActionModelMock.create(actionType = ActionType.STATUS))
    private val navFreeNote = MissionNavActionEntity.fromMissionActionModel(MissionActionModelMock.create(actionType = ActionType.NOTE,
        startDateTimeUtc = Instant.parse("2022-01-02T12:06:00Z"), observations = "Largué, appareillé"))


    @BeforeEach
    fun setUp() {
        // Set up mock behavior for GroupActionByDate
        val groupActionByDateData: Map<LocalDate, List<MissionActionEntity>>? = mapOf(
            LocalDate.of(2022, 1, 1) to listOf(envControl, fishControl, envSurveillance),
            LocalDate.of(2022, 1, 2) to listOf(navControl, navStatus, navFreeNote)
        )
        Mockito.`when`(groupActionByDate.execute(any())).thenReturn(groupActionByDateData)
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
                    "13:00 / 15:00 - Contrôle Environnement - Pêche loisir (autre que PAP)",
                    "13:00 - Contrôle Pêche - en mer - (DD): 52.14,14.30 - Le Pi (AC 1435) - Infractions: sans PV - RAS",
                    "13:00 / 15:00 - Surveillance Environnement - Pêche loisir (autre que PAP)",
                ),
                LocalDate.of(2022, 1, 2) to listOf(
                    "23:00 / 02:00 - Contrôle administratif - FR-23566",
                    "23:00 - Mouillage - observations",
                    "13:06 - Largué, appareillé"
                )
            )
        )
    }

    @Test
    fun `formatEnvControl should return basic formatted string`() {
        assertThat(formatActionsForTimeline.formatEnvControl(envControl)).isEqualTo("13:00 / 15:00 - Contrôle Environnement - Pêche loisir (autre que PAP)")
    }

    @Test
    fun `formatEnvSurveillance should return basic formatted string`() {
        val action  = MissionEnvActionEntity.fromEnvAction(missionId=1, action=EnvActionSurveillanceMock.create(themes = listOf(ThemeEntityMock.create())))
        assertThat(formatActionsForTimeline.formatEnvSurveillance(action)).isEqualTo("13:00 / 15:00 - Surveillance Environnement - Pêche loisir (autre que PAP)")
    }

    @Test
    fun `formatEnvControl should return formatted string with facade`() {
        val action = MissionEnvActionEntity.fromEnvAction(missionId=1, action=EnvActionControlMock.create(facade = "Golfe de Gascogne"))
        assertThat(formatActionsForTimeline.formatEnvControl(action)).isEqualTo("13:00 / 15:00 - Contrôle Environnement - Golfe de Gascogne - Pêche loisir (autre que PAP)")
    }

    @Test
    fun `formatEnvControl should return formatted string with themes`() {
        assertThat(formatActionsForTimeline.formatEnvControl(envControl)).isEqualTo("13:00 / 15:00 - Contrôle Environnement - Pêche loisir (autre que PAP)")
    }

    @Test
    fun `formatEnvControl should return formatted string with amount of controls`() {
        val action = MissionEnvActionEntity.fromEnvAction(missionId=1, action=EnvActionControlMock.create(actionNumberOfControls = 2))
        assertThat(formatActionsForTimeline.formatEnvControl(action)).isEqualTo("13:00 / 15:00 - Contrôle Environnement - Pêche loisir (autre que PAP) - 2 contrôle(s)")
    }

    @Test
    fun `formatEnvControl should return the complete formatted string`() {
        val action = MissionEnvActionEntity.fromEnvAction(missionId=1, action=EnvActionControlMock.create(actionNumberOfControls = 2, facade = "Golfe de Gascogne",))
        assertThat(formatActionsForTimeline.formatEnvControl(action)).isEqualTo("13:00 / 15:00 - Contrôle Environnement - Golfe de Gascogne - Pêche loisir (autre que PAP) - 2 contrôle(s)")
    }

    @Test
    fun `formatEnvControl should return null if action is null`() {
        val action = null
        assertThat(formatActionsForTimeline.formatEnvControl(action)).isNull()
    }

    @Test
    fun `formatFishControl should return the formatted string for sea controls`() {
        assertThat(formatActionsForTimeline.formatFishControl(fishControl)).isEqualTo("13:00 - Contrôle Pêche - en mer - (DD): 52.14,14.30 - Le Pi (AC 1435) - Infractions: sans PV - RAS")
    }

    @Test
    fun `formatFishControl should return the formatted string for land controls`() {
        val action = MissionFishActionEntity.fromFishAction(action = FishActionControlMock.create(actionType = MissionActionType.LAND_CONTROL), )
        assertThat(formatActionsForTimeline.formatFishControl(action)).isEqualTo("13:00 - Contrôle Pêche - à terre - La Rochelle (LR) - Le Pi (AC 1435) - Infractions: sans PV - RAS")
    }

    @Test
    fun `formatFishControl should return the formatted string with natinfs`() {
        val action = MissionFishActionEntity.fromFishAction(action = FishActionControlMock.create(infractions = listOf(
            FishInfraction(
                infractionType = InfractionType.WITH_RECORD,
                natinf = 123
            ),
            FishInfraction(
                infractionType = InfractionType.WITHOUT_RECORD,
                natinf = 456
            )
        )), )
        assertThat(formatActionsForTimeline.formatFishControl(action)).isEqualTo("13:00 - Contrôle Pêche - en mer - (DD): 52.14,14.30 - Le Pi (AC 1435) - Infractions: 1 PV - NATINF: 123 + 456")
    }

    @Test
    fun `formatFishControl should return null if action is null`() {
        val action = null
        assertThat(formatActionsForTimeline.formatFishControl(action)).isNull()
    }

    @Test
    fun `formatNavStatus should return formatted string`() {
        val action = MissionNavActionEntity.fromMissionActionModel(MissionActionModelMock.create(actionType = ActionType.STATUS, observations = null))

        assertThat(formatActionsForTimeline.formatNavStatus(action)).isEqualTo("23:00 - Mouillage")
    }

    @Test
    fun `formatNavStatus should return formatted reason`() {
        val action = MissionNavActionEntity.fromMissionActionModel(MissionActionModelMock.create(actionType = ActionType.STATUS, reason = ActionStatusReason.MAINTENANCE.toString(), observations = null))
        assertThat(formatActionsForTimeline.formatNavStatus(action)).isEqualTo("23:00 - Mouillage - Maintenance")
    }

    @Test
    fun `formatNavStatus should return formatted observations`() {
        val action = MissionNavActionEntity.fromMissionActionModel(MissionActionModelMock.create(actionType = ActionType.STATUS, observations = "3 adultes & 2 enfants <> RAS"))
        assertThat(formatActionsForTimeline.formatNavStatus(action)).isEqualTo("23:00 - Mouillage - 3 adultes & 2 enfants <> RAS")
    }

    @Test
    fun `formatNavStatus should return null if action is null`() {
        val action = null
        assertThat(formatActionsForTimeline.formatNavStatus(action)).isNull()
    }

    @Test
    fun `formatNavControl should return formatted string`() {
        val action = MissionNavActionEntity.fromMissionActionModel(MissionActionModelMock.create(actionType = ActionType.CONTROL))
        assertThat(formatActionsForTimeline.formatNavControl(action)).isEqualTo("23:00 / 02:00 - Contrôle administratif - FR-23566")
    }

    @Test
    fun `formatNavControl should return null if action is null`() {
        val action = null
        assertThat(formatActionsForTimeline.formatNavControl(action)).isNull()
    }

    @Test
    fun `formatNavNote should return formatted string`() {
         assertThat(formatActionsForTimeline.formatNavAction(navFreeNote)).isEqualTo("13:06 - Largué, appareillé")
    }

    @Test
    fun `formatNavActionCommon for IllegalImmigration should return a correctly formatted string`() {
        val action = MissionNavActionEntity.fromMissionActionModel(MissionActionModelMock.create(actionType = ActionType.ILLEGAL_IMMIGRATION, observations = null))
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("23:00 / 02:00 - Lutte contre l'immigration illégale")
    }

    @Test
    fun `formatNavActionCommon for Representation should return a correctly formatted string`() {
        val action = MissionNavActionEntity.fromMissionActionModel(MissionActionModelMock.create(actionType = ActionType.REPRESENTATION, observations = null))
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("23:00 / 02:00 - Représentation")
    }

    @Test
    fun `formatNavActionCommon for Vigimer should return a correctly formatted string`() {
        val action = MissionNavActionEntity.fromMissionActionModel(MissionActionModelMock.create(actionType = ActionType.VIGIMER, observations = null))
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("23:00 / 02:00 - Permanence Vigimer")
    }

    @Test
    fun `formatNavActionCommon for BAAEM should return a correctly formatted string`() {
        val action = MissionNavActionEntity.fromMissionActionModel(MissionActionModelMock.create(actionType = ActionType.BAAEM_PERMANENCE, observations = null))
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("23:00 / 02:00 - Permanence BAAEM")
    }


    @Test
    fun `formatNavActionCommon for PublicOrder should return a correctly formatted string`() {
        val action = MissionNavActionEntity.fromMissionActionModel(MissionActionModelMock.create(actionType = ActionType.PUBLIC_ORDER, observations = null))
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("23:00 / 02:00 - Maintien de l'ordre public")
    }

    @Test
    fun `formatNavActionCommon for NauticalEvent should return a correctly formatted string`() {
        val action = MissionNavActionEntity.fromMissionActionModel(MissionActionModelMock.create(actionType = ActionType.NAUTICAL_EVENT, observations = "RAS"))
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("23:00 / 02:00 - Sécu de manifestation nautique - RAS")
    }

    @Test
    fun `formatNavActionCommon for Rescue should return a correctly formatted string`() {
        val action = MissionNavActionEntity.fromMissionActionModel(MissionActionModelMock.create(actionType = ActionType.RESCUE, observations = "RAS"))
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("23:00 / 02:00 - Assistance et sauvetage - RAS")
    }


    @Test
    fun `formatNavActionCommon for format special chars in observations`() {
        val action = MissionNavActionEntity.fromMissionActionModel(MissionActionModelMock.create(actionType = ActionType.RESCUE, observations = "3 adultes & 2 enfants <> RAS"))
        assertThat(formatActionsForTimeline.formatNavAction(action)).isEqualTo("23:00 / 02:00 - Assistance et sauvetage - 3 adultes & 2 enfants <> RAS")
    }


}