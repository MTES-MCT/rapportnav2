package fr.gouv.gmampa.rapportnav.infrastructure.api

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.api.ControllersExceptionHandler
import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestErrorCode
import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class ControllersExceptionHandlerTest {

    private lateinit var handler: ControllersExceptionHandler

    companion object {
        private val PROBLEM_JSON_MEDIA_TYPE = MediaType.parseMediaType("application/problem+json")
    }

    @BeforeEach
    fun setUp() {
        handler = ControllersExceptionHandler()
    }

    @Nested
    inner class HandleBackendUsageException {

        @Test
        fun `should return HTTP 400 status`() {
            val exception = BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "Resource not found"
            )

            val response = handler.handleBackendUsageException(exception)

            assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        }

        @Test
        fun `should return application problem json content type`() {
            val exception = BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "Resource not found"
            )

            val response = handler.handleBackendUsageException(exception)

            assertThat(response.headers.contentType).isEqualTo(PROBLEM_JSON_MEDIA_TYPE)
        }

        @Test
        fun `should include error code in type URI`() {
            val exception = BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "Resource not found"
            )

            val response = handler.handleBackendUsageException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.type.toString())
                .isEqualTo("urn:rapportnav:error:usage:COULD_NOT_FIND_EXCEPTION")
        }

        @Test
        fun `should include code extension property for backwards compatibility`() {
            val exception = BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "Resource not found"
            )

            val response = handler.handleBackendUsageException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.properties).containsKey("code")
            assertThat(problemDetail.properties!!["code"]).isEqualTo("COULD_NOT_FIND_EXCEPTION")
        }

        @Test
        fun `should include data extension property when provided`() {
            val customData = mapOf("id" to 123, "name" to "test")
            val exception = BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "Resource not found",
                data = customData
            )

            val response = handler.handleBackendUsageException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.properties).containsKey("data")
            assertThat(problemDetail.properties!!["data"]).isEqualTo(customData)
        }

        @Test
        fun `should not include data property when not provided`() {
            val exception = BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "Resource not found"
            )

            val response = handler.handleBackendUsageException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.properties).doesNotContainKey("data")
        }

        @Test
        fun `should use provided message as detail`() {
            val exception = BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "Custom error message"
            )

            val response = handler.handleBackendUsageException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.detail).isEqualTo("Custom error message")
        }

        @Test
        fun `should use default message when none provided`() {
            val exception = BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION
            )

            val response = handler.handleBackendUsageException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.detail).isEqualTo("The requested resource could not be found")
        }

        @Test
        fun `should have human readable title`() {
            val exception = BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION
            )

            val response = handler.handleBackendUsageException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.title).isEqualTo("Resource Not Found")
        }
    }

    @Nested
    inner class HandleBackendRequestException {

        @Test
        fun `should return HTTP 422 status`() {
            val exception = BackendRequestException(
                code = BackendRequestErrorCode.BODY_MISSING_DATA,
                message = "Missing required data"
            )

            val response = handler.handleBackendRequestException(exception)

            assertThat(response.statusCode).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
        }

        @Test
        fun `should return application problem json content type`() {
            val exception = BackendRequestException(
                code = BackendRequestErrorCode.BODY_MISSING_DATA,
                message = "Missing required data"
            )

            val response = handler.handleBackendRequestException(exception)

            assertThat(response.headers.contentType).isEqualTo(PROBLEM_JSON_MEDIA_TYPE)
        }

        @Test
        fun `should include error code in type URI`() {
            val exception = BackendRequestException(
                code = BackendRequestErrorCode.BODY_MISSING_DATA,
                message = "Missing required data"
            )

            val response = handler.handleBackendRequestException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.type.toString())
                .isEqualTo("urn:rapportnav:error:request:BODY_MISSING_DATA")
        }

        @Test
        fun `should include code extension property for backwards compatibility`() {
            val exception = BackendRequestException(
                code = BackendRequestErrorCode.BODY_MISSING_DATA,
                message = "Missing required data"
            )

            val response = handler.handleBackendRequestException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.properties).containsKey("code")
            assertThat(problemDetail.properties!!["code"]).isEqualTo("BODY_MISSING_DATA")
        }

        @Test
        fun `should include data extension property when provided`() {
            val customData = mapOf("field" to "name")
            val exception = BackendRequestException(
                code = BackendRequestErrorCode.BODY_MISSING_DATA,
                message = "Missing required data",
                data = customData
            )

            val response = handler.handleBackendRequestException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.properties).containsKey("data")
            assertThat(problemDetail.properties!!["data"]).isEqualTo(customData)
        }
    }

    @Nested
    inner class HandleBackendInternalException {

        @Test
        fun `should return HTTP 500 status`() {
            val exception = BackendInternalException(
                message = "Internal error occurred"
            )

            val response = handler.handleBackendInternalException(exception)

            assertThat(response.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        }

        @Test
        fun `should return application problem json content type`() {
            val exception = BackendInternalException(
                message = "Internal error occurred"
            )

            val response = handler.handleBackendInternalException(exception)

            assertThat(response.headers.contentType).isEqualTo(PROBLEM_JSON_MEDIA_TYPE)
        }

        @Test
        fun `should include INTERNAL_ERROR in type URI`() {
            val exception = BackendInternalException(
                message = "Internal error occurred"
            )

            val response = handler.handleBackendInternalException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.type.toString())
                .isEqualTo("urn:rapportnav:error:internal:INTERNAL_ERROR")
        }

        @Test
        fun `should include exception type in properties`() {
            val exception = BackendInternalException(
                message = "Internal error occurred"
            )

            val response = handler.handleBackendInternalException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.properties).containsKey("exception")
            assertThat(problemDetail.properties!!["exception"]).isEqualTo("BackendInternalException")
        }

        @Test
        fun `should include cause when original exception provided`() {
            val originalException = RuntimeException("Original cause")
            val exception = BackendInternalException(
                message = "Internal error occurred",
                originalException = originalException
            )

            val response = handler.handleBackendInternalException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.properties).containsKey("cause")
            assertThat(problemDetail.properties!!["cause"]).isEqualTo("Original cause")
        }

        @Test
        fun `should have Internal Server Error title`() {
            val exception = BackendInternalException(
                message = "Internal error occurred"
            )

            val response = handler.handleBackendInternalException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.title).isEqualTo("Internal Server Error")
        }
    }

    @Nested
    inner class HandleUnexpectedException {

        @Test
        fun `should return HTTP 500 status`() {
            val exception = RuntimeException("Something went wrong")

            val response = handler.handleUnexpectedException(exception)

            assertThat(response.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        }

        @Test
        fun `should return application problem json content type`() {
            val exception = RuntimeException("Something went wrong")

            val response = handler.handleUnexpectedException(exception)

            assertThat(response.headers.contentType).isEqualTo(PROBLEM_JSON_MEDIA_TYPE)
        }

        @Test
        fun `should include UNEXPECTED_ERROR in type URI`() {
            val exception = RuntimeException("Something went wrong")

            val response = handler.handleUnexpectedException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.type.toString())
                .isEqualTo("urn:rapportnav:error:internal:UNEXPECTED_ERROR")
        }

        @Test
        fun `should include exception type in properties`() {
            val exception = RuntimeException("Something went wrong")

            val response = handler.handleUnexpectedException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.properties).containsKey("exception")
            assertThat(problemDetail.properties!!["exception"]).isEqualTo("RuntimeException")
        }

        @Test
        fun `should use default message when exception has no message`() {
            val exception = RuntimeException()

            val response = handler.handleUnexpectedException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.detail).isEqualTo("An unexpected error occurred")
        }

        @Test
        fun `should have Unexpected Error title`() {
            val exception = RuntimeException("Something went wrong")

            val response = handler.handleUnexpectedException(exception)
            val problemDetail = response.body!!

            assertThat(problemDetail.title).isEqualTo("Unexpected Error")
        }
    }
}
