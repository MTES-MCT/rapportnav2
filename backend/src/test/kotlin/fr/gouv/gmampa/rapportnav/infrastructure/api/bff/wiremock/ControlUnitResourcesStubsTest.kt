package fr.gouv.gmampa.rapportnav.infrastructure.api.bff.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitResourceType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.ControlUnitResourcesStubs
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.utils.LoadJsonData
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.APIEnvControlUnitRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule

/**
 * Local dev can't reach MonitorEnv: check the WireMock stubs serve GET /api/v2/control_units/{id} end to end,
 * through the real HTTP call and mapping. Runs on a dynamic port so it doesn't clash with a local backend on 8089.
 */
class ControlUnitResourcesStubsTest {

    private lateinit var server: WireMockServer
    private lateinit var repository: APIEnvControlUnitRepository

    @BeforeEach
    fun setup() {
        server = WireMockServer(WireMockConfiguration.options().dynamicPort())
        server.start()
        ControlUnitResourcesStubs.configureStubs(server)

        val mapper = JsonMapper.builder()
            .addModule(KotlinModule.Builder().build())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build()
        repository = APIEnvControlUnitRepository(HttpClientFactory(), mapper, "http://localhost:${server.port()}")
    }

    @AfterEach
    fun tearDown() {
        server.stop()
    }

    @Test
    fun `serves PAM Iris with its administration, ship and car`() {
        val controlUnit = repository.findById(10404)

        assertThat(controlUnit.id).isEqualTo(10404)
        assertThat(controlUnit.name).isEqualTo("PAM Iris")
        assertThat(controlUnit.administration).isNotBlank()
        assertThat(controlUnit.resources.filter { it.type == ControlUnitResourceType.PATROL_BOAT && !it.isArchived }.map { it.id to it.name })
            .containsExactly(582 to "Iris")
        assertThat(controlUnit.resources.map { it.type }).contains(ControlUnitResourceType.CAR)
    }

    @Test
    fun `serves every PAM unit of the stub data`() {
        listOf(10141, 10404, 10121, 10345, 10080, 10519).forEach { controlUnitId ->
            assertThat(repository.findById(controlUnitId).resources).isNotEmpty
        }
    }

    @Test
    fun `answers 404 for an unknown control unit, like MonitorEnv`() {
        assertThrows<BackendInternalException> { repository.findById(99999999) }
    }

    @Test
    fun `stubs every control unit of the resources file`() {
        val resourcesJson = LoadJsonData.load("env/control_unit_resources.json")!!
        val nodes = JsonMapper().readTree(resourcesJson)
        val unitIds = (0 until nodes.size()).map { nodes.get(it).get("controlUnitId").asInt() }.toSet()

        assertThat(ControlUnitResourcesStubs.fullControlUnitsById(resourcesJson).keys).isEqualTo(unitIds)
    }
}
