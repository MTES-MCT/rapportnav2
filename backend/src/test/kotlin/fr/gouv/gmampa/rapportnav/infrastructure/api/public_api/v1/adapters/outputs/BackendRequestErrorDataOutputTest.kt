package fr.gouv.gmampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs

import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestErrorCode
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs.BackendRequestErrorDataOutput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BackendRequestErrorDataOutputTest {

    @Test
    fun `should create with code only`() {
        val output = BackendRequestErrorDataOutput(code = BackendRequestErrorCode.BODY_MISSING_DATA)
        assertThat(output.code).isEqualTo(BackendRequestErrorCode.BODY_MISSING_DATA)
        assertThat(output.data).isNull()
        assertThat(output.message).isNull()
    }

    @Test
    fun `should create with all fields`() {
        val output = BackendRequestErrorDataOutput(
            code = BackendRequestErrorCode.BODY_MISSING_DATA,
            data = listOf("field1", "field2"),
            message = "Missing required fields"
        )
        assertThat(output.code).isEqualTo(BackendRequestErrorCode.BODY_MISSING_DATA)
        assertThat(output.data).isEqualTo(listOf("field1", "field2"))
        assertThat(output.message).isEqualTo("Missing required fields")
    }

    @Test
    fun `should support equality based on data class`() {
        val a = BackendRequestErrorDataOutput(code = BackendRequestErrorCode.BODY_MISSING_DATA, message = "err")
        val b = BackendRequestErrorDataOutput(code = BackendRequestErrorCode.BODY_MISSING_DATA, message = "err")
        assertThat(a).isEqualTo(b)
    }
}