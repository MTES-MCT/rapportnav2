package fr.gouv.dgampa.rapportnav.infrastructure.api

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.io.File
import java.security.SecureRandom
import java.util.*

/**
 * Controller that serves the SPA index.html for all frontend routes.
 * Handles CSP nonce injection directly to avoid issues with ContentCachingResponseWrapper
 * and Spring's optimized static file streaming.
 */
@Controller
class FrontendRoutesController(
    @Value("\${STATIC_FILES_PATH:}") private val staticFilesPath: String
) {
    companion object {
        private const val NONCE_PLACEHOLDER = "__CSP_NONCE__"
        private const val NONCE_SIZE = 16 // 128 bits
        private val secureRandom = SecureRandom()
    }

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
        ],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    @ResponseBody
    fun serveFrontend(request: HttpServletRequest, response: HttpServletResponse): String {
        val indexFile = File(staticFilesPath, "index.html")
        var htmlContent = indexFile.readText(Charsets.UTF_8)

        // Generate nonce and inject into HTML
        val nonce = generateNonce()

        // Replace placeholder if present (source HTML)
        htmlContent = htmlContent.replace(NONCE_PLACEHOLDER, nonce)

        // Inject nonce into inline scripts that don't have one (built HTML)
        // Matches <script> tags without src attribute (inline scripts)
        htmlContent = htmlContent.replace(
            Regex("""<script(?![^>]*\bsrc\b)(?![^>]*\bnonce\b)([^>]*)>"""),
            """<script nonce="$nonce"$1>"""
        )

        // Set CSP header with nonce
        val isSecure = request.isSecure
        setCspHeader(response, nonce, isSecure)

        return htmlContent
    }

    private fun generateNonce(): String {
        val bytes = ByteArray(NONCE_SIZE)
        secureRandom.nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }

    private fun setCspHeader(response: HttpServletResponse, nonce: String, isSecure: Boolean) {

        // this is for our integration server, which is http and not https
        // setting 'upgrade-insecure-requests' on http blocks requests
        val upgradeInsecure = if (isSecure) "upgrade-insecure-requests;" else ""

        val csp = "default-src 'self'; " +
            "script-src 'self' 'nonce-$nonce'; " +
            "style-src 'self' 'unsafe-inline'; " +
            "img-src 'self'; " +
            "font-src 'self' data:; " +
            "connect-src 'self' https://sentry.incubateur.net https://recherche-entreprises.api.gouv.fr; " +
            "frame-src 'none'; " +
            "base-uri 'self'; " +
            "frame-ancestors 'none'; " +
            "form-action 'self'; " +
            "object-src 'none'; " +
            upgradeInsecure

        response.setHeader("Content-Security-Policy", csp.trimEnd())
    }
}
