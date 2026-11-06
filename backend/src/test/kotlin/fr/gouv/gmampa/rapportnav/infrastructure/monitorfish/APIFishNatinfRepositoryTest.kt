package fr.gouv.gmampa.rapportnav.infrastructure.monitorfish

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.APIFishNatinfRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.ArgumentMatchers.argThat
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class APIFishNatinfRepositoryTest {

    private val host = "https://monitorfish.gouv.fr"
    private val apiKey = "test-api-key"
    private val httpClient: HttpClient = mock()

    private fun repositoryReturning(status: Int, body: String): APIFishNatinfRepository {
        val httpResponse: HttpResponse<String> = mock()
        Mockito.`when`(httpResponse.statusCode()).thenReturn(status)
        Mockito.`when`(httpResponse.body()).thenReturn(body)
        Mockito.`when`(
            httpClient.send(
                Mockito.any(HttpRequest::class.java),
                Mockito.any<HttpResponse.BodyHandler<String>>()
            )
        ).thenReturn(httpResponse)

        val httpClientFactory: HttpClientFactory = mock()
        Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)

        return APIFishNatinfRepository(
            clientFactory = httpClientFactory,
            mapper = JsonMapper.builder().addModule(KotlinModule.Builder().build()).build(),
            host = host,
            monitorFishApiKey = apiKey
        )
    }

    @Test
    fun `should fetch all natinfs and map every field`() {
        val json = """
            [
                { "natinfCode": 27718, "regulation": "Fishing Regulation", "infractionCategory": "FISHING", "infraction": "Illegal landing" },
                { "natinfCode": 27719, "regulation": null, "infractionCategory": null, "infraction": "Illegal navigation" }
            ]
        """.trimIndent()

        val result = repositoryReturning(200, json).findAll()

        assertEquals(
            listOf(
                NatinfEntity(
                    natinfCode = 27718,
                    infraction = "Illegal landing",
                    regulation = "Fishing Regulation",
                    infractionCategory = "Pêche"
                ),
                NatinfEntity(natinfCode = 27719, infraction = "Illegal navigation"),
            ),
            result
        )
    }

    @Test
    fun `should call infractions endpoint with api key`() {
        repositoryReturning(200, "[]").findAll()

        verify(httpClient).send(
            argThat { request ->
                request.uri() == URI.create("$host/api/v1/infractions") &&
                    request.headers().firstValue("x-api-key").orElse(null) == apiKey
            },
            Mockito.any<HttpResponse.BodyHandler<String>>()
        )
    }

    @Test
    fun `should skip infractions without natinf code or label`() {
        val json = """
            [
                { "natinfCode": 27718, "infraction": "Illegal landing" },
                { "natinfCode": null, "infraction": "No code" },
                { "natinfCode": 27719, "infraction": null },
                {}
            ]
        """.trimIndent()

        val result = repositoryReturning(200, json).findAll()

        assertEquals(listOf(27718), result.map { it.natinfCode })
    }

    @Test
    fun `should return empty list when API returns empty array`() {
        assertTrue(repositoryReturning(200, "[]").findAll().isEmpty())
    }

    @ParameterizedTest
    @ValueSource(ints = [401, 404, 500, 503])
    fun `should throw BackendInternalException when API returns an error status`(status: Int) {
        val repository = repositoryReturning(status, "error")

        val exception = assertThrows(BackendInternalException::class.java) {
            repository.findAll()
        }
        assertTrue(exception.message.contains("$status"))
    }
}
