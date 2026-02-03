package fr.gouv.dgampa.rapportnav.infrastructure.api.adapters

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import java.net.URI

/**
 * Factory for creating RFC 7807 Problem Detail responses.
 *
 * Creates standardized error responses with:
 * - `type`: URN identifying the error type (e.g., `urn:rapportnav:error:usage:COULD_NOT_FIND_EXCEPTION`)
 * - `title`: Human-readable summary of the problem
 * - `status`: HTTP status code
 * - `detail`: Detailed explanation of the error
 * - `code`: Extension property for backwards compatibility with existing frontend
 * - `data`: Extension property for additional error context
 */
object ProblemDetailFactory {

    private const val URN_PREFIX = "urn:rapportnav:error"

    /**
     * Creates a Problem Detail for usage errors (HTTP 400).
     *
     * Usage errors occur when a request is valid but the backend cannot process it,
     * typically due to stale client data.
     */
    fun forUsageError(
        code: BackendUsageErrorCode,
        message: String? = null,
        data: Any? = null
    ): ProblemDetail = buildWithCode(
        status = HttpStatus.BAD_REQUEST,
        category = "usage",
        code = code.name,
        title = code.toTitle(),
        detail = message ?: code.defaultMessage(),
        data = data
    )

    /**
     * Creates a Problem Detail for request errors (HTTP 422).
     *
     * Request errors occur when the request itself is invalid
     * (missing/wrongly-typed properties, illogical data).
     */
    fun forRequestError(
        code: BackendRequestErrorCode,
        message: String? = null,
        data: Any? = null
    ): ProblemDetail = buildWithCode(
        status = HttpStatus.UNPROCESSABLE_ENTITY,
        category = "request",
        code = code.name,
        title = code.toTitle(),
        detail = message ?: code.defaultMessage(),
        data = data
    )

    /**
     * Creates a Problem Detail for internal errors (HTTP 500).
     *
     * Internal errors are known backend failures that were caught and wrapped
     * in BackendInternalException.
     */
    fun forInternalError(
        message: String,
        exceptionType: String? = null,
        cause: String? = null
    ): ProblemDetail = buildServerError(
        errorCode = "INTERNAL_ERROR",
        title = "Internal Server Error",
        detail = message,
        exceptionType = exceptionType,
        cause = cause
    )

    /**
     * Creates a Problem Detail for unexpected errors (HTTP 500).
     *
     * Unexpected errors are unhandled exceptions that bubbled up to the controller layer.
     */
    fun forUnexpectedError(
        message: String,
        exceptionType: String? = null,
        cause: String? = null
    ): ProblemDetail = buildServerError(
        errorCode = "UNEXPECTED_ERROR",
        title = "Unexpected Error",
        detail = message,
        exceptionType = exceptionType,
        cause = cause
    )

    private fun buildWithCode(
        status: HttpStatus,
        category: String,
        code: String,
        title: String,
        detail: String,
        data: Any?
    ): ProblemDetail = ProblemDetail.forStatus(status).apply {
        this.type = URI.create("$URN_PREFIX:$category:$code")
        this.title = title
        this.detail = detail
        setProperty("code", code)
        data?.let { setProperty("data", it) }
    }

    private fun buildServerError(
        errorCode: String,
        title: String,
        detail: String,
        exceptionType: String?,
        cause: String?
    ): ProblemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR).apply {
        this.type = URI.create("$URN_PREFIX:internal:$errorCode")
        this.title = title
        this.detail = detail
        exceptionType?.let { setProperty("exception", it) }
        cause?.let { setProperty("cause", it) }
    }
}

/**
 * Returns a human-readable title for the error code.
 */
fun BackendUsageErrorCode.toTitle(): String = when (this) {
    BackendUsageErrorCode.PASSWORD_TOO_WEAK_EXCEPTION -> "Password Too Weak"
    BackendUsageErrorCode.INCORRECT_USER_IDENTIFIER_EXCEPTION -> "Incorrect User Identifier"
    BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION -> "Invalid Parameters"
    BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION -> "Could Not Save"
    BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION -> "Resource Not Found"
    BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION -> "Could Not Delete"
    BackendUsageErrorCode.ALREADY_EXISTS_EXCEPTION -> "Resource Already Exists"
    BackendUsageErrorCode.TOO_MANY_ROWS_EXCEPTION -> "Too Many Rows"
    BackendUsageErrorCode.COULD_NOT_FIND_CONTROL_FOR_INFRACTION_EXCEPTION -> "Control Not Found For Infraction"
}

/**
 * Returns a default message for the error code when no custom message is provided.
 */
fun BackendUsageErrorCode.defaultMessage(): String = when (this) {
    BackendUsageErrorCode.PASSWORD_TOO_WEAK_EXCEPTION -> "The provided password does not meet security requirements"
    BackendUsageErrorCode.INCORRECT_USER_IDENTIFIER_EXCEPTION -> "The user identifier is incorrect"
    BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION -> "The provided parameters are invalid"
    BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION -> "The resource could not be saved"
    BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION -> "The requested resource could not be found"
    BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION -> "The resource could not be deleted"
    BackendUsageErrorCode.ALREADY_EXISTS_EXCEPTION -> "A resource with this identifier already exists"
    BackendUsageErrorCode.TOO_MANY_ROWS_EXCEPTION -> "The query returned too many rows"
    BackendUsageErrorCode.COULD_NOT_FIND_CONTROL_FOR_INFRACTION_EXCEPTION -> "No control found for the specified infraction"
}

/**
 * Returns a human-readable title for the error code.
 */
fun BackendRequestErrorCode.toTitle(): String = when (this) {
    BackendRequestErrorCode.BODY_MISSING_DATA -> "Missing Request Data"
}

/**
 * Returns a default message for the error code when no custom message is provided.
 */
fun BackendRequestErrorCode.defaultMessage(): String = when (this) {
    BackendRequestErrorCode.BODY_MISSING_DATA -> "The request body is missing required data"
}
