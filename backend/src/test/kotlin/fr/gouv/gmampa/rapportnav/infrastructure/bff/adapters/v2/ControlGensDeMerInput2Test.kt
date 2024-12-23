package fr.gouv.gmampa.rapportnav.infrastructure.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.ControlGensDeMerInput2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class ControlGensDeMerInput2Test {
    @Test
    fun `execute should convert into entity`() {
        val input = ControlGensDeMerInput2(
            observations = "My beautiful observation",
            amountOfControls = 2,
            unitShouldConfirm = false,
            unitHasConfirmed = true,
            infractions = listOf(),
            hasBeenDone = true,
            knowledgeOfFrenchLawAndLanguage = ControlResult.YES,
            staffOutnumbered = ControlResult.NO,
            upToDateMedicalCheck = ControlResult.NOT_CONTROLLED
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
        assertThat(entity.knowledgeOfFrenchLawAndLanguage).isEqualTo(ControlResult.YES)
        assertThat(entity.staffOutnumbered).isEqualTo(ControlResult.NO)
        assertThat(entity.upToDateMedicalCheck).isEqualTo(ControlResult.NOT_CONTROLLED)
    }
}
