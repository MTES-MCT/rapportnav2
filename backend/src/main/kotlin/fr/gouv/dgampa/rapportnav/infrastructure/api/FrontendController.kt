package fr.gouv.dgampa.rapportnav.infrastructure.api

import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller

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

    // TODO RESTORE
//    @RequestMapping("/error")
//    fun error(request: HttpServletRequest, response: HttpServletResponse): Any {
//        response.status = HttpStatus.OK.value()
//        return "forward:/index.html"
//    }

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
