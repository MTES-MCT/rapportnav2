package fr.gouv.gmampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs

import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs.BackendInternalErrorDataOutput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BackendInternalErrorDataOutputTest {

    @Test
    fun `should use default message when none provided`() {
        val output = BackendInternalErrorDataOutput()
        assertThat(output.message).isEqualTo("An internal error occurred.")
        assertThat(output.exception).isNull()
        assertThat(output.cause).isNull()
    }

    @Test
    fun `should store custom values`() {
        val output = BackendInternalErrorDataOutput(
            message = "Something went wrong",
            exception = "IOException",
            cause = "Connection refused"
        )
        assertThat(output.message).isEqualTo("Something went wrong")
        assertThat(output.exception).isEqualTo("IOException")
        assertThat(output.cause).isEqualTo("Connection refused")
    }
}