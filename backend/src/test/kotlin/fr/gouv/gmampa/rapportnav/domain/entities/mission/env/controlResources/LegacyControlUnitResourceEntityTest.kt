package fr.gouv.gmampa.rapportnav.domain.entities.mission.env.controlResources

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LegacyControlUnitResourceEntityTest {

    @Test
    fun `equals should be true when same id`() {
        val a = LegacyControlUnitResourceEntity(id = 1, controlUnitId = 10, name = "Resource A")
        val b = LegacyControlUnitResourceEntity(id = 1, controlUnitId = 20, name = "Resource B")
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `equals should be false when different id`() {
        val a = LegacyControlUnitResourceEntity(id = 1, name = "Same name")
        val b = LegacyControlUnitResourceEntity(id = 2, name = "Same name")
        assertThat(a).isNotEqualTo(b)
    }

    @Test
    fun `hashCode should be consistent with equals`() {
        val a = LegacyControlUnitResourceEntity(id = 5)
        val b = LegacyControlUnitResourceEntity(id = 5)
        assertThat(a.hashCode()).isEqualTo(b.hashCode())
    }
}