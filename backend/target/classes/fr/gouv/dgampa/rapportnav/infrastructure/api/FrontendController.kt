package fr.gouv.dgampa.rapportnav.infrastructure.api

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping

/**
 * This controller redirects all unhandled routes by the backend to our SPA frontend
 */
@Controller
class FrontendController : ErrorController {

    private val logger = LoggerFactory.getLogger(FrontendController::class.java)

    // @GetMapping(value = {"/", "/{path:^(?!api$|static$|favicon).*}"})
    // fun error(request: HttpServletRequest, response: HttpServletResponse): Any {
    //     logger.info("error with request {}", request)
    //     logger.info("error with response {}", response)
    //     response.status = HttpStatus.OK.value()
    //     return "forward:/index.html"
    // }

    @RequestMapping("/error")
    fun error(request: HttpServletRequest, response: HttpServletResponse): Any {
        response.status = HttpStatus.OK.value()
        return "forward:/index.html"
    }

    // @GetMapping("/")
    // fun index(): String {
    //     return "index.html" // This should match the name of your index.html template
    // }

    // @GetMapping("/{path:^(?!api$|static$|favicon).*}")
    // fun redirect(): String {
    //     return "forward:/index.html"
    // }

    val errorPath: String
        get() = "/error"
}
