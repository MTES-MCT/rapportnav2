package fr.gouv.dgampa.rapportnav.infrastructure.api.admin

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin")
class ApiAdminController {

    @GetMapping("/test")
    @Operation(summary = "Get data for a specific mission")
    fun getAdminRepository(): Any? {
        return "Yes I'm admin secured...";
    }
}
