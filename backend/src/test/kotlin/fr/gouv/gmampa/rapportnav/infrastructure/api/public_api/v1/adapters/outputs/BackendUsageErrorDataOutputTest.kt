package fr.gouv.gmampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs.BackendUsageErrorDataOutput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BackendUsageErrorDataOutputTest {

    @Test
    fun `should create with code only`() {
        val output = BackendUsageErrorDataOutput(code = BackendUsageErrorCode.USER_NOT_FOUND_EXCEPTION)
        assertThat(output.code).isEqualTo(BackendUsageErrorCode.USER_NOT_FOUND_EXCEPTION)
        assertThat(output.data).isNull()
        assertThat(output.message).isNull()
    }

    @Test
    fun `should create with all fields`() {
        val output = BackendUsageErrorDataOutput(
            code = BackendUsageErrorCode.ALREADY_EXISTS_EXCEPTION,
            data = mapOf("id" to 42),
            message = "Resource already exists"
        )
        assertThat(output.code).isEqualTo(BackendUsageErrorCode.ALREADY_EXISTS_EXCEPTION)
        assertThat(output.data).isEqualTo(mapOf("id" to 42))
        assertThat(output.message).isEqualTo("Resource already exists")
    }

    @Test
    fun `should support equality based on data class`() {
        val a = BackendUsageErrorDataOutput(code = BackendUsageErrorCode.INVALID_TOKEN_EXCEPTION)
        val b = BackendUsageErrorDataOutput(code = BackendUsageErrorCode.INVALID_TOKEN_EXCEPTION)
        assertThat(a).isEqualTo(b)
    }
}