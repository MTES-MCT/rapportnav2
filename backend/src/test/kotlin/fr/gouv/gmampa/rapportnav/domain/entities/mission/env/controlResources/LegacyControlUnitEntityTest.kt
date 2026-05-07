package fr.gouv.gmampa.rapportnav.domain.entities.mission.env.controlResources

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LegacyControlUnitEntityTest {

    @Test
    fun `equals should be true when same id`() {
        val a = LegacyControlUnitEntity(id = 1, name = "Unit A", administration = "Admin A")
        val b = LegacyControlUnitEntity(id = 1, name = "Unit B", administration = "Admin B")
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `equals should be false when different id`() {
        val a = LegacyControlUnitEntity(id = 1, name = "Same")
        val b = LegacyControlUnitEntity(id = 2, name = "Same")
        assertThat(a).isNotEqualTo(b)
    }

    @Test
    fun `hashCode should be consistent with equals`() {
        val a = LegacyControlUnitEntity(id = 7)
        val b = LegacyControlUnitEntity(id = 7)
        assertThat(a.hashCode()).isEqualTo(b.hashCode())
    }
}