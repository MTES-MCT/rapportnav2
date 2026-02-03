package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.config.CustomAuthenticationFilter
import fr.gouv.dgampa.rapportnav.config.SecurityConfig
import fr.gouv.dgampa.rapportnav.domain.entities.user.AuthoritiesEnum
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.stereotype.Controller
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.context.WebApplicationContext

class SecurityConfigTest {

    private lateinit var customAuthFilter: CustomAuthenticationFilter
    private lateinit var apiKeyAuthFilter: ApiKeyAuthenticationFilter
    private lateinit var securityConfig: SecurityConfig

    @BeforeEach
    fun setUp() {
        customAuthFilter = mock()
        apiKeyAuthFilter = mock()
        securityConfig = SecurityConfig(customAuthFilter, apiKeyAuthFilter)
    }

    @Test
    fun `should have Configuration annotation`() {
        val annotation = SecurityConfig::class.java.getAnnotation(Configuration::class.java)
        assertNotNull(annotation)
    }

    @Test
    fun `should have EnableWebSecurity with debug enabled`() {
        val annotation = SecurityConfig::class.java.getAnnotation(EnableWebSecurity::class.java)
        assertNotNull(annotation)
        assertTrue(annotation.debug)
    }

    @Test
    fun `apiKeySecurityFilter method should have Order 1 annotation`() {
        val method = SecurityConfig::class.java.getMethod("apiKeySecurityFilter", HttpSecurity::class.java)
        val orderAnnotation = method.getAnnotation(Order::class.java)
        assertNotNull(orderAnnotation)
        assertEquals(1, orderAnnotation.value)
    }

    @Test
    fun `filterChain method should have Order 2 annotation`() {
        val method = SecurityConfig::class.java.getMethod("filterChain", HttpSecurity::class.java)
        val orderAnnotation = method.getAnnotation(Order::class.java)
        assertNotNull(orderAnnotation)
        assertEquals(2, orderAnnotation.value)
    }

    @Test
    fun `should be instantiable with required dependencies`() {
        val config = SecurityConfig(customAuthFilter, apiKeyAuthFilter)
        assertNotNull(config)
    }

    @Test
    fun `apiKeySecurityFilter method should exist and return SecurityFilterChain`() {
        val method = SecurityConfig::class.java.getMethod("apiKeySecurityFilter", HttpSecurity::class.java)
        assertNotNull(method)
        assertEquals(
            org.springframework.security.web.SecurityFilterChain::class.java,
            method.returnType
        )
    }

    @Test
    fun `filterChain method should exist and return SecurityFilterChain`() {
        val method = SecurityConfig::class.java.getMethod("filterChain", HttpSecurity::class.java)
        assertNotNull(method)
        assertEquals(
            org.springframework.security.web.SecurityFilterChain::class.java,
            method.returnType
        )
    }

    @Test
    fun `SecurityConfig should accept non-null filter dependencies`() {
        val customFilter: CustomAuthenticationFilter = mock()
        val apiKeyFilter: ApiKeyAuthenticationFilter = mock()

        val config = SecurityConfig(customFilter, apiKeyFilter)

        assertNotNull(config)
    }

    @Test
    fun `apiKeySecurityFilter should have Bean annotation`() {
        val method = SecurityConfig::class.java.getMethod("apiKeySecurityFilter", HttpSecurity::class.java)
        val beanAnnotation = method.getAnnotation(org.springframework.context.annotation.Bean::class.java)
        assertNotNull(beanAnnotation)
    }

    @Test
    fun `filterChain should have Bean annotation`() {
        val method = SecurityConfig::class.java.getMethod("filterChain", HttpSecurity::class.java)
        val beanAnnotation = method.getAnnotation(org.springframework.context.annotation.Bean::class.java)
        assertNotNull(beanAnnotation)
    }
}

/**
 * Test controller providing endpoints to test security rules
 */
@Controller
class SecurityTestController {
    @GetMapping("/api/v1/auth/login")
    @ResponseBody
    fun login(): String = "login"

    @PostMapping("/api/v1/auth/login")
    @ResponseBody
    fun loginPost(): String = "login"

    @PostMapping("/api/v1/auth/register")
    @ResponseBody
    fun register(): String = "register"

    @GetMapping("/api/v2/missions")
    @ResponseBody
    fun missions(): String = "missions"

    @GetMapping("/api/v2/admin/test")
    @ResponseBody
    fun adminTest(): String = "admin"

    @GetMapping("/api/analytics/test")
    @ResponseBody
    fun analyticsTest(): String = "analytics"

    @GetMapping("/")
    @ResponseBody
    fun root(): String = "root"
}

/**
 * Integration tests using the real SecurityConfig class.
 * Tests security headers, session management, and CORS configuration.
 *
 * Note: Authorization denial tests require a full @SpringBootTest context
 * to properly validate access control rules.
 */
@WebMvcTest(SecurityTestController::class)
@Import(SecurityConfigIntegrationTest.MockFilterConfig::class, SecurityConfig::class)
@org.springframework.test.context.ContextConfiguration(
    classes = [
        SecurityTestController::class,
        SecurityConfigIntegrationTest.MockFilterConfig::class,
        SecurityConfig::class
    ]
)
class SecurityConfigIntegrationTest {

    @Autowired
    lateinit var context: WebApplicationContext

    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()
    }

    /**
     * Provides mock beans for SecurityConfig dependencies
     */
    @TestConfiguration
    class MockFilterConfig {
        @Bean
        fun customAuthenticationFilter(): CustomAuthenticationFilter = mock()

        @Bean
        fun apiKeyAuthenticationFilter(): ApiKeyAuthenticationFilter = mock()
    }

    @Nested
    inner class PublicEndpoints {
        @Test
        fun `login POST endpoint should be publicly accessible`() {
            mockMvc.perform(
                post("/api/v1/auth/login")
                    .contentType("application/json")
                    .content("""{"email":"test@test.com","password":"password"}""")
            )
                .andExpect(status().isOk)
        }

        @Test
        fun `login GET endpoint should be publicly accessible`() {
            mockMvc.perform(get("/api/v1/auth/login"))
                .andExpect(status().isOk)
        }

        @Test
        fun `root endpoint should be publicly accessible`() {
            mockMvc.perform(get("/"))
                .andExpect(status().isOk)
        }
    }

    @Nested
    inner class AuthenticatedEndpoints {
        @Test
        @WithMockUser(authorities = ["ROLE_USER_PAM"])
        fun `api v2 endpoints should be accessible with PAM role`() {
            mockMvc.perform(get("/api/v2/missions"))
                .andExpect(status().isOk)
        }

        @Test
        @WithMockUser(authorities = ["ROLE_USER_ULAM"])
        fun `api v2 endpoints should be accessible with ULAM role`() {
            mockMvc.perform(get("/api/v2/missions"))
                .andExpect(status().isOk)
        }

        @Test
        @WithMockUser(authorities = ["ROLE_ADMIN"])
        fun `api v2 endpoints should be accessible with ADMIN role`() {
            mockMvc.perform(get("/api/v2/missions"))
                .andExpect(status().isOk)
        }

        @Test
        @WithMockUser(authorities = ["ROLE_ADMIN"])
        fun `admin endpoints should be accessible with admin role`() {
            mockMvc.perform(get("/api/v2/admin/test"))
                .andExpect(status().isOk)
        }

        @Test
        @WithMockUser(authorities = ["ROLE_ADMIN"])
        fun `register endpoint should be accessible with admin role`() {
            mockMvc.perform(
                post("/api/v1/auth/register")
                    .contentType("application/json")
                    .content("""{"email":"new@test.com","password":"password"}""")
            )
                .andExpect(status().isOk)
        }

        @Test
        @WithMockUser(authorities = ["ROLE_API_USER"])
        fun `analytics endpoints should be accessible with API user role`() {
            mockMvc.perform(get("/api/analytics/test"))
                .andExpect(status().isOk)
        }
    }

    @Nested
    inner class SecurityHeaders {
        @Test
        @WithMockUser
        fun `response should include CSP header with default-src self`() {
            mockMvc.perform(get("/api/v2/missions"))
                .andExpect { result ->
                    val cspHeader = result.response.getHeader("Content-Security-Policy")
                    assertNotNull(cspHeader, "CSP header should be present")
                    assertTrue(cspHeader!!.contains("default-src 'self'"), "CSP should contain default-src 'self'")
                }
        }

        @Test
        @WithMockUser
        fun `response should include CSP header preventing framing`() {
            mockMvc.perform(get("/api/v2/missions"))
                .andExpect { result ->
                    val cspHeader = result.response.getHeader("Content-Security-Policy")
                    assertNotNull(cspHeader, "CSP header should be present")
                    assertTrue(cspHeader!!.contains("frame-ancestors 'none'"), "CSP should prevent framing")
                }
        }

        @Test
        @WithMockUser
        fun `CSP should include script-src directive`() {
            mockMvc.perform(get("/api/v2/missions"))
                .andExpect { result ->
                    val cspHeader = result.response.getHeader("Content-Security-Policy")
                    assertNotNull(cspHeader)
                    assertTrue(cspHeader!!.contains("script-src"), "CSP should contain script-src directive")
                }
        }

        @Test
        @WithMockUser
        fun `CSP should include connect-src directive`() {
            mockMvc.perform(get("/api/v2/missions"))
                .andExpect { result ->
                    val cspHeader = result.response.getHeader("Content-Security-Policy")
                    assertNotNull(cspHeader)
                    assertTrue(cspHeader!!.contains("connect-src"), "CSP should contain connect-src directive")
                }
        }

        @Test
        @WithMockUser
        fun `CSP should include trusted-types directive`() {
            mockMvc.perform(get("/api/v2/missions"))
                .andExpect { result ->
                    val cspHeader = result.response.getHeader("Content-Security-Policy")
                    assertNotNull(cspHeader)
                    assertTrue(cspHeader!!.contains("require-trusted-types-for"), "CSP should contain trusted-types directive")
                }
        }

        @Test
        @WithMockUser
        fun `response should include X-Frame-Options DENY header`() {
            mockMvc.perform(get("/api/v2/missions"))
                .andExpect { result ->
                    val frameOptions = result.response.getHeader("X-Frame-Options")
                    assertNotNull(frameOptions, "X-Frame-Options header should be present")
                    assertEquals("DENY", frameOptions, "X-Frame-Options should be DENY")
                }
        }

        @Test
        @WithMockUser
        fun `CSP should allow Sentry connections`() {
            mockMvc.perform(get("/api/v2/missions"))
                .andExpect { result ->
                    val cspHeader = result.response.getHeader("Content-Security-Policy")
                    assertNotNull(cspHeader)
                    assertTrue(cspHeader!!.contains("sentry.incubateur.net"), "CSP should allow Sentry connections")
                }
        }

        @Test
        @WithMockUser
        fun `CSP should allow entreprises API connections`() {
            mockMvc.perform(get("/api/v2/missions"))
                .andExpect { result ->
                    val cspHeader = result.response.getHeader("Content-Security-Policy")
                    assertNotNull(cspHeader)
                    assertTrue(cspHeader!!.contains("recherche-entreprises.api.gouv.fr"), "CSP should allow entreprises API")
                }
        }
    }

    @Nested
    inner class SessionManagement {
        @Test
        @WithMockUser
        fun `session should be stateless - no JSESSIONID cookie`() {
            mockMvc.perform(get("/api/v2/missions"))
                .andExpect { result ->
                    val setCookieHeader = result.response.getHeader(HttpHeaders.SET_COOKIE)
                    val hasSessionCookie = setCookieHeader?.contains("JSESSIONID") == true
                    assertFalse(hasSessionCookie, "Session should be stateless - no JSESSIONID cookie expected")
                }
        }
    }

    @Nested
    inner class CorsConfiguration {
        @Test
        @WithMockUser
        fun `CORS should allow cross-origin requests`() {
            mockMvc.perform(
                get("/api/v2/missions")
                    .header("Origin", "http://localhost:3000")
            )
                .andExpect(status().isOk)
        }
    }
}
