package fr.gouv.gmampa.rapportnav.infrastructure.monitorenv.v2

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitResourceType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.APIEnvControlUnitRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class APIEnvControlUnitRepositoryFindByIdTest {

    private val host = "http://localhost:8089"

    // Same settings as JacksonConfig.
    private val mapper = JsonMapper.builder()
        .addModule(KotlinModule.Builder().build())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
        .build()

    private val httpClient: HttpClient = mock()
    private val httpResponse: HttpResponse<String> = mock()
    private lateinit var repository: APIEnvControlUnitRepository

    @BeforeEach
    fun setup() {
        val clientFactory: HttpClientFactory = mock()
        whenever(clientFactory.create()).thenReturn(httpClient)
        whenever(httpClient.send(any<HttpRequest>(), any<HttpResponse.BodyHandler<String>>())).thenReturn(httpResponse)
        repository = APIEnvControlUnitRepository(clientFactory = clientFactory, mapper = mapper, host = host)
    }

    private fun respond(status: Int, body: String) {
        whenever(httpResponse.statusCode()).thenReturn(status)
        whenever(httpResponse.body()).thenReturn(body)
    }

    @Test
    fun `maps the control unit and all its resources from api v2 control_units`() {
        respond(
            200,
            """
            {
              "id": 10404,
              "name": "PAM Iris",
              "isArchived": false,
              "administrationId": 1,
              "administration": { "id": 1, "name": "DAM", "isArchived": false },
              "controlUnitContacts": [],
              "controlUnitResources": [
                { "id": 582, "controlUnitId": 10404, "isArchived": false, "name": "Iris", "type": "PATROL_BOAT", "station": { "id": 1 } },
                { "id": 100, "controlUnitId": 10404, "isArchived": true, "name": "Ancien", "type": "PATROL_BOAT" },
                { "id": 101, "controlUnitId": 10404, "isArchived": false, "name": "Nouveau", "type": "HOVERCRAFT" }
              ]
            }
            """.trimIndent()
        )

        val controlUnit = repository.findById(10404)

        verify(httpClient).send(
            argThat<HttpRequest> { uri() == URI.create("$host/api/v2/control_units/10404") },
            any<HttpResponse.BodyHandler<String>>()
        )
        assertThat(controlUnit.id).isEqualTo(10404)
        assertThat(controlUnit.name).isEqualTo("PAM Iris")
        assertThat(controlUnit.administration).isEqualTo("DAM")
        assertThat(controlUnit.isArchived).isFalse()
        // The repository returns every resource as-is: filtering (archived, type) is the callers' business.
        assertThat(controlUnit.resources).containsExactly(
            ControlResourceEntity(id = 582, name = "Iris", type = ControlUnitResourceType.PATROL_BOAT, isArchived = false),
            ControlResourceEntity(id = 100, name = "Ancien", type = ControlUnitResourceType.PATROL_BOAT, isArchived = true),
            ControlResourceEntity(id = 101, name = "Nouveau", type = null, isArchived = false)
        )
    }

    @Test
    fun `throws when MonitorEnv answers an error status`() {
        respond(404, "Not found")

        val e = assertThrows<BackendInternalException> { repository.findById(99999) }
        assertThat(e.message).contains("404").contains("/api/v2/control_units/99999")
    }

    @Test
    fun `throws when MonitorEnv can't be reached`() {
        whenever(httpClient.send(any<HttpRequest>(), any<HttpResponse.BodyHandler<String>>())).thenThrow(IOException("Connection refused"))

        assertThrows<BackendInternalException> { repository.findById(10404) }
    }
}
