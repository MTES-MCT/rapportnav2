package fr.gouv.gmampa.rapportnav.domain.entities.mission.env.controlResources

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import fr.gouv.gmampa.rapportnav.mocks.mission.env.ControlUnitResourceEnvMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ControlResourceEntityTest {

    @Test
    fun `fromControlUnitResourceEnv should map id and name`() {
        val resourceEnv = ControlUnitResourceEnvMock.create(id = 42, name = "Vedette rapide")

        val result = ControlResourceEntity.fromControlUnitResourceEnv(resourceEnv)

        assertThat(result.id).isEqualTo(42)
        assertThat(result.name).isEqualTo("Vedette rapide")
    }
}