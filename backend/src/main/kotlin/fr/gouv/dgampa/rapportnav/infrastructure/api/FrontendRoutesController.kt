package fr.gouv.dgampa.rapportnav.infrastructure.api

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class FrontendRoutesController {

    @GetMapping(
        value = [
            "/",
            "/login",
            "/signup",
            "/admin",
            "/pam/**",
            "/ulam/**",
            "/inquiry/**",
            "/v2/**"
        ]
    )
    fun redirectToFrontend(): String {
        return "forward:/index.html"
    }
}
