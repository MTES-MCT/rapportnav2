package fr.gouv.dgampa.rapportnav.infrastructure.api.admin

import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/admin")
class ApiAdminController {

    private val logger = LoggerFactory.getLogger(ApiAdminController::class.java)

    @GetMapping("/test")
    @Operation(summary = "Admin test endpoint")
    fun testEndpoint(): ResponseEntity<String> {
        val auth = SecurityContextHolder.getContext().authentication
        logger.debug("AUTHENTICATED PRINCIPAL: {}", auth?.principal)
        logger.debug("AUTHORITIES: {}", auth?.authorities)
        return ResponseEntity.ok("Yes I'm admin secured...")
    }
}
