package fr.gouv.gmampa.rapportnav.infrastructure.api.bff.v2

import com.nimbusds.jwt.SignedJWT
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.MetabaseEmbedController
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(MetabaseEmbedController::class)
@ContextConfiguration(classes = [MetabaseEmbedController::class])
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = [
    "metabase.secret-key=a]test-secret-key-that-is-at-least-256-bits-long-for-hs256!!",
    "metabase.site-url=https://metabase.din.developpement-durable.gouv.fr"
])
class MetabaseEmbedControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getServiceById: GetServiceById

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    private fun authenticateUser(serviceId: Int? = null, serviceType: ServiceTypeEnum = ServiceTypeEnum.PAM) {
        val user = User(
            id = 1,
            serviceId = serviceId,
            firstName = "John",
            lastName = "Doe",
            email = "john@test.fr",
            password = "hashed",
            roles = listOf(RoleTypeEnum.USER_PAM)
        )
        if (serviceId != null) {
            `when`(getServiceById.execute(serviceId)).thenReturn(
                ServiceEntity(id = serviceId, name = "Test Service", serviceType = serviceType)
            )
        }
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(user, "", emptyList())
    }

    @Test
    fun `should return 401 when no user in security context`() {
        mockMvc.perform(get("/api/v2/metabase/embed-url"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should return 200 with iframeUrl when user is authenticated`() {
        authenticateUser(serviceId = 1)

        mockMvc.perform(get("/api/v2/metabase/embed-url"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.iframeUrl").exists())
            .andExpect(jsonPath("$.iframeUrl").value(
                org.hamcrest.Matchers.startsWith("https://metabase.din.developpement-durable.gouv.fr/embed/dashboard/")
            ))
    }

    @Test
    fun `should include correct unit param for PAM service`() {
        authenticateUser(serviceId = 3) // PAM Themis

        val result = mockMvc.perform(get("/api/v2/metabase/embed-url"))
            .andExpect(status().isOk)
            .andReturn()

        val iframeUrl = extractIframeUrl(result.response.contentAsString)
        val claims = parseJwtClaims(iframeUrl)
        val params = claims.getJSONObjectClaim("params")

        assertEquals("PAM Themis", params["unit%C3%A9"])
    }

    @Test
    fun `should include correct unit param for ULAM service`() {
        authenticateUser(serviceId = 9, serviceType = ServiceTypeEnum.ULAM) // "33"

        val result = mockMvc.perform(get("/api/v2/metabase/embed-url"))
            .andExpect(status().isOk)
            .andReturn()

        val iframeUrl = extractIframeUrl(result.response.contentAsString)
        val claims = parseJwtClaims(iframeUrl)
        val params = claims.getJSONObjectClaim("params")

        assertEquals("33", params["unit%C3%A9"])
    }

    @Test
    fun `should set null unit param when serviceId has no mapping`() {
        authenticateUser(serviceId = 999)

        val result = mockMvc.perform(get("/api/v2/metabase/embed-url"))
            .andExpect(status().isOk)
            .andReturn()

        val iframeUrl = extractIframeUrl(result.response.contentAsString)
        val claims = parseJwtClaims(iframeUrl)
        val params = claims.getJSONObjectClaim("params")

        assertNull(params["unit%C3%A9"])
    }

    @Test
    fun `should use PAM dashboard for PAM service`() {
        authenticateUser(serviceId = 1, serviceType = ServiceTypeEnum.PAM)

        val result = mockMvc.perform(get("/api/v2/metabase/embed-url"))
            .andExpect(status().isOk)
            .andReturn()

        val iframeUrl = extractIframeUrl(result.response.contentAsString)
        val claims = parseJwtClaims(iframeUrl)
        val resource = claims.getJSONObjectClaim("resource")

        assertEquals(436L, resource["dashboard"])
    }

    @Test
    fun `should use ULAM dashboard for ULAM service`() {
        authenticateUser(serviceId = 9, serviceType = ServiceTypeEnum.ULAM)

        val result = mockMvc.perform(get("/api/v2/metabase/embed-url"))
            .andExpect(status().isOk)
            .andReturn()

        val iframeUrl = extractIframeUrl(result.response.contentAsString)
        val claims = parseJwtClaims(iframeUrl)
        val resource = claims.getJSONObjectClaim("resource")

        assertEquals(491L, resource["dashboard"])
    }

    private fun extractIframeUrl(json: String): String {
        val regex = """"iframeUrl"\s*:\s*"([^"]+)"""".toRegex()
        return regex.find(json)!!.groupValues[1]
    }

    private fun parseJwtClaims(iframeUrl: String): com.nimbusds.jwt.JWTClaimsSet {
        val token = iframeUrl
            .substringAfter("/embed/dashboard/")
            .substringBefore("#")
        return SignedJWT.parse(token).jwtClaimsSet
    }
}
