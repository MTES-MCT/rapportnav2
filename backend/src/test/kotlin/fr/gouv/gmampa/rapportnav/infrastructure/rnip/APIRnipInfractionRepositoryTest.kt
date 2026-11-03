package fr.gouv.gmampa.rapportnav.infrastructure.rnip

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.infrastructure.rnip.APIRnipInfractionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.rnip.inputs.RnipInfractionInput
import fr.gouv.dgampa.rapportnav.infrastructure.rnip.inputs.RnipInspectorInput
import fr.gouv.dgampa.rapportnav.infrastructure.rnip.outputs.RnipLineStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
import java.time.Instant
import java.util.UUID

class APIRnipInfractionRepositoryTest {

    private val host = "https://rnip.gouv.fr"

    private val httpClientFactory: HttpClientFactory = mock()
    private val httpClient: HttpClient = mock()
    private val httpResponse: HttpResponse<String> = mock()

    private val mapper = JsonMapper.builder()
        .addModule(KotlinModule.Builder().build())
        .build()

    private val id = UUID.fromString("7f3c1a2e-0000-4000-8000-000000000001")

    private val infraction = RnipInfractionInput(
        id = id,
        dateEnvoi = Instant.parse("2026-11-02T10:00:00Z"),
        numeroRapport = "SATI-42",
        dateDeCommission = Instant.parse("2026-10-30T08:30:00Z"),
        module = SatiModuleType.M1,
        natinf = "2593",
        inspecteur = RnipInspectorInput(
            nom = "Durand",
            prenom = "Alice",
            nomDuService = "DML 29",
            codeUnite = "ULAM 29",
            provenance = "RapportNav"
        )
    )

    private fun createRepository(statusCode: Int, body: String): APIRnipInfractionRepository {
        Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
        Mockito.`when`(httpResponse.statusCode()).thenReturn(statusCode)
        Mockito.`when`(httpResponse.body()).thenReturn(body)
        Mockito.`when`(
            httpClient.send(
                Mockito.any(HttpRequest::class.java),
                Mockito.any<HttpResponse.BodyHandler<String>>()
            )
        ).thenReturn(httpResponse)
        return APIRnipInfractionRepository(clientFactory = httpClientFactory, mapper = mapper, host = host)
    }

    @Test
    fun `should send infractions and return a result for each line`() {
        val repository = createRepository(
            statusCode = 200,
            body = """[{ "id": "$id", "success": false, "status": "REJECTED", "message": "Unknown NATINF" }]"""
        )

        val results = repository.send(listOf(infraction))

        assertEquals(1, results.size)
        assertEquals(id, results[0].id)
        assertEquals(false, results[0].success)
        assertEquals(RnipLineStatus.REJECTED, results[0].status)
        assertEquals("Unknown NATINF", results[0].message)
    }

    @Test
    fun `should POST the infractions as JSON to the RNIP host`() {
        val repository = createRepository(statusCode = 200, body = "[]")

        repository.send(listOf(infraction))

        verify(httpClient).send(
            argThat { request ->
                request.uri() == URI.create("$host/api/v1/infractions") &&
                    request.method() == "POST" &&
                    request.headers().firstValue("Content-Type").orElse("") == "application/json" &&
                    request.bodyPublisher().get().contentLength() > 0
            },
            Mockito.any<HttpResponse.BodyHandler<String>>()
        )
    }

    @Test
    fun `should throw BackendInternalException when RNIP returns an error status`() {
        val repository = createRepository(statusCode = 500, body = "Internal Server Error")

        val exception = assertThrows<BackendInternalException> {
            repository.send(listOf(infraction))
        }
        assertTrue(exception.message.contains("500"))
    }
}
