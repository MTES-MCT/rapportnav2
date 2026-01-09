package fr.gouv.gmampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.config.JacksonConfig
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.ApiAdminController
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@WebMvcTest(ApiAdminController::class)
@Import(ApiAdminControllerSimpleTest.TestSecurityConfig::class)
@ContextConfiguration(classes = [ApiAdminController::class, JacksonConfig::class])
class ApiAdminControllerSimpleTest {

    @Autowired
    lateinit var context: WebApplicationContext

    @Autowired
    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()
    }

    @TestConfiguration
    @EnableWebSecurity
    class TestSecurityConfig {
        @Bean
        fun filterChain(http: HttpSecurity): SecurityFilterChain {
            return http
                .authorizeHttpRequests {
                    it.requestMatchers("/api/v2/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                }
                .httpBasic(withDefaults())
                .csrf { it.disable() }
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
            .andExpect(content().string("Yes I'm admin secured..."))
    }
}
