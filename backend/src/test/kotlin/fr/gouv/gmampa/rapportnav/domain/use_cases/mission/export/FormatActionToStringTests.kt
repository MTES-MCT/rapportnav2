package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.SpeciesControl
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.SpeciesInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionFreeNoteEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.FormatActionToString
import fr.gouv.gmampa.rapportnav.mocks.mission.action.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

@SpringBootTest(classes = [FormatActionToString::class])
class FormatActionToStringTests {

    @Autowired
    private lateinit var formatActionToString: FormatActionToString

    @Test
    fun `formatTime should return formatted time`() {
        assertThat(
            formatActionToString.formatTime(
                ZonedDateTime.of(
                    LocalDateTime.of(2022, 1, 2, 12, 0),
                    ZoneOffset.UTC
                )
            )
        ).isEqualTo("12:00")
    }

    @Test
    fun `formatTime should return NA when null`() {
        assertThat(formatActionToString.formatTime(null)).isEqualTo("N/A")
    }

    @Test
    fun `formatEnvControl should return basic formatted string`() {
        val action: EnvActionControlEntity = EnvActionControlMock.create()
        assertThat(formatActionToString.formatEnvControl(action)).isEqualTo("12:00 / 14:00 - Contrôle Environnement")
    }

    @Test
    fun `formatEnvControl should return formatted string with facade`() {
        val action: EnvActionControlEntity = EnvActionControlMock.create(facade = "Golfe de Gascogne")
        assertThat(formatActionToString.formatEnvControl(action)).isEqualTo("12:00 / 14:00 - Contrôle Environnement - Golfe de Gascogne")
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
        assertThat(formatActionToString.formatEnvControl(action)).isEqualTo("12:00 / 14:00 - Contrôle Environnement - Rejets illicites + Natura 2000")
    }

    @Test
    fun `formatEnvControl should return formatted string with amount of controls`() {
        val action: EnvActionControlEntity = EnvActionControlMock.create(actionNumberOfControls = 2)
        assertThat(formatActionToString.formatEnvControl(action)).isEqualTo("12:00 / 14:00 - Contrôle Environnement - 2 contrôles")
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
        assertThat(formatActionToString.formatEnvControl(action)).isEqualTo("12:00 / 14:00 - Contrôle Environnement - Golfe de Gascogne - Rejets illicites - 2 contrôles")
    }

    @Test
    fun `formatEnvControl should return null if action is null`() {
        val action = null
        assertThat(formatActionToString.formatEnvControl(action)).isNull()
    }

    @Test
    fun `formatFishControl should return the formatted string`() {
        val action: MissionAction = FishActionControlMock.create()
        assertThat(formatActionToString.formatFishControl(action)).isEqualTo("12:00 - Contrôle Pêche - 52.14/14.3 - Le Pi - 314 - Espèces contrôlées: N/A - Infractions: sans PV - RAS")
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
        assertThat(formatActionToString.formatFishControl(action)).isEqualTo("12:00 - Contrôle Pêche - 52.14/14.3 - Le Pi - 314 - Espèces contrôlées: N/A - Infractions: avec PV - NATINF: 123 + 456")
    }

    @Test
    fun `formatFishControl should return the complete formatted string with species`() {
        val action: MissionAction = FishActionControlMock.create(
            speciesOnboard = listOf(
                SpeciesControl().apply {
                    speciesCode = "COD"
                    declaredWeight = 12.2
                    controlledWeight = null
                },
                SpeciesControl().apply {
                    speciesCode = "SOL"
                    declaredWeight = 12.2
                    controlledWeight = 14.2
                }
            )
        )
        assertThat(formatActionToString.formatFishControl(action)).isEqualTo("12:00 - Contrôle Pêche - 52.14/14.3 - Le Pi - 314 - Espèces contrôlées: COD: N/A/12.2 kg - SOL: 14.2/12.2 kg - Infractions: sans PV - RAS")
    }

    @Test
    fun `formatFishControl should return null if action is null`() {
        val action = null
        assertThat(formatActionToString.formatFishControl(action)).isNull()
    }

    @Test
    fun `formatNavNote should return formatted string`() {
        val action: ActionFreeNoteEntity = NavActionFreeNoteMock.create()
        assertThat(formatActionToString.formatNavNote(action)).isEqualTo("12:06 - Largué, appareillé")
    }

    @Test
    fun `formatNavNote should return null if action is null`() {
        val action = null
        assertThat(formatActionToString.formatNavNote(action)).isNull()
    }

    @Test
    fun `formatNavStatus should return formatted string`() {
        val action: ActionStatusEntity = NavActionStatusMock.createActionStatusEntity()
        assertThat(formatActionToString.formatNavStatus(action)).isEqualTo("12:00 - Navigation - début - observations")
    }

    @Test
    fun `formatNavStatus should return formatted string without observations`() {
        val action: ActionStatusEntity = NavActionStatusMock.createActionStatusEntity(observations = null)
        assertThat(formatActionToString.formatNavStatus(action)).isEqualTo("12:00 - Navigation - début ")
    }

    @Test
    fun `formatNavStatus should return null if action is null`() {
        val action = null
        assertThat(formatActionToString.formatNavStatus(action)).isNull()
    }

    @Test
    fun `formatNavControl should return formatted string`() {
        val action: ActionControlEntity = NavActionControlMock.createActionControlEntity()
        assertThat(formatActionToString.formatNavControl(action)).isEqualTo("12:00 / 14:00 - Contrôle administratif ")
    }

    @Test
    fun `formatNavControl should return null if action is null`() {
        val action = null
        assertThat(formatActionToString.formatNavControl(action)).isNull()
    }


}
