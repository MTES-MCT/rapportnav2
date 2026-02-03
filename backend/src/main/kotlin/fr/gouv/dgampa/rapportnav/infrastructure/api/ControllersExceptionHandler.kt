package fr.gouv.dgampa.rapportnav.infrastructure.api

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.ProblemDetailFactory
import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
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

    // -------------------------------------------------------------------------
    // Domain exceptions

    @ExceptionHandler(BackendInternalException::class)
    fun handleBackendInternalException(
        e: BackendInternalException,
    ): ResponseEntity<ProblemDetail> {
        logger.error("Internal error: ${e.message}", e)
        val problemDetail = ProblemDetailFactory.forInternalError(
            message = e.message,
            exceptionType = e::class.simpleName,
            cause = e.originalException?.message
        )
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(PROBLEM_JSON_MEDIA_TYPE)
            .body(problemDetail)
    }

    @ExceptionHandler(BackendRequestException::class)
    fun handleBackendRequestException(e: BackendRequestException): ResponseEntity<ProblemDetail> {
        logger.warn("Request error: ${e.message}", e)
        val problemDetail = ProblemDetailFactory.forRequestError(
            code = e.code,
            message = e.message,
            data = e.data
        )
        return ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .contentType(PROBLEM_JSON_MEDIA_TYPE)
            .body(problemDetail)
    }

    @ExceptionHandler(BackendUsageException::class)
    fun handleBackendUsageException(e: BackendUsageException): ResponseEntity<ProblemDetail> {
        logger.warn("Usage error: code=${e.code}, message=${e.message}")
        val problemDetail = ProblemDetailFactory.forUsageError(
            code = e.code,
            message = e.message,
            data = e.data
        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(PROBLEM_JSON_MEDIA_TYPE)
            .body(problemDetail)
    }


    // -------------------------------------------------------------------------
    // Infrastructure and unhandled domain exceptions
    // - Unhandled domain exceptions are a bug, thus an unexpected exception.
    // - Infrastructure exceptions are not supposed to bubble up until here.
    //   They should be caught or transformed into domain exceptions.
    //   If that happens, it's a bug, thus an unexpected exception.

    /** Catch-all for unexpected exceptions. */
    @ExceptionHandler(Throwable::class)
    fun handleUnexpectedException(e: Throwable): ResponseEntity<ProblemDetail> {
        logger.error("Unexpected error: ${e.message}", e)
        val problemDetail = ProblemDetailFactory.forUnexpectedError(
            message = e.message ?: "An unexpected error occurred",
            exceptionType = e::class.simpleName,
            cause = e.cause?.message
        )
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(PROBLEM_JSON_MEDIA_TYPE)
            .body(problemDetail)
    }
}
