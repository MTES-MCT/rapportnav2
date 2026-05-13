package fr.gouv.gmampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.use_cases.service.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetServiceForUser
import fr.gouv.dgampa.rapportnav.infrastructure.api.ControllersExceptionHandler
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.ServiceManageController
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext


@WebMvcTest(ServiceManageController::class)
@Import(ServiceManageControllerTest.TestSecurityConfig::class)
@ContextConfiguration(classes = [ServiceManageController::class, ControllersExceptionHandler::class])
class ServiceManageControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getAgent: GetAllAgent

    @MockitoBean
    private lateinit var deleteAgent: DeleteAgent

    @MockitoBean
    private lateinit var disableAgent: DisableAgent

    @MockitoBean
    private lateinit var migrateAgent: MigrateAgent

    @MockitoBean
    private lateinit var createOrUpdateAgent: CreateOrUpdateAgent

    @MockitoBean
    private lateinit var getCrewByServiceId: GetCrewByServiceId

    @MockitoBean
    private lateinit var updateResource: UpdateResource

    @MockitoBean
    private lateinit var getServiceForUser: GetServiceForUser

    @Autowired
    lateinit var context: WebApplicationContext

    val input = """
                    {
                        "id": 1,
                        "name": "Updated resource",
                        "controlUnitId": 10,
                        "registrationId": "REG-123",
                        "radioFrequency": "VHF-16"
                    }
                    """.trimIndent()

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
                    it.requestMatchers("/api/v2/manage/**").hasAnyAuthority("ROLE_MANAGER_PAM", "ROLE_MANAGER_ULAM")
                        .anyRequest().authenticated()
                }
                .httpBasic(withDefaults())
                .csrf { it.disable() }
                .build()
        }
    }

    @Test
    @WithMockUser(authorities = ["ROLE_USER_ULAM"])
    fun `Should return 403 when user has no manage role try to delete user`() {
        mockMvc.perform(delete("/api/v2/manage/services/2/agents/3"))
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_MANAGER_ULAM"])
    fun `Should not return 403 when user with manage role try to delete user`() {
        `when`(getServiceForUser.execute()).thenReturn(
            ServiceEntityMock.create(id = 2)
        )
        mockMvc.perform(delete("/api/v2/manage/services/2/agents/3"))
            .andExpect(status().isOk)
    }


    @Test
    @WithMockUser(authorities = ["ROLE_MANAGER_ULAM"])
    fun `Should update resource when user owns service and control unit`() {
        `when`(getServiceForUser.execute()).thenReturn(
            ServiceEntityMock.create(
                id = 2,
                controlUnits = listOf(10)
            )
        )
        `when`(updateResource.execute(any())).thenReturn(null)

        mockMvc.perform(
            put("/api/v2/manage/services/2/resources")
                .contentType(MediaType.APPLICATION_JSON)
                .content(input)
        )
            .andExpect(status().isOk)

        verify(updateResource).execute(any())
    }


    @Test
    @WithMockUser(authorities = ["ROLE_MANAGER_ULAM"])
    fun `Should not update resource when user is not from the service`() {
        `when`(getServiceForUser.execute()).thenReturn(
            ServiceEntityMock.create(
                id = 3,
                controlUnits = listOf(10)
            )
        )
        `when`(updateResource.execute(any())).thenReturn(null)

        mockMvc.perform(
            put("/api/v2/manage/services/2/resources")
                .contentType(MediaType.APPLICATION_JSON)
                .content(input)
        )
            .andExpect(status().isBadRequest)
    }


    @Test
    @WithMockUser(authorities = ["ROLE_MANAGER_ULAM"])
    fun `Should not update resource when the control unit is not from the service`() {
        `when`(getServiceForUser.execute()).thenReturn(
            ServiceEntityMock.create(
                id = 2,
                controlUnits = listOf(11)
            )
        )
        `when`(updateResource.execute(any())).thenReturn(null)

        mockMvc.perform(
            put("/api/v2/manage/services/2/resources")
                .contentType(MediaType.APPLICATION_JSON)
                .content(input)
        )
            .andExpect(status().isBadRequest)
    }
}
