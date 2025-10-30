package fr.gouv.gmampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.ApiAdminController
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.web.SecurityFilterChain
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ApiAdminController::class)
@ContextConfiguration(classes = [ApiAdminController::class, ApiAdminControllerSimpleTest.TestSecurityConfig::class])
class ApiAdminControllerSimpleTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @TestConfiguration
    @EnableWebSecurity
    class TestSecurityConfig {
        @Bean
        fun filterChain(http: HttpSecurity): SecurityFilterChain {
            return http
                .authorizeHttpRequests { authorize ->
                    authorize
                        .requestMatchers("/api/v2/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                }
                .csrf { it.disable() }
                .httpBasic { } // Add basic auth for testing
                .build()
        }
    }

    @Test
    fun `Should return 401 when no authentication`() {
        mockMvc.perform(get("/api/v2/admin/test"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_USER"])
    fun `Should return 403 when user has no admin role`() {
        mockMvc.perform(get("/api/v2/admin/test"))
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `Should return 200 when user has admin role`() {
        mockMvc.perform(get("/api/v2/admin/test"))
            .andExpect(status().isOk)
    }
}
