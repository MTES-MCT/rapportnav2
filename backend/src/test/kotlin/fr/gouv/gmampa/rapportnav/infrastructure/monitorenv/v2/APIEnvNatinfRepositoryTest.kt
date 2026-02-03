package fr.gouv.gmampa.rapportnav.infrastructure.monitorenv.v2

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.config.JacksonConfig
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.APIEnvNatinfRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.argThat
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [APIEnvNatinfRepository::class])
@ContextConfiguration(classes = [JacksonConfig::class])
class APIEnvNatinfRepositoryTest {

    private val host = "https://monitorenv.gouv.fr"

    @MockitoBean
    private lateinit var mapper: JsonMapper

    @MockitoBean
    private var httpResponse: HttpResponse<String> = mock()

    @MockitoBean
    private lateinit var httpClientFactory: HttpClientFactory

    @MockitoBean
    private var httpClient: HttpClient = mock(HttpClient::class.java)

    private fun createRealMapper(): JsonMapper = JsonMapper.builder()
        .addModule(KotlinModule.Builder().build())
        .build()

    @Nested
    inner class FindAll {

        @Test
        fun `should fetch all natinfs and return list`() {
            val realMapper = createRealMapper()
            val natinfsJson = """
                [
                    {
                        "natinfCode": 27718,
                        "regulation": "Fishing Regulation",
                        "infractionCategory": "Fishing",
                        "infraction": "Illegal landing"
                    },
                    {
                        "natinfCode": 27719,
                        "regulation": "Maritime Regulation",
                        "infractionCategory": "Navigation",
                        "infraction": "Illegal navigation"
                    }
                ]
            """.trimIndent()

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
            Mockito.`when`(httpResponse.body()).thenReturn(natinfsJson)
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val repository = APIEnvNatinfRepository(
                clientFactory = httpClientFactory,
                mapper = realMapper,
                host = host
            )

            val result = repository.findAll()

            assertEquals(2, result.size)
            assertEquals(27718, result[0].natinfCode)
            assertEquals("Illegal landing", result[0].infraction)
        }

        @Test
        fun `should call correct URL for natinfs`() {
            val realMapper = createRealMapper()

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
            Mockito.`when`(httpResponse.body()).thenReturn("[]")
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val repository = APIEnvNatinfRepository(
                clientFactory = httpClientFactory,
                mapper = realMapper,
                host = host
            )

            repository.findAll()

            verify(httpClient).send(
                argThat { request ->
                    request.uri() == URI.create("$host/bff/v1/natinfs")
                },
                Mockito.any<HttpResponse.BodyHandler<String>>()
            )
        }

        @Test
        fun `should return empty list when API returns empty array`() {
            val realMapper = createRealMapper()

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
            Mockito.`when`(httpResponse.body()).thenReturn("[]")
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val repository = APIEnvNatinfRepository(
                clientFactory = httpClientFactory,
                mapper = realMapper,
                host = host
            )

            val result = repository.findAll()

            assertTrue(result.isEmpty())
        }

        @Test
        fun `should throw BackendInternalException when API returns 500`() {
            val realMapper = createRealMapper()

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(500)
            Mockito.`when`(httpResponse.body()).thenReturn("Internal Server Error")
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val repository = APIEnvNatinfRepository(
                clientFactory = httpClientFactory,
                mapper = realMapper,
                host = host
            )

            val exception = assertThrows(BackendInternalException::class.java) {
                repository.findAll()
            }
            assertTrue(exception.message.contains("500"))
        }

        @Test
        fun `should throw BackendInternalException when API returns 404`() {
            val realMapper = createRealMapper()

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(404)
            Mockito.`when`(httpResponse.body()).thenReturn("Not Found")
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val repository = APIEnvNatinfRepository(
                clientFactory = httpClientFactory,
                mapper = realMapper,
                host = host
            )

            val exception = assertThrows(BackendInternalException::class.java) {
                repository.findAll()
            }
            assertTrue(exception.message.contains("404"))
        }
    }
}
