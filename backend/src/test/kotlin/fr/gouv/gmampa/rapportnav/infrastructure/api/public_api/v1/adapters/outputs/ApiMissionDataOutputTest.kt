package fr.gouv.gmampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs

import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs.ApiMissionDataOutput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ApiMissionDataOutputTest {

    @Test
    fun `should create output with given values`() {
        val output = ApiMissionDataOutput(id = 5, containsActionsAddedByUnit = true)
        assertThat(output.id).isEqualTo(5)
        assertThat(output.containsActionsAddedByUnit).isTrue()
    }

    @Test
    fun `should support equality based on data class`() {
        val a = ApiMissionDataOutput(id = 1, containsActionsAddedByUnit = false)
        val b = ApiMissionDataOutput(id = 1, containsActionsAddedByUnit = false)
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `should not be equal when fields differ`() {
        val a = ApiMissionDataOutput(id = 1, containsActionsAddedByUnit = true)
        val b = ApiMissionDataOutput(id = 1, containsActionsAddedByUnit = false)
        assertThat(a).isNotEqualTo(b)
    }
}