package fr.gouv.gmampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.config.SecurityConfig
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.ApiAdminController
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension



@Disabled("TODO: Fix PathPatternRequestMatcher SecurityConfig")
@WebMvcTest()
@Import(SecurityConfig::class)
@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc(addFilters = true)
@ContextConfiguration(classes = [ApiAdminController::class, TokenService::class, JwtEncoder::class, JwtDecoder::class])
class ApiAdminControllerITest {

    @Autowired
    private lateinit var api: MockMvc

    @MockitoBean
    private lateinit var jwtDecoder: JwtDecoder

    @MockitoBean
    private lateinit var jwtEncoder: JwtEncoder

    @MockitoBean
    private lateinit var userRepository: IUserRepository

    @Autowired
    private lateinit var tokenService: TokenService

    @Test
    fun `Should return 401 if the user has not JWT token`() {
        val t = api.perform(
            get("/api/v1/admin/test")
                .header("X-Xsrf-Token", "b321f985-a7dd-47ea-a1e0-d05d8791dbd8")
        )
        t.andExpect(status().isUnauthorized)
    }

    @Test
    fun `Should return 403 if the user is has not admin role`() {
        val t = api.perform(
            get("/api/v1/admin/test")
                .header("X-Xsrf-Token", "b321f985-a7dd-47ea-a1e0-d05d8791dbd8")
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_USER")))
        )
        t.andExpect(status().isForbidden)
    }

    @Test
    fun `Should return 200 if the user is has admin role`() {
        val t = api.perform(
            get("/api/v1/admin/test")
                .header("X-Xsrf-Token", "b321f985-a7dd-47ea-a1e0-d05d8791dbd8")
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_ADMIN")))
        )
        t.andExpect(status().isOk)
    }
}
