package fr.gouv.gmampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs

import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs.AuthLoginDataOutput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AuthLoginDataOutputTest {

    @Test
    fun `should store token`() {
        val output = AuthLoginDataOutput(token = "jwt-abc-123")
        assertThat(output.token).isEqualTo("jwt-abc-123")
    }

    @Test
    fun `should support equality based on data class`() {
        val a = AuthLoginDataOutput(token = "same")
        val b = AuthLoginDataOutput(token = "same")
        assertThat(a).isEqualTo(b)
    }
}