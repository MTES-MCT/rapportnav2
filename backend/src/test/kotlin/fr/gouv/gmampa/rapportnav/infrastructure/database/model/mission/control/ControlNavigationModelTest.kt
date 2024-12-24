package fr.gouv.gmampa.rapportnav.infrastructure.database.model.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ControlNavigationModel::class])
class ControlNavigationModelTest {

    @Test
    fun `execute should retrieve hasBeenDone in control navigation entity`() {
        val hasBeenDone = false

        val controlNavigation = ControlNavigationModel(id = UUID.randomUUID())
        controlNavigation.hasBeenDone = hasBeenDone

        val response = controlNavigation.toControlNavigationEntity()
        assertThat(response).isNotNull()
        assertThat(response.hasBeenDone).isEqualTo(hasBeenDone)
    }

    @Test
    fun `execute should retrieve hasBeenDone in control navigation Model from Entity`() {
        val hasBeenDone = true

        val entity = ControlNavigationEntity(
            id = UUID.randomUUID(),
            missionId = 761,
            amountOfControls = 3,
            hasBeenDone = hasBeenDone,
            actionControlId = "myActionId"
        )

        val response = ControlNavigationModel.fromControlNavigationEntity(entity)
        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(entity.id)
        assertThat(response.missionId).isEqualTo(entity.missionId)
        assertThat(response.hasBeenDone).isEqualTo(entity.hasBeenDone)
        assertThat(response.actionControlId).isEqualTo(entity.actionControlId)
        assertThat(response.amountOfControls).isEqualTo(entity.amountOfControls)
    }
}