package fr.gouv.gmampa.rapportnav.infrastructure.api.adapters

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.ProblemDetailFactory
import fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.defaultMessage
import fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.toTitle
import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class ProblemDetailFactoryTest {

    @Nested
    inner class ForUsageError {

        @Test
        fun `should return HTTP 400 status`() {
            val problemDetail = ProblemDetailFactory.forUsageError(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION
            )

            assertThat(problemDetail.status).isEqualTo(HttpStatus.BAD_REQUEST.value())
        }

        @Test
        fun `should set type URI with usage category and code`() {
            val problemDetail = ProblemDetailFactory.forUsageError(
                code = BackendUsageErrorCode.ALREADY_EXISTS_EXCEPTION
            )

            assertThat(problemDetail.type.toString())
                .isEqualTo("urn:rapportnav:error:usage:ALREADY_EXISTS_EXCEPTION")
        }

        @Test
        fun `should set title from error code`() {
            val problemDetail = ProblemDetailFactory.forUsageError(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION
            )

            assertThat(problemDetail.title).isEqualTo("Resource Not Found")
        }

        @Test
        fun `should use provided message as detail`() {
            val problemDetail = ProblemDetailFactory.forUsageError(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "Custom message"
            )

            assertThat(problemDetail.detail).isEqualTo("Custom message")
        }

        @Test
        fun `should use default message when none provided`() {
            val problemDetail = ProblemDetailFactory.forUsageError(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION
            )

            assertThat(problemDetail.detail).isEqualTo("The requested resource could not be found")
        }

        @Test
        fun `should include code in properties`() {
            val problemDetail = ProblemDetailFactory.forUsageError(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION
            )

            assertThat(problemDetail.properties).containsEntry("code", "INVALID_PARAMETERS_EXCEPTION")
        }

        @Test
        fun `should include data in properties when provided`() {
            val data = mapOf("id" to 123)
            val problemDetail = ProblemDetailFactory.forUsageError(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                data = data
            )

            assertThat(problemDetail.properties).containsEntry("data", data)
        }

        @Test
        fun `should not include data in properties when not provided`() {
            val problemDetail = ProblemDetailFactory.forUsageError(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION
            )

            assertThat(problemDetail.properties).doesNotContainKey("data")
        }
    }

    @Nested
    inner class ForRequestError {

        @Test
        fun `should return HTTP 422 status`() {
            val problemDetail = ProblemDetailFactory.forRequestError(
                code = BackendRequestErrorCode.BODY_MISSING_DATA
            )

            assertThat(problemDetail.status).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value())
        }

        @Test
        fun `should set type URI with request category and code`() {
            val problemDetail = ProblemDetailFactory.forRequestError(
                code = BackendRequestErrorCode.BODY_MISSING_DATA
            )

            assertThat(problemDetail.type.toString())
                .isEqualTo("urn:rapportnav:error:request:BODY_MISSING_DATA")
        }

        @Test
        fun `should set title from error code`() {
            val problemDetail = ProblemDetailFactory.forRequestError(
                code = BackendRequestErrorCode.BODY_MISSING_DATA
            )

            assertThat(problemDetail.title).isEqualTo("Missing Request Data")
        }

        @Test
        fun `should use provided message as detail`() {
            val problemDetail = ProblemDetailFactory.forRequestError(
                code = BackendRequestErrorCode.BODY_MISSING_DATA,
                message = "Field 'name' is required"
            )

            assertThat(problemDetail.detail).isEqualTo("Field 'name' is required")
        }

        @Test
        fun `should use default message when none provided`() {
            val problemDetail = ProblemDetailFactory.forRequestError(
                code = BackendRequestErrorCode.BODY_MISSING_DATA
            )

            assertThat(problemDetail.detail).isEqualTo("The request body is missing required data")
        }

        @Test
        fun `should include code in properties`() {
            val problemDetail = ProblemDetailFactory.forRequestError(
                code = BackendRequestErrorCode.BODY_MISSING_DATA
            )

            assertThat(problemDetail.properties).containsEntry("code", "BODY_MISSING_DATA")
        }

        @Test
        fun `should include data in properties when provided`() {
            val data = listOf("field1", "field2")
            val problemDetail = ProblemDetailFactory.forRequestError(
                code = BackendRequestErrorCode.BODY_MISSING_DATA,
                data = data
            )

            assertThat(problemDetail.properties).containsEntry("data", data)
        }
    }

    @Nested
    inner class ForInternalError {

        @Test
        fun `should return HTTP 500 status`() {
            val problemDetail = ProblemDetailFactory.forInternalError(
                message = "Database connection failed"
            )

            assertThat(problemDetail.status).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
        }

        @Test
        fun `should set type URI with internal category`() {
            val problemDetail = ProblemDetailFactory.forInternalError(
                message = "Database connection failed"
            )

            assertThat(problemDetail.type.toString())
                .isEqualTo("urn:rapportnav:error:internal:INTERNAL_ERROR")
        }

        @Test
        fun `should set title to Internal Server Error`() {
            val problemDetail = ProblemDetailFactory.forInternalError(
                message = "Database connection failed"
            )

            assertThat(problemDetail.title).isEqualTo("Internal Server Error")
        }

        @Test
        fun `should use provided message as detail`() {
            val problemDetail = ProblemDetailFactory.forInternalError(
                message = "Database connection failed"
            )

            assertThat(problemDetail.detail).isEqualTo("Database connection failed")
        }

        @Test
        fun `should include exception type when provided`() {
            val problemDetail = ProblemDetailFactory.forInternalError(
                message = "Database connection failed",
                exceptionType = "SQLException"
            )

            assertThat(problemDetail.properties).containsEntry("exception", "SQLException")
        }

        @Test
        fun `should not include exception type when not provided`() {
            val problemDetail = ProblemDetailFactory.forInternalError(
                message = "Database connection failed"
            )

            assertThat(problemDetail.properties?.containsKey("exception") ?: false).isFalse()
        }

        @Test
        fun `should include cause when provided`() {
            val problemDetail = ProblemDetailFactory.forInternalError(
                message = "Database connection failed",
                cause = "Connection refused"
            )

            assertThat(problemDetail.properties).containsEntry("cause", "Connection refused")
        }

        @Test
        fun `should not include cause when not provided`() {
            val problemDetail = ProblemDetailFactory.forInternalError(
                message = "Database connection failed"
            )

            assertThat(problemDetail.properties?.containsKey("cause") ?: false).isFalse()
        }
    }

    @Nested
    inner class ForUnexpectedError {

        @Test
        fun `should return HTTP 500 status`() {
            val problemDetail = ProblemDetailFactory.forUnexpectedError(
                message = "Something went wrong"
            )

            assertThat(problemDetail.status).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
        }

        @Test
        fun `should set type URI with unexpected error code`() {
            val problemDetail = ProblemDetailFactory.forUnexpectedError(
                message = "Something went wrong"
            )

            assertThat(problemDetail.type.toString())
                .isEqualTo("urn:rapportnav:error:internal:UNEXPECTED_ERROR")
        }

        @Test
        fun `should set title to Unexpected Error`() {
            val problemDetail = ProblemDetailFactory.forUnexpectedError(
                message = "Something went wrong"
            )

            assertThat(problemDetail.title).isEqualTo("Unexpected Error")
        }

        @Test
        fun `should include exception type and cause when provided`() {
            val problemDetail = ProblemDetailFactory.forUnexpectedError(
                message = "Something went wrong",
                exceptionType = "NullPointerException",
                cause = "Value was null"
            )

            assertThat(problemDetail.properties).containsEntry("exception", "NullPointerException")
            assertThat(problemDetail.properties).containsEntry("cause", "Value was null")
        }
    }

    @Nested
    inner class BackendUsageErrorCodeExtensions {

        @Test
        fun `toTitle should return human readable titles for all codes`() {
            assertThat(BackendUsageErrorCode.PASSWORD_TOO_WEAK_EXCEPTION.toTitle()).isEqualTo("Password Too Weak")
            assertThat(BackendUsageErrorCode.INCORRECT_USER_IDENTIFIER_EXCEPTION.toTitle()).isEqualTo("Incorrect User Identifier")
            assertThat(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION.toTitle()).isEqualTo("Invalid Parameters")
            assertThat(BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION.toTitle()).isEqualTo("Could Not Save")
            assertThat(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION.toTitle()).isEqualTo("Resource Not Found")
            assertThat(BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION.toTitle()).isEqualTo("Could Not Delete")
            assertThat(BackendUsageErrorCode.ALREADY_EXISTS_EXCEPTION.toTitle()).isEqualTo("Resource Already Exists")
            assertThat(BackendUsageErrorCode.TOO_MANY_ROWS_EXCEPTION.toTitle()).isEqualTo("Too Many Rows")
            assertThat(BackendUsageErrorCode.COULD_NOT_FIND_CONTROL_FOR_INFRACTION_EXCEPTION.toTitle()).isEqualTo("Control Not Found For Infraction")
        }

        @Test
        fun `defaultMessage should return meaningful messages for all codes`() {
            assertThat(BackendUsageErrorCode.PASSWORD_TOO_WEAK_EXCEPTION.defaultMessage()).isEqualTo("The provided password does not meet security requirements")
            assertThat(BackendUsageErrorCode.INCORRECT_USER_IDENTIFIER_EXCEPTION.defaultMessage()).isEqualTo("The user identifier is incorrect")
            assertThat(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION.defaultMessage()).isEqualTo("The provided parameters are invalid")
            assertThat(BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION.defaultMessage()).isEqualTo("The resource could not be saved")
            assertThat(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION.defaultMessage()).isEqualTo("The requested resource could not be found")
            assertThat(BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION.defaultMessage()).isEqualTo("The resource could not be deleted")
            assertThat(BackendUsageErrorCode.ALREADY_EXISTS_EXCEPTION.defaultMessage()).isEqualTo("A resource with this identifier already exists")
            assertThat(BackendUsageErrorCode.TOO_MANY_ROWS_EXCEPTION.defaultMessage()).isEqualTo("The query returned too many rows")
            assertThat(BackendUsageErrorCode.COULD_NOT_FIND_CONTROL_FOR_INFRACTION_EXCEPTION.defaultMessage()).isEqualTo("No control found for the specified infraction")
        }
    }

    @Nested
    inner class BackendRequestErrorCodeExtensions {

        @Test
        fun `toTitle should return human readable title`() {
            assertThat(BackendRequestErrorCode.BODY_MISSING_DATA.toTitle()).isEqualTo("Missing Request Data")
        }

        @Test
        fun `defaultMessage should return meaningful message`() {
            assertThat(BackendRequestErrorCode.BODY_MISSING_DATA.defaultMessage()).isEqualTo("The request body is missing required data")
        }
    }
}
