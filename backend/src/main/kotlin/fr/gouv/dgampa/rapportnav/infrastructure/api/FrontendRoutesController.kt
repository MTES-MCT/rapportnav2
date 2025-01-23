package fr.gouv.dgampa.rapportnav.infrastructure.api

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class FrontendRoutesController {

        @GetMapping("/{path:^(?!api|graphql|static|error).*}")
        fun redirectToFrontend(): String {
            return "forward:/index.html"
        }
    }
