package fr.gouv.gmampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.config.SecurityConfig
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamBackfillReport
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.BackfillPamResourceUsage
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.ResourceUsageAdminController
import fr.gouv.dgampa.rapportnav.infrastructure.api.filter.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.infrastructure.api.filter.CustomAuthenticationFilter
import fr.gouv.dgampa.rapportnav.infrastructure.api.filter.SentryUserContextFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

/**
 * The backfill writes in bulk: check the real SecurityConfig restricts it to ROLE_ADMIN.
 * The authentication filters are mocked to pass the request on (a plain mock would swallow it and every call
 * would answer 200), so the authorization rules are what decides the outcome.
 */
@WebMvcTest(ResourceUsageAdminController::class)
@Import(ResourceUsageAdminControllerSecurityTest.PassThroughFilters::class, SecurityConfig::class)
@ContextConfiguration(
    classes = [
        ResourceUsageAdminController::class,
        ResourceUsageAdminControllerSecurityTest.PassThroughFilters::class,
        SecurityConfig::class
    ]
)
@TestPropertySource(properties = ["metabase.site-url=https://metabase.example"])
class ResourceUsageAdminControllerSecurityTest {

    @Autowired
    lateinit var context: WebApplicationContext

    @MockitoBean
    lateinit var backfillPamResourceUsage: BackfillPamResourceUsage

    lateinit var mockMvc: MockMvc

    @TestConfiguration
    class PassThroughFilters {
        @Bean
        fun customAuthenticationFilter(): CustomAuthenticationFilter = passThrough(mock())

        @Bean
        fun apiKeyAuthenticationFilter(): ApiKeyAuthenticationFilter = passThrough(mock())

        @Bean
        fun sentryUserContextFilter(): SentryUserContextFilter = SentryUserContextFilter()

        private fun <T : jakarta.servlet.Filter> passThrough(filter: T): T = filter.also {
            doAnswer { invocation ->
                (invocation.arguments[2] as FilterChain).doFilter(
                    invocation.arguments[0] as ServletRequest,
                    invocation.arguments[1] as ServletResponse
                )
            }.whenever(it).doFilter(any(), any(), any())
        }
    }

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()
        whenever(backfillPamResourceUsage.execute(any(), any())).thenReturn(
            PamBackfillReport(dryRun = true, scanned = 0, created = 0, updated = 0, unchanged = 0, skipped = listOf())
        )
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `admin can run the backfill`() {
        mockMvc.perform(post("/api/v2/admin/resource-usage/backfill-pam"))
            .andExpect(status().isOk)

        verify(backfillPamResourceUsage).execute(any(), any())
    }

    @Test
    @WithMockUser(authorities = ["ROLE_USER_PAM"])
    fun `a PAM user is refused`() {
        mockMvc.perform(post("/api/v2/admin/resource-usage/backfill-pam?dryRun=false"))
            .andExpect(status().isForbidden)

        verify(backfillPamResourceUsage, never()).execute(any(), any())
    }

    @Test
    @WithMockUser(authorities = ["ROLE_MANAGER_PAM"])
    fun `a PAM manager is refused`() {
        mockMvc.perform(post("/api/v2/admin/resource-usage/backfill-pam?dryRun=false"))
            .andExpect(status().isForbidden)

        verify(backfillPamResourceUsage, never()).execute(any(), any())
    }

    @Test
    fun `an anonymous call is refused`() {
        mockMvc.perform(post("/api/v2/admin/resource-usage/backfill-pam?dryRun=false"))
            .andExpect { result -> check(result.response.status in listOf(401, 403)) { "status=${result.response.status}" } }

        verify(backfillPamResourceUsage, never()).execute(any(), any())
    }
}
