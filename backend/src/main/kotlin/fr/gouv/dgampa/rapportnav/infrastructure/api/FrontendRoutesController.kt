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
            "/pam/**",
            "/ulam/**",
            "/v2/**"
        ]
    )
    fun redirectToFrontend(): String {
        return "forward:/index.html"
    }
}
