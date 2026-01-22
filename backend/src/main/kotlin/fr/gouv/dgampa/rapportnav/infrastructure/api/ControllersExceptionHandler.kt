package fr.gouv.dgampa.rapportnav.infrastructure.api

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs.BackendInternalErrorDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs.BackendRequestErrorDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs.BackendUsageErrorDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Global exception handler for all controllers.
 *
 * Sentry integration: Errors are automatically captured via the Log4j2 Sentry appender
 * when we call logger.error(). No manual Sentry.captureException() calls needed.
 */
@RestControllerAdvice
@Order(HIGHEST_PRECEDENCE)
class ControllersExceptionHandler {
    private val logger: Logger = LoggerFactory.getLogger(ControllersExceptionHandler::class.java)

    // -------------------------------------------------------------------------
    // Domain exceptions

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BackendInternalException::class)
    fun handleBackendInternalException(
        e: BackendInternalException,
    ): BackendInternalErrorDataOutput {
        logger.error("Internal error: ${e.message}", e)
        return BackendInternalErrorDataOutput(
            message = e.message,
            exception = e::class.simpleName,
            cause = e.originalException?.message
        )
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BackendRequestException::class)
    fun handleBackendRequestException(e: BackendRequestException): BackendRequestErrorDataOutput {
        logger.warn("Request error: ${e.message}", e)
        return BackendRequestErrorDataOutput(code = e.code, data = e.data, message = e.message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BackendUsageException::class)
    fun handleBackendUsageException(e: BackendUsageException): BackendUsageErrorDataOutput {
        logger.warn("Usage error: code=${e.code}, message=${e.message}")
        return BackendUsageErrorDataOutput(code = e.code, data = e.data, message = e.message)
    }


    // -------------------------------------------------------------------------
    // Infrastructure and unhandled domain exceptions
    // - Unhandled domain exceptions are a bug, thus an unexpected exception.
    // - Infrastructure exceptions are not supposed to bubble up until here.
    //   They should be caught or transformed into domain exceptions.
    //   If that happens, it's a bug, thus an unexpected exception.

    /** Catch-all for unexpected exceptions. */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable::class)
    fun handleUnexpectedException(e: Throwable): BackendInternalErrorDataOutput {
        logger.error("Unexpected error: ${e.message}", e)
        return BackendInternalErrorDataOutput(
            message = e.message ?: "An unexpected error occurred",
            exception = e::class.simpleName,
            cause = e.cause?.message
        )
    }
}
