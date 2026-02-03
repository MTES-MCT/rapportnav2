package fr.gouv.gmampa.rapportnav.infrastructure.monitorfish

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.config.JacksonConfig
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.Completion
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.APIFishActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.input.PatchActionInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output.MissionActionDataOutput
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
import java.time.Instant
import java.time.ZonedDateTime

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [APIFishActionRepository::class])
@ContextConfiguration(classes = [JacksonConfig::class])
class APIFishActionRepositoryTest {

    private val host = "https://monitorfish.gouv.fr"
    private val apiKey = "test-api-key"

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

    private fun createMissionActionDataOutput(
        id: Int = 123,
        missionId: Int = 456
    ) = MissionActionDataOutput(
        id = id,
        missionId = missionId,
        vesselId = 789,
        vesselName = "Test Vessel",
        flagState = CountryCode.FR,
        actionType = MissionActionType.SEA_CONTROL,
        actionDatetimeUtc = ZonedDateTime.parse("2024-01-15T10:30:00Z"),
        userTrigram = "JDO",
        completion = Completion.COMPLETED,
        hasSomeGearsSeized = false,
        hasSomeSpeciesSeized = false,
        isDeleted = false,
        isFromPoseidon = true
    )

    @Nested
    inner class FindFishActions {

        @Test
        fun `should fetch fish actions for a mission and return domain entities`() {
            val realMapper = createRealMapper()
            val missionAction = createMissionActionDataOutput()
            val json = realMapper.writeValueAsString(listOf(missionAction))

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
            Mockito.`when`(httpResponse.body()).thenReturn(json)
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val repository = APIFishActionRepository(
                mapper = realMapper,
                clientFactory = httpClientFactory,
                host = host,
                monitorFishApiKey = apiKey
            )

            val result = repository.findFishActions(missionId = 456)

            assertEquals(1, result.size)
            assertEquals(123, result[0].id)
            assertEquals(456, result[0].missionId)
            assertEquals(MissionActionType.SEA_CONTROL, result[0].actionType)
            assertEquals("JDO", result[0].userTrigram)
        }

        @Test
        fun `should call correct URL with mission ID`() {
            val realMapper = createRealMapper()
            val missionAction = createMissionActionDataOutput()
            val json = realMapper.writeValueAsString(listOf(missionAction))

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
            Mockito.`when`(httpResponse.body()).thenReturn(json)
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val repository = APIFishActionRepository(
                mapper = realMapper,
                clientFactory = httpClientFactory,
                host = host,
                monitorFishApiKey = apiKey
            )

            repository.findFishActions(missionId = 456)

            verify(httpClient).send(
                argThat { request ->
                    request.uri() == URI.create("$host/api/v1/mission_actions?missionId=456")
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

            val repository = APIFishActionRepository(
                mapper = realMapper,
                clientFactory = httpClientFactory,
                host = host,
                monitorFishApiKey = apiKey
            )

            val result = repository.findFishActions(missionId = 456)

            assertTrue(result.isEmpty())
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

            val repository = APIFishActionRepository(
                mapper = realMapper,
                clientFactory = httpClientFactory,
                host = host,
                monitorFishApiKey = apiKey
            )

            val exception = assertThrows(BackendInternalException::class.java) {
                repository.findFishActions(missionId = 456)
            }
            assertTrue(exception.message.contains("404"))
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

            val repository = APIFishActionRepository(
                mapper = realMapper,
                clientFactory = httpClientFactory,
                host = host,
                monitorFishApiKey = apiKey
            )

            val exception = assertThrows(BackendInternalException::class.java) {
                repository.findFishActions(missionId = 456)
            }
            assertTrue(exception.message.contains("500"))
        }
    }

    @Nested
    inner class PatchAction {

        @Test
        fun `should send PATCH request and return updated action`() {
            val realMapper = createRealMapper()
            val missionAction = createMissionActionDataOutput(id = 123)
            val json = realMapper.writeValueAsString(missionAction)

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
            Mockito.`when`(httpResponse.body()).thenReturn(json)
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val repository = APIFishActionRepository(
                mapper = realMapper,
                clientFactory = httpClientFactory,
                host = host,
                monitorFishApiKey = apiKey
            )

            val patchInput = PatchActionInput(
                observationsByUnit = "Updated observations",
                actionDatetimeUtc = Instant.parse("2024-01-15T10:30:00Z")
            )

            val result = repository.patchAction(actionId = "123", action = patchInput)

            assertNotNull(result)
            assertEquals(123, result?.id)
        }

        @Test
        fun `should call correct URL with action ID`() {
            val realMapper = createRealMapper()
            val missionAction = createMissionActionDataOutput(id = 123)
            val json = realMapper.writeValueAsString(missionAction)

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
            Mockito.`when`(httpResponse.body()).thenReturn(json)
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val repository = APIFishActionRepository(
                mapper = realMapper,
                clientFactory = httpClientFactory,
                host = host,
                monitorFishApiKey = apiKey
            )

            repository.patchAction(
                actionId = "action-123",
                action = PatchActionInput(observationsByUnit = "Test")
            )

            verify(httpClient).send(
                argThat { request ->
                    request.uri() == URI.create("$host/api/v1/mission_actions/action-123")
                },
                Mockito.any<HttpResponse.BodyHandler<String>>()
            )
        }

        @Test
        fun `should throw BackendInternalException when PATCH returns 400`() {
            val realMapper = createRealMapper()

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(400)
            Mockito.`when`(httpResponse.body()).thenReturn("Bad Request")
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val repository = APIFishActionRepository(
                mapper = realMapper,
                clientFactory = httpClientFactory,
                host = host,
                monitorFishApiKey = apiKey
            )

            val exception = assertThrows(BackendInternalException::class.java) {
                repository.patchAction(
                    actionId = "123",
                    action = PatchActionInput(observationsByUnit = "Test")
                )
            }
            assertTrue(exception.message.contains("400"))
        }

        @Test
        fun `should throw BackendInternalException when PATCH returns 500`() {
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

            val repository = APIFishActionRepository(
                mapper = realMapper,
                clientFactory = httpClientFactory,
                host = host,
                monitorFishApiKey = apiKey
            )

            val exception = assertThrows(BackendInternalException::class.java) {
                repository.patchAction(
                    actionId = "123",
                    action = PatchActionInput(observationsByUnit = "Test")
                )
            }
            assertTrue(exception.message.contains("500"))
        }
    }

    @Nested
    inner class GetVessels {

        @Test
        fun `should fetch vessels and return list`() {
            val realMapper = createRealMapper()
            val vesselsJson = """
                [
                    {
                        "vesselId": 1,
                        "flagState": "FR",
                        "vesselName": "Vessel 1",
                        "internalReferenceNumber": "FRA000123"
                    },
                    {
                        "vesselId": 2,
                        "flagState": "ES",
                        "vesselName": "Vessel 2",
                        "internalReferenceNumber": "ESP000456"
                    }
                ]
            """.trimIndent()

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
            Mockito.`when`(httpResponse.body()).thenReturn(vesselsJson)
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val repository = APIFishActionRepository(
                mapper = realMapper,
                clientFactory = httpClientFactory,
                host = host,
                monitorFishApiKey = apiKey
            )

            val result = repository.getVessels()

            assertEquals(2, result.size)
            assertEquals(1, result[0].vesselId)
            assertEquals("Vessel 1", result[0].vesselName)
            assertEquals(CountryCode.FR, result[0].flagState)
        }

        @Test
        fun `should call correct URL for vessels`() {
            val realMapper = createRealMapper()
            val vesselsJson = "[]"

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
            Mockito.`when`(httpResponse.body()).thenReturn(vesselsJson)
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val repository = APIFishActionRepository(
                mapper = realMapper,
                clientFactory = httpClientFactory,
                host = host,
                monitorFishApiKey = apiKey
            )

            repository.getVessels()

            verify(httpClient).send(
                argThat { request ->
                    request.uri() == URI.create("$host/api/v1/vessels")
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

            val repository = APIFishActionRepository(
                mapper = realMapper,
                clientFactory = httpClientFactory,
                host = host,
                monitorFishApiKey = apiKey
            )

            val result = repository.getVessels()

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

            val repository = APIFishActionRepository(
                mapper = realMapper,
                clientFactory = httpClientFactory,
                host = host,
                monitorFishApiKey = apiKey
            )

            val exception = assertThrows(BackendInternalException::class.java) {
                repository.getVessels()
            }
            assertTrue(exception.message.contains("500"))
        }
    }
}