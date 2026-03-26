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

    private fun createControllerWithHtml(htmlContent: String): FrontendRoutesController {
        val indexFile = File(tempDir.toFile(), "index.html")
        indexFile.writeText(htmlContent)
        return FrontendRoutesController(tempDir.toString())
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
}