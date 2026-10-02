package fr.gouv.dgampa.rapportnav.infrastructure.api

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.io.File
import java.net.URI
import java.security.SecureRandom
import java.util.*

/**
 * Controller that serves the SPA index.html for all frontend routes.
 * Handles CSP nonce injection directly to avoid issues with ContentCachingResponseWrapper
 * and Spring's optimized static file streaming.
 */
@Controller
class FrontendRoutesController(
    @param:Value("\${STATIC_FILES_PATH:}") private val staticFilesPath: String,
    @param:Value("\${metabase.site-url}") private val metabaseSiteUrl: String,
    // ProConnect issuer URI (e.g. https://auth.agentconnect.gouv.fr/api/v2). Only its scheme+host
    // is used, to allow the OIDC logout form POST to redirect to ProConnect's end_session endpoint.
    @param:Value("\${rapportnav.oidc.issuer-uri:}") private val oidcIssuerUri: String
) {
    companion object {
        private const val NONCE_PLACEHOLDER = "__CSP_NONCE__"
        private const val NONCE_SIZE = 16 // 128 bits
        private val secureRandom = SecureRandom()
    }

    // scheme://host of the ProConnect issuer, or "" when OIDC is not configured. Used in the CSP
    // form-action directive so the cross-origin post-logout redirect is not blocked (browsers such
    // as Firefox enforce form-action against form-submission redirect targets).
    private val proConnectOrigin: String = runCatching {
        oidcIssuerUri.takeIf { it.isNotBlank() }?.let { URI(it) }?.let { "${it.scheme}://${it.authority}" }
    }.getOrNull().orEmpty()

    @GetMapping(
        value = [
            "/",
            "/login",
            "/signup",
            "/admin",
            "/pam/**",
            "/ulam/**",
            "/inquiry/**",
            "/v2/**",
            "/metabase"
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

        // Prevent caching of HTML (each response has a unique CSP nonce)
        response.setHeader("Cache-Control", "no-store")

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

        // Allow the OIDC logout form POST to redirect to ProConnect's end_session endpoint.
        // Without the ProConnect origin here, a strict 'self' form-action blocks that cross-origin
        // redirect in browsers that re-check form-action against redirect targets (e.g. Firefox).
        val formAction = "form-action 'self'" + (if (proConnectOrigin.isNotBlank()) " $proConnectOrigin" else "") + "; "

        val csp = "default-src 'self'; " +
            "script-src 'self' 'nonce-$nonce'; " +
            "style-src 'self' 'unsafe-inline'; " +
            "img-src 'self'; " +
            "font-src 'self' data:; " +
            "connect-src 'self' https://sentry.incubateur.net https://recherche-entreprises.api.gouv.fr https://data.geopf.fr; " +
            "frame-src $metabaseSiteUrl; " +
            "base-uri 'self'; " +
            "frame-ancestors 'none'; " +
            formAction +
            "object-src 'none'; " +
            upgradeInsecure

        response.setHeader("Content-Security-Policy", csp.trimEnd())
    }
}
