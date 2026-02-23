package fr.gouv.dgampa.rapportnav.infrastructure.api

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendForbiddenException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.ProblemDetailFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * Global exception handler for all controllers.
 *
 * Extends [ResponseEntityExceptionHandler] to leverage Spring's built-in RFC 7807 handling
 * for standard Spring MVC exceptions (MethodArgumentNotValidException, HttpMessageNotReadableException, etc.).
 *
 * Returns RFC 7807 Problem Details responses with `application/problem+json` content type.
 *
 * Sentry integration: Errors are automatically captured via the Log4j2 Sentry appender
 * when we call logger.error(). No manual Sentry.captureException() calls needed.
 */
@RestControllerAdvice
@Order(HIGHEST_PRECEDENCE)
class ControllersExceptionHandler : ResponseEntityExceptionHandler() {
    private val log: Logger = LoggerFactory.getLogger(ControllersExceptionHandler::class.java)

    companion object {
        private val PROBLEM_JSON_MEDIA_TYPE = MediaType.APPLICATION_PROBLEM_JSON
    }

    private fun respond(status: HttpStatus, body: ProblemDetail) = ResponseEntity
        .status(status).contentType(PROBLEM_JSON_MEDIA_TYPE).body(body)

    // -------------------------------------------------------------------------
    // Domain exceptions (business rule violations)

    @ExceptionHandler(BackendUsageException::class)
    fun handleBackendUsageException(e: BackendUsageException): ResponseEntity<ProblemDetail> {
        log.warn("Usage error: code=${e.code}, message=${e.message}")
        return respond(HttpStatus.BAD_REQUEST, ProblemDetailFactory.forUsageError(
            code = e.code,
            message = e.message,
            data = e.data
        ))
    }

    @ExceptionHandler(BackendForbiddenException::class)
    fun handleBackendForbiddenException(e: BackendForbiddenException): ResponseEntity<ProblemDetail> {
        logger.warn("Forbidden: code=${e.code}, message=${e.message}")
        return respond(HttpStatus.FORBIDDEN, ProblemDetailFactory.forForbiddenError(
            code = e.code,
            message = e.message,
            data = e.data
        ))
    }

    // -------------------------------------------------------------------------
    // Infrastructure exceptions (technical failures)

    @ExceptionHandler(BackendInternalException::class)
    fun handleBackendInternalException(e: BackendInternalException): ResponseEntity<ProblemDetail> {
        log.error("Internal error: ${e.message}", e)
        return respond(HttpStatus.INTERNAL_SERVER_ERROR, ProblemDetailFactory.forInternalError(
            message = e.message,
            exceptionType = e::class.simpleName,
            cause = e.originalException?.message
        ))
    }

    // -------------------------------------------------------------------------
    // Validation exceptions
    //
    // We override ResponseEntityExceptionHandler methods to provide a consistent
    // response format across all validation errors. This gives us:
    //
    // 1. Consistent "code" field (VALIDATION_ERROR) for frontend error handling
    // 2. Structured "errors" array with field-level details
    // 3. Meaningful "type" URI instead of "about:blank"
    // 4. Proper content-type: application/problem+json

    /**
     * Handles @Valid annotation failures on request body DTOs.
     *
     * Triggered when a request body fails bean validation constraints like
     * @NotNull, @NotEmpty, @Size, @Email, etc.
     *
     * Example trigger: POST with {"missionIds": []} when @NotEmpty is required
     */
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val fieldErrors = ex.bindingResult.fieldErrors.map { error ->
            mapOf(
                "field" to error.field,
                "message" to (error.defaultMessage ?: "Invalid value"),
                "rejectedValue" to error.rejectedValue
            )
        }
        log.warn("Validation error: ${fieldErrors.size} field(s) invalid")
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(PROBLEM_JSON_MEDIA_TYPE)
            .body(ProblemDetailFactory.forValidationError(
                message = "Request validation failed",
                errors = fieldErrors
            ))
    }

    /**
     * Handles malformed JSON in request body.
     *
     * Triggered when the request body cannot be parsed as valid JSON,
     * or when JSON values cannot be deserialized to the expected types.
     *
     * Example triggers:
     * - Invalid JSON syntax: "{ invalid }"
     * - Wrong enum value: {"exportMode": "WRONG"} when expecting INDIVIDUAL_MISSION
     * - Type mismatch: {"missionIds": "not-an-array"} when expecting List<Int>
     */
    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        log.warn("Malformed request body: ${ex.message}")
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(PROBLEM_JSON_MEDIA_TYPE)
            .body(ProblemDetailFactory.forValidationError(
                message = "Malformed request body: unable to parse JSON",
                errors = null
            ))
    }

    // -------------------------------------------------------------------------
    // Fallback handler

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(e: Exception): ResponseEntity<ProblemDetail> {
        log.error("Unexpected error: ${e.message}", e)
        return respond(HttpStatus.INTERNAL_SERVER_ERROR, ProblemDetailFactory.forUnexpectedError(
            message = e.message ?: "An unexpected error occurred",
            exceptionType = e::class.simpleName,
            cause = e.cause?.message
        ))
    }
}
