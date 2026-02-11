package fr.gouv.dgampa.rapportnav.infrastructure.api

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.ProblemDetailFactory
import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestException
import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Global exception handler for all controllers.
 *
 * Returns RFC 7807 Problem Details responses with `application/problem+json` content type.
 *
 * Sentry integration: Errors are automatically captured via the Log4j2 Sentry appender
 * when we call logger.error(). No manual Sentry.captureException() calls needed.
 */
@RestControllerAdvice
@Order(HIGHEST_PRECEDENCE)
class ControllersExceptionHandler {
    private val logger: Logger = LoggerFactory.getLogger(ControllersExceptionHandler::class.java)

    companion object {
        private val PROBLEM_JSON_MEDIA_TYPE = MediaType.parseMediaType("application/problem+json")
    }

    private fun respond(status: HttpStatus, body: ProblemDetail) = ResponseEntity
        .status(status).contentType(PROBLEM_JSON_MEDIA_TYPE).body(body)

    // -------------------------------------------------------------------------
    // Domain exceptions

    @ExceptionHandler(BackendInternalException::class)
    fun handleBackendInternalException(e: BackendInternalException): ResponseEntity<ProblemDetail> {
        logger.error("Internal error: ${e.message}", e)
        return respond(HttpStatus.INTERNAL_SERVER_ERROR, ProblemDetailFactory.forInternalError(
            message = e.message,
            exceptionType = e::class.simpleName,
            cause = e.originalException?.message
        ))
    }

    @ExceptionHandler(BackendRequestException::class)
    fun handleBackendRequestException(e: BackendRequestException): ResponseEntity<ProblemDetail> {
        logger.warn("Request error: ${e.message}", e)
        return respond(HttpStatus.UNPROCESSABLE_ENTITY, ProblemDetailFactory.forRequestError(
            code = e.code,
            message = e.message,
            data = e.data
        ))
    }

    @ExceptionHandler(BackendUsageException::class)
    fun handleBackendUsageException(e: BackendUsageException): ResponseEntity<ProblemDetail> {
        logger.warn("Usage error: code=${e.code}, message=${e.message}")
        return respond(HttpStatus.BAD_REQUEST, ProblemDetailFactory.forUsageError(
            code = e.code,
            message = e.message,
            data = e.data
        ))
    }

    // -------------------------------------------------------------------------
    // Validation exceptions

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ProblemDetail> {
        val fieldErrors = e.bindingResult.fieldErrors.map { error ->
            mapOf(
                "field" to error.field,
                "message" to (error.defaultMessage ?: "Invalid value"),
                "rejectedValue" to error.rejectedValue
            )
        }
        logger.warn("Validation error: ${fieldErrors.size} field(s) invalid")
        return respond(HttpStatus.BAD_REQUEST, ProblemDetailFactory.forValidationError(
            message = "Request validation failed",
            errors = fieldErrors
        ))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<ProblemDetail> {
        val violations = e.constraintViolations.map { violation ->
            mapOf(
                "field" to violation.propertyPath.toString(),
                "message" to violation.message,
                "rejectedValue" to violation.invalidValue
            )
        }
        logger.warn("Constraint violation: ${violations.size} violation(s)")
        return respond(HttpStatus.BAD_REQUEST, ProblemDetailFactory.forValidationError(
            message = "Request constraint validation failed",
            errors = violations
        ))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ProblemDetail> {
        logger.warn("Malformed request body: ${e.message}")
        return respond(HttpStatus.BAD_REQUEST, ProblemDetailFactory.forValidationError(
            message = "Malformed request body: unable to parse JSON",
            errors = null
        ))
    }

    // -------------------------------------------------------------------------
    // Infrastructure and unhandled domain exceptions
    // - Unhandled domain exceptions are a bug, thus an unexpected exception.
    // - Infrastructure exceptions are not supposed to bubble up until here.
    //   They should be caught or transformed into domain exceptions.
    //   If that happens, it's a bug, thus an unexpected exception.

    @ExceptionHandler(Throwable::class)
    fun handleUnexpectedException(e: Throwable): ResponseEntity<ProblemDetail> {
        logger.error("Unexpected error: ${e.message}", e)
        return respond(HttpStatus.INTERNAL_SERVER_ERROR, ProblemDetailFactory.forUnexpectedError(
            message = e.message ?: "An unexpected error occurred",
            exceptionType = e::class.simpleName,
            cause = e.cause?.message
        ))
    }
}
