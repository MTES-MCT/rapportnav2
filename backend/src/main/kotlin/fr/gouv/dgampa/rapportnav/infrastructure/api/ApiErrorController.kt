package fr.gouv.dgampa.rapportnav.infrastructure.api

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiErrorHandler {

    private val logger = LoggerFactory.getLogger(ApiErrorHandler::class.java)

    @ExceptionHandler(Exception::class)
    fun handleException(
        ex: Exception,
        request: HttpServletRequest
    ): ProblemDetail {

        logger.error("Unhandled exception", ex)

        val problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        problem.title = "Internal Server Error"
        problem.detail = ex.message
        problem.setProperty("path", request.requestURI)
        problem.setProperty("exception", ex.javaClass.name)

        return problem
    }
}
