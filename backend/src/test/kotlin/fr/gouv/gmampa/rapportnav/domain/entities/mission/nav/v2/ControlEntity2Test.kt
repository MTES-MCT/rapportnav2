package fr.gouv.gmampa.rapportnav.domain.entities.mission.nav.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ControlEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.v2.ControlModel2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.v2.InfractionModel2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
class ControlEntity2Test {

    @Test
    fun `execute should retrieve entity from Model`() {
        val model = ControlModel2(
            controlType = ControlType.NAVIGATION,
            amountOfControls = 1,
            hasBeenDone = false,
            observations = "My observations",
            staffOutnumbered = ControlResult.YES.toString(),
            upToDateMedicalCheck = ControlResult.NO.toString(),
            compliantOperatingPermit = ControlResult.YES.toString(),
            upToDateNavigationPermit = ControlResult.NOT_CONTROLLED.toString(),
            compliantSecurityDocuments = ControlResult.NOT_CONCERNED.toString(),
            knowledgeOfFrenchLawAndLanguage = ControlResult.YES.toString(),
            infractions = listOf(InfractionModel2(UUID.randomUUID()))
        )

        val entity = ControlEntity2.fromControlModel(model)
        assertThat(entity).isNotNull()
        assertThat(entity.id).isEqualTo(model.id)
        assertThat(entity.controlType).isEqualTo(model.controlType)
        assertThat(entity.amountOfControls).isEqualTo(model.amountOfControls)
        assertThat(entity.observations).isEqualTo(model.observations)
        assertThat(entity.hasBeenDone).isEqualTo(model.hasBeenDone)
        assertThat(entity.observations).isEqualTo(model.observations)
        assertThat(entity.staffOutnumbered.toString()).isEqualTo(model.staffOutnumbered)
        assertThat(entity.upToDateMedicalCheck.toString()).isEqualTo(model.upToDateMedicalCheck)
        assertThat(entity.compliantOperatingPermit.toString()).isEqualTo(model.compliantOperatingPermit)
        assertThat(entity.upToDateNavigationPermit.toString()).isEqualTo(model.upToDateNavigationPermit)
        assertThat(entity.compliantSecurityDocuments.toString()).isEqualTo(model.compliantSecurityDocuments)
        assertThat(entity.knowledgeOfFrenchLawAndLanguage.toString()).isEqualTo(model.knowledgeOfFrenchLawAndLanguage)
        assertThat(entity.infractions?.map { it.id }?.toSet()).isEqualTo(model.infractions?.map { it.id }?.toSet())
    }

    @Test
    fun `execute should convert entity from Model`() {
        val entity = ControlEntity2(
            id = UUID.randomUUID(),
            controlType = ControlType.NAVIGATION,
            amountOfControls = 1,
            hasBeenDone = false,
            observations = "My observations",
            staffOutnumbered = ControlResult.NO,
            upToDateMedicalCheck = ControlResult.NO,
            compliantOperatingPermit = ControlResult.YES,
            upToDateNavigationPermit = ControlResult.NO,
            compliantSecurityDocuments = ControlResult.NOT_CONCERNED,
            knowledgeOfFrenchLawAndLanguage = ControlResult.NOT_CONTROLLED,
            infractions = listOf(InfractionEntity2(id = UUID.randomUUID(), natinfs = listOf()))
        )

        val model = entity.toControlModel()
        assertThat(entity).isNotNull()
        assertThat(entity.id).isEqualTo(model.id)
        assertThat(entity.controlType).isEqualTo(model.controlType)
        assertThat(entity.amountOfControls).isEqualTo(model.amountOfControls)
        assertThat(entity.observations).isEqualTo(model.observations)
        assertThat(entity.hasBeenDone).isEqualTo(model.hasBeenDone)
        assertThat(entity.observations).isEqualTo(model.observations)
        assertThat(entity.staffOutnumbered.toString()).isEqualTo(model.staffOutnumbered)
        assertThat(entity.upToDateMedicalCheck.toString()).isEqualTo(model.upToDateMedicalCheck)
        assertThat(entity.compliantOperatingPermit.toString()).isEqualTo(model.compliantOperatingPermit)
        assertThat(entity.upToDateNavigationPermit.toString()).isEqualTo(model.upToDateNavigationPermit)
        assertThat(entity.compliantSecurityDocuments.toString()).isEqualTo(model.compliantSecurityDocuments)
        assertThat(entity.knowledgeOfFrenchLawAndLanguage.toString()).isEqualTo(model.knowledgeOfFrenchLawAndLanguage)
        assertThat(entity.infractions?.map { it.id }?.toSet()).isEqualTo(model.infractions?.map { it.id }?.toSet())
    }

}
