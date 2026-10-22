package fr.gouv.gmampa.rapportnav.infrastructure.api

import fr.gouv.dgampa.rapportnav.infrastructure.api.FrontendRoutesController
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.kotlin.argThat
import org.springframework.web.bind.annotation.GetMapping
import java.io.File
import java.nio.file.Path

class FrontendRoutesControllerTest {

    @TempDir
    lateinit var tempDir: Path

    private lateinit var controller: FrontendRoutesController
    private lateinit var request: HttpServletRequest
    private lateinit var response: HttpServletResponse

    @BeforeEach
    fun setUp() {
        request = mock()
        response = mock()
    }

    private fun createControllerWithHtml(
        htmlContent: String,
        sentryDsn: String = "https://sentry.test/1",
        satiEnabledServices: String = "5,6,7"
    ): FrontendRoutesController {
        val indexFile = File(tempDir.toFile(), "index.html")
        indexFile.writeText(htmlContent)
        return FrontendRoutesController(
            tempDir.toString(),
            "https://metabase.test",
            sentryDsn,
            "support@example.fr",
            "a@example.fr,b@example.fr",
            satiEnabledServices
        )
    }

    @Nested
    inner class PlaceholderReplacement {
        @Test
        fun `should replace __CSP_NONCE__ placeholder with generated nonce`() {
            val html = """<html><head><script nonce="__CSP_NONCE__">console.log('test');</script></head></html>"""
            controller = createControllerWithHtml(html)

            whenever(request.isSecure).thenReturn(true)

            val result = controller.serveFrontend(request, response)

            assertFalse(result.contains("__CSP_NONCE__"), "Placeholder should be replaced")
            assertTrue(result.contains("nonce=\""), "Nonce attribute should be present")
        }

        @Test
        fun `should replace multiple __CSP_NONCE__ placeholders with same nonce`() {
            val html = """
                <html>
                <head>
                    <script nonce="__CSP_NONCE__">console.log('1');</script>
                    <script nonce="__CSP_NONCE__">console.log('2');</script>
                </head>
                </html>
            """.trimIndent()
            controller = createControllerWithHtml(html)

            whenever(request.isSecure).thenReturn(true)

            val result = controller.serveFrontend(request, response)

            assertFalse(result.contains("__CSP_NONCE__"), "All placeholders should be replaced")
            val nonces = Regex("""nonce="([^"]+)"""").findAll(result).map { it.groupValues[1] }.toList()
            assertEquals(2, nonces.size, "Should have 2 nonces")
            assertEquals(nonces[0], nonces[1], "All nonces should be identical")
        }
    }

    @Nested
    inner class ImportMetaEnvInjection {
        private val envScript =
            """<script>globalThis.import_meta_env = JSON.parse('"import_meta_env_placeholder"');</script>"""

        @Test
        fun `should replace import_meta_env placeholder with configured values`() {
            controller = createControllerWithHtml("<html><head>$envScript</head></html>")

            whenever(request.isSecure).thenReturn(true)

            val result = controller.serveFrontend(request, response)

            assertFalse(result.contains("import_meta_env_placeholder"), "Placeholder should be replaced")
            assertTrue(result.contains("\"SATI_ENABLED_SERVICES\":\"5,6,7\""), "SATI services should be injected")
            assertTrue(result.contains("\"FRONTEND_SENTRY_DSN\":\"https://sentry.test/1\""), "Sentry DSN should be injected")
            assertTrue(result.contains("\"MAILTO_SUPPORT_RESOURCE\":\"a@example.fr,b@example.fr\""))
        }

        @Test
        fun `should escape quotes and closing script tags in injected values`() {
            controller = createControllerWithHtml(
                "<html><head>$envScript</head></html>",
                sentryDsn = "it's</script><script>alert(1)"
            )

            whenever(request.isSecure).thenReturn(true)

            val result = controller.serveFrontend(request, response)

            assertFalse(result.contains("</script><script>alert"), "Closing script tag must be escaped")
            assertTrue(result.contains("it\\'s"), "Single quote must be escaped for the JS string")
            assertTrue(result.contains("\\\\u003c/script>"), "< must be escaped as \\u003c")
        }

        @Test
        fun `should leave html unchanged when there is no placeholder`() {
            val html = """<html><head><script>globalThis.import_meta_env = JSON.parse('{"A":"b"}');</script></head></html>"""
            controller = createControllerWithHtml(html)

            whenever(request.isSecure).thenReturn(true)

            val result = controller.serveFrontend(request, response)

            assertTrue(result.contains("""JSON.parse('{"A":"b"}')"""))
        }
    }

    @Nested
    inner class InlineScriptNonceInjection {
        @Test
        fun `should inject nonce into inline scripts without nonce attribute`() {
            val html = """<html><head><script>console.log('inline');</script></head></html>"""
            controller = createControllerWithHtml(html)

            whenever(request.isSecure).thenReturn(true)

            val result = controller.serveFrontend(request, response)

            assertTrue(result.contains("""<script nonce="""), "Nonce should be injected into inline script")
        }

        @Test
        fun `should not inject nonce into scripts with src attribute`() {
            val html = """<html><head><script src="/app.js"></script></head></html>"""
            controller = createControllerWithHtml(html)

            whenever(request.isSecure).thenReturn(true)

            val result = controller.serveFrontend(request, response)

            assertFalse(result.contains("""<script nonce="""), "External scripts should not get nonce injected")
            assertTrue(result.contains("""<script src="/app.js">"""), "Script tag should remain unchanged")
        }

        @Test
        fun `should not double-inject nonce into scripts that already have nonce`() {
            val html = """<html><head><script nonce="existing">console.log('test');</script></head></html>"""
            controller = createControllerWithHtml(html)

            whenever(request.isSecure).thenReturn(true)

            val result = controller.serveFrontend(request, response)

            val nonceCount = Regex("""nonce=""").findAll(result).count()
            assertEquals(1, nonceCount, "Should not add additional nonce to script that already has one")
        }
    }

    @Nested
    inner class CspHeader {
        @Test
        fun `should include upgrade-insecure-requests on HTTPS request`() {
            val html = "<html><head></head><body></body></html>"
            controller = createControllerWithHtml(html)

            whenever(request.isSecure).thenReturn(true)

            controller.serveFrontend(request, response)

            verify(response).setHeader(argThat { this == "Content-Security-Policy" }, argThat {
                this.contains("upgrade-insecure-requests")
            })
        }

        @Test
        fun `should not include upgrade-insecure-requests on HTTP request`() {
            val html = "<html><head></head><body></body></html>"
            controller = createControllerWithHtml(html)

            whenever(request.isSecure).thenReturn(false)

            controller.serveFrontend(request, response)

            verify(response).setHeader(argThat { this == "Content-Security-Policy" }, argThat {
                !this.contains("upgrade-insecure-requests")
            })
        }

        @Test
        fun `should include upgrade-insecure-requests when request isSecure is true`() {
            // ForwardedHeaderFilter now handles X-Forwarded-Proto automatically
            // and sets request.isSecure accordingly
            val html = "<html><head></head><body></body></html>"
            controller = createControllerWithHtml(html)

            whenever(request.isSecure).thenReturn(true)

            controller.serveFrontend(request, response)

            verify(response).setHeader(argThat { this == "Content-Security-Policy" }, argThat {
                this.contains("upgrade-insecure-requests")
            })
        }

        @Test
        fun `CSP nonce in header should match nonce in HTML`() {
            val html = """<html><head><script>console.log('test');</script></head></html>"""
            controller = createControllerWithHtml(html)

            whenever(request.isSecure).thenReturn(true)

            var capturedCsp: String? = null
            whenever(response.setHeader(argThat { this == "Content-Security-Policy" }, argThat { true })).then {
                capturedCsp = it.getArgument(1)
                null
            }

            val result = controller.serveFrontend(request, response)

            // Extract nonce from HTML
            val htmlNonceMatch = Regex("""nonce="([^"]+)"""").find(result)
            assertNotNull(htmlNonceMatch, "Nonce should be in HTML")
            val htmlNonce = htmlNonceMatch!!.groupValues[1]

            // Extract nonce from CSP header
            assertNotNull(capturedCsp, "CSP header should be set")
            val cspNonceMatch = Regex("""'nonce-([^']+)'""").find(capturedCsp!!)
            assertNotNull(cspNonceMatch, "Nonce should be in CSP header")
            val cspNonce = cspNonceMatch!!.groupValues[1]

            assertEquals(htmlNonce, cspNonce, "Nonce in HTML should match nonce in CSP header")
        }
    }

    @Nested
    inner class Routes {
        @Test
        fun `should serve index html for public DSFR pages`() {
            val mapping = FrontendRoutesController::class.java
                .getMethod("serveFrontend", HttpServletRequest::class.java, HttpServletResponse::class.java)
                .getAnnotation(GetMapping::class.java)

            assertTrue(
                mapping.value.toList().containsAll(
                    listOf("/accessibilite", "/mentions-legales", "/donnees-personnelles", "/plan-du-site")
                ),
                "Public pages must be served by the SPA, otherwise a direct load returns 404"
            )
        }
    }

    @Nested
    inner class RobotsHeader {
        @Test
        fun `should tell crawlers not to index nor follow`() {
            controller = createControllerWithHtml("<html><head></head><body></body></html>")

            whenever(request.isSecure).thenReturn(true)

            controller.serveFrontend(request, response)

            verify(response).setHeader("X-Robots-Tag", "noindex, nofollow")
        }
    }
}
