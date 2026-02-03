package fr.gouv.gmampa.rapportnav.infrastructure.monitorenv.v2

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.config.JacksonConfig
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.APIEnvAdministrationRepository
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
@SpringBootTest(classes = [APIEnvAdministrationRepository::class])
@ContextConfiguration(classes = [JacksonConfig::class])
class APIEnvAdministrationRepositoryTest {

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
    inner class FindById {

        @Test
        fun `should fetch administration by id and return data`() {
            val realMapper = createRealMapper()
            val administrationJson = """
                {
                    "id": 1,
                    "name": "DIRM NAMO",
                    "isArchived": false,
                    "controlUnitIds": [101, 102],
                    "controlUnits": []
                }
            """.trimIndent()

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
            Mockito.`when`(httpResponse.body()).thenReturn(administrationJson)
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val repository = APIEnvAdministrationRepository(
                clientFactory = httpClientFactory,
                mapper = realMapper,
                host = host
            )

            val result = repository.findById(administrationId = 1)

            assertNotNull(result)
            assertEquals(1, result?.id)
            assertEquals("DIRM NAMO", result?.name)
            assertEquals(false, result?.isArchived)
        }

        @Test
        fun `should call correct URL with administration ID`() {
            val realMapper = createRealMapper()
            val administrationJson = """{"id": 1, "name": "Test", "isArchived": false, "controlUnitIds": [], "controlUnits": []}"""

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
            Mockito.`when`(httpResponse.body()).thenReturn(administrationJson)
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val repository = APIEnvAdministrationRepository(
                clientFactory = httpClientFactory,
                mapper = realMapper,
                host = host
            )

            repository.findById(administrationId = 42)

            verify(httpClient).send(
                argThat { request ->
                    request.uri() == URI.create("$host/api/v1/administrations/42")
                },
                Mockito.any<HttpResponse.BodyHandler<String>>()
            )
        }

        @Test
        fun `should return null when API returns 404`() {
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

            val repository = APIEnvAdministrationRepository(
                clientFactory = httpClientFactory,
                mapper = realMapper,
                host = host
            )

            val result = repository.findById(administrationId = 999)

            assertNull(result)
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

            val repository = APIEnvAdministrationRepository(
                clientFactory = httpClientFactory,
                mapper = realMapper,
                host = host
            )

            val exception = assertThrows(BackendInternalException::class.java) {
                repository.findById(administrationId = 1)
            }
            assertTrue(exception.message.contains("500"))
        }
    }

    @Nested
    inner class FindAll {

        @Test
        fun `should fetch all administrations and return list`() {
            val realMapper = createRealMapper()
            val administrationsJson = """
                [
                    {"id": 1, "name": "DIRM NAMO", "isArchived": false, "controlUnitIds": [], "controlUnits": []},
                    {"id": 2, "name": "DIRM SA", "isArchived": false, "controlUnitIds": [], "controlUnits": []}
                ]
            """.trimIndent()

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
            Mockito.`when`(httpResponse.body()).thenReturn(administrationsJson)
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val repository = APIEnvAdministrationRepository(
                clientFactory = httpClientFactory,
                mapper = realMapper,
                host = host
            )

            val result = repository.findAll()

            assertEquals(2, result.size)
            assertEquals("DIRM NAMO", result[0].name)
            assertEquals("DIRM SA", result[1].name)
        }

        @Test
        fun `should call correct URL for findAll`() {
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

            val repository = APIEnvAdministrationRepository(
                clientFactory = httpClientFactory,
                mapper = realMapper,
                host = host
            )

            repository.findAll()

            verify(httpClient).send(
                argThat { request ->
                    request.uri() == URI.create("$host/api/v1/administrations")
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

            val repository = APIEnvAdministrationRepository(
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

            val repository = APIEnvAdministrationRepository(
                clientFactory = httpClientFactory,
                mapper = realMapper,
                host = host
            )

            val exception = assertThrows(BackendInternalException::class.java) {
                repository.findAll()
            }
            assertTrue(exception.message.contains("500"))
        }
    }
}
