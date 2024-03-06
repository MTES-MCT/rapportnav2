package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionFreeNoteEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.FormatActionToString
import fr.gouv.gmampa.rapportnav.mocks.mission.action.EnvActionControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.NavActionControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.NavActionFreeNoteMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.NavActionStatusMock
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
    fun `formatEnvControl should return null if action is null`() {
        val action = null
        assertThat(formatActionToString.formatEnvControl(action)).isNull()
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
