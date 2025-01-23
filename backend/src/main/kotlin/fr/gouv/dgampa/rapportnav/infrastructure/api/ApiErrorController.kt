package fr.gouv.dgampa.rapportnav.infrastructure.api

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.context.request.ServletWebRequest

@Controller
class ApiErrorController : ErrorController {

    private val logger = LoggerFactory.getLogger(ApiErrorController::class.java)

    @RequestMapping("/error")
    fun error(request: HttpServletRequest): ResponseEntity<Map<String, Any>> {
        val errorAttributes = DefaultErrorAttributes()
        val webRequest = ServletWebRequest(request)
        val options = ErrorAttributeOptions.of(
            ErrorAttributeOptions.Include.MESSAGE,
            ErrorAttributeOptions.Include.EXCEPTION,
            ErrorAttributeOptions.Include.ERROR,
            ErrorAttributeOptions.Include.STATUS,
        )

        val errorDetails = errorAttributes.getErrorAttributes(webRequest, options)
        val status = errorDetails["status"] as? Int ?: 500

        // Log the error details for debugging
        logger.error("Error occurred: {}", errorDetails)

        return ResponseEntity.status(status).body(errorDetails)
    }

}
