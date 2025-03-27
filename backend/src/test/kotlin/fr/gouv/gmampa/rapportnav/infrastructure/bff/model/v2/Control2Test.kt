package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ControlEntity2
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Control2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
class Control2Test {
    @Test
    fun `execute should convert into entity`() {
        val targetId = UUID.randomUUID()
        val input = Control2(
            targetId = targetId,
            controlType = ControlType.NAVIGATION,
            observations = "My beautiful observation",
            amountOfControls = 2,
            infractions = listOf(),
            hasBeenDone = true,
            nbrOfHours = 76,
            compliantOperatingPermit = ControlResult.YES,
            upToDateNavigationPermit = ControlResult.NO,
            compliantSecurityDocuments = ControlResult.NOT_CONTROLLED,
            knowledgeOfFrenchLawAndLanguage = ControlResult.YES,
            staffOutnumbered = ControlResult.NO,
            upToDateMedicalCheck = ControlResult.NOT_CONTROLLED
        )
        val entity = input.toControlEntity()

        assertThat(entity).isNotNull()
        assertThat(entity.id).isNotNull()
        assertThat(entity.infractions).isEmpty()
        assertThat(entity.nbrOfHours).isEqualTo(input.nbrOfHours)
        assertThat(entity.hasBeenDone).isEqualTo(input.hasBeenDone)
        assertThat(entity.compliantOperatingPermit).isEqualTo(input.compliantOperatingPermit)
        assertThat(entity.upToDateNavigationPermit).isEqualTo(input.upToDateNavigationPermit)
        assertThat(entity.compliantSecurityDocuments).isEqualTo(input.compliantSecurityDocuments)
        assertThat(entity.knowledgeOfFrenchLawAndLanguage).isEqualTo(input.knowledgeOfFrenchLawAndLanguage)
        assertThat(entity.staffOutnumbered).isEqualTo(input.staffOutnumbered)
        assertThat(entity.upToDateMedicalCheck).isEqualTo(input.upToDateMedicalCheck)
    }


    @Test
    fun `execute should convert from entity`() {
        val targetId = UUID.randomUUID()
        val entity = ControlEntity2(
            id = UUID.randomUUID(),
            targetId = targetId,
            controlType = ControlType.NAVIGATION,
            observations = "My beautiful observation",
            amountOfControls = 2,
            infractions = listOf(),
            hasBeenDone = true,
            nbrOfHours = 76,
            compliantOperatingPermit = ControlResult.YES,
            upToDateNavigationPermit = ControlResult.NO,
            compliantSecurityDocuments = ControlResult.NOT_CONTROLLED,
            knowledgeOfFrenchLawAndLanguage = ControlResult.YES,
            staffOutnumbered = ControlResult.NO,
            upToDateMedicalCheck = ControlResult.NOT_CONTROLLED,
        )
        val value = Control2.fromControlEntity(entity = entity)

        assertThat(value).isNotNull()
        assertThat(value.id).isNotNull()
        assertThat(value.infractions).isEmpty()
        assertThat(value.nbrOfHours).isEqualTo(entity.nbrOfHours)
        assertThat(value.hasBeenDone).isEqualTo(entity.hasBeenDone)
        assertThat(value.compliantOperatingPermit).isEqualTo(entity.compliantOperatingPermit)
        assertThat(value.upToDateNavigationPermit).isEqualTo(entity.upToDateNavigationPermit)
        assertThat(value.compliantSecurityDocuments).isEqualTo(entity.compliantSecurityDocuments)
        assertThat(value.knowledgeOfFrenchLawAndLanguage).isEqualTo(entity.knowledgeOfFrenchLawAndLanguage)
        assertThat(value.staffOutnumbered).isEqualTo(entity.staffOutnumbered)
        assertThat(value.upToDateMedicalCheck).isEqualTo(entity.upToDateMedicalCheck)
    }
}
