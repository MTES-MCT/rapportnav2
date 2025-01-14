package fr.gouv.gmampa.rapportnav.infrastructure.monitorenv.v2

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.APIEnvControlUnitRepository
import fr.gouv.dgampa.rapportnav.infrastructure.utils.GsonSerializer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.argThat
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [APIEnvControlUnitRepository::class])
class APIEnvControlUnitRepositoryTest {

    val legacyControlUnit = LegacyControlUnitEntity(
        name = "test-pam",
        administration = "paris",
        isArchived = false,
        id = 1,
        resources = listOf()
    )

    val host = "https://monitorenv.din.developpement-durable.gouv.fr"

    @MockitoBean
    private lateinit var objectMapper: ObjectMapper

    @Mock
    private var httpResponse = Mockito.mock<HttpResponse<String>>()


    @MockitoBean
    private lateinit var httpClientFactory: HttpClientFactory

    @Mock
    private var httpClient: HttpClient = Mockito.mock(HttpClient::class.java)


    @Test
    fun `execute should fet controlUnits from monitorenv`() {

        Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
        Mockito.`when`(httpResponse.body()).thenReturn(getMissionString())
        Mockito.`when`(
            httpClient.send(
                Mockito.any(HttpRequest::class.java),
                Mockito.any<HttpResponse.BodyHandler<String>>()
            )
        )
            .thenReturn(httpResponse)
        val envRepo = APIEnvControlUnitRepository(mapper = objectMapper, clientFactory = httpClientFactory)
        envRepo.findAll()
        verify(httpClient).send(
            argThat { request ->
                request.uri().equals(URI.create("$host/api/v1/control_units"))
            },
            Mockito.any<HttpResponse.BodyHandler<String>>()
        )

    }


    private fun getMissionString(): String {
        val gson: Gson = GsonSerializer().create()
        return gson.toJson(listOf(legacyControlUnit))
    }
}
