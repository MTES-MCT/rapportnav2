package fr.gouv.gmampa.rapportnav.infrastructure.utils

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.config.JacksonConfig
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.infrastructure.utils.APIGetCountryRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [APIGetCountryRepository::class])
@ContextConfiguration(classes = [JacksonConfig::class])
class APIGetCountryRepositoryTest {

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

    private fun setupHttpMock(statusCode: Int, body: String) {
        Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
        Mockito.`when`(httpResponse.statusCode()).thenReturn(statusCode)
        Mockito.`when`(httpResponse.body()).thenReturn(body)
        Mockito.`when`(
            httpClient.send(
                Mockito.any(HttpRequest::class.java),
                Mockito.any<HttpResponse.BodyHandler<String>>()
            )
        ).thenReturn(httpResponse)
    }

    @Nested
    inner class GetCountries {

        @Test
        fun `should return list of countries from API`() {
            val realMapper = createRealMapper()
            setupHttpMock(
                200,
                """
                [
                    {"iso2": "FR", "iso3": "FRA", "name_fr": "France", "flag": "🇫🇷"},
                    {"iso2": "DE", "iso3": "DEU", "name_fr": "Allemagne", "flag": "🇩🇪"}
                ]
                """.trimIndent()
            )

            val repository = APIGetCountryRepository<Any>(
                mapper = realMapper,
                clientFactory = httpClientFactory
            )

            val result = repository.getCountries()

            assertEquals(2, result.size)
            assertEquals("FR", result[0].iso2)
            assertEquals("FRA", result[0].iso3)
            assertEquals("France", result[0].name)
            assertEquals("🇫🇷", result[0].flag)
        }

        @Test
        fun `should return empty list when API returns empty array`() {
            val realMapper = createRealMapper()
            setupHttpMock(200, "[]")

            val repository = APIGetCountryRepository<Any>(
                mapper = realMapper,
                clientFactory = httpClientFactory
            )

            val result = repository.getCountries()

            assertTrue(result.isEmpty())
        }

        @Test
        fun `should map null fields gracefully`() {
            val realMapper = createRealMapper()
            setupHttpMock(
                200,
                """[{"iso2": null, "iso3": null, "name_fr": null, "flag": null}]"""
            )

            val repository = APIGetCountryRepository<Any>(
                mapper = realMapper,
                clientFactory = httpClientFactory
            )

            val result = repository.getCountries()

            assertEquals(1, result.size)
            assertNull(result[0].iso2)
            assertNull(result[0].iso3)
            assertNull(result[0].name)
            assertNull(result[0].flag)
        }

        @Test
        fun `should throw BackendInternalException when API returns 500`() {
            val realMapper = createRealMapper()
            setupHttpMock(500, "Internal Server Error")

            val repository = APIGetCountryRepository<Any>(
                mapper = realMapper,
                clientFactory = httpClientFactory
            )

            val exception = assertThrows(BackendInternalException::class.java) {
                repository.getCountries()
            }
            assertTrue(exception.message.contains("500"))
        }

        @Test
        fun `should throw BackendInternalException when API returns 404`() {
            val realMapper = createRealMapper()
            setupHttpMock(404, "Not Found")

            val repository = APIGetCountryRepository<Any>(
                mapper = realMapper,
                clientFactory = httpClientFactory
            )

            val exception = assertThrows(BackendInternalException::class.java) {
                repository.getCountries()
            }
            assertTrue(exception.message.contains("404"))
        }

        @Test
        fun `should throw BackendInternalException when API returns 401`() {
            val realMapper = createRealMapper()
            setupHttpMock(401, "Unauthorized")

            val repository = APIGetCountryRepository<Any>(
                mapper = realMapper,
                clientFactory = httpClientFactory
            )

            assertThrows(BackendInternalException::class.java) {
                repository.getCountries()
            }
        }
    }
}
