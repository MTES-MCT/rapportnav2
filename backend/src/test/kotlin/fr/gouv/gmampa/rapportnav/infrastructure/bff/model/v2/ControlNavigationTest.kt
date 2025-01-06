package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.ControlNavigation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class ControlNavigationTest {
    @Test
    fun `execute should convert into entity`() {
        val input = ControlNavigation(
            observations = "My beautiful observation",
            amountOfControls = 2,
            unitShouldConfirm = false,
            unitHasConfirmed = true,
            infractions = listOf(),
            hasBeenDone = true
        )
        input.setMissionIdAndActionId(missionId = 761, actionId = "my action id")
        val entity = input.toEntity()

        assertThat(entity).isNotNull()
        assertThat(entity.id).isNotNull()
        assertThat(entity.missionId).isEqualTo(761)
        assertThat(entity.infractions).isEqualTo(null)
        assertThat(entity.actionControlId).isEqualTo("my action id")
        assertThat(entity.unitShouldConfirm).isEqualTo(false)
        assertThat(entity.unitHasConfirmed).isEqualTo(true)
        assertThat(entity.hasBeenDone).isEqualTo(true)
    }
}
