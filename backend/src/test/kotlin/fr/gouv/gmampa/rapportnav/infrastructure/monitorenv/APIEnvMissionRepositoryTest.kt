package fr.gouv.gmampa.rapportnav.infrastructure.monitorenv

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.config.JacksonConfig
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.PatchedEnvActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.APIEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchActionInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchMissionInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.MissionDataOutput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import org.mockito.ArgumentMatchers.argThat
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.cache.autoconfigure.CacheAutoConfiguration
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
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [APIEnvMissionRepository::class])
@ContextConfiguration(classes = [JacksonConfig::class])
class APIEnvMissionRepositoryTest {

    val host = "https://url.developpement-durable.gouv.fr"

    @MockitoBean
    private lateinit var mapper: JsonMapper

    @MockitoBean
    private var httpResponse = mock<HttpResponse<String>>()


    @MockitoBean
    private lateinit var httpClientFactory: HttpClientFactory

    @MockitoBean
    private var httpClient: HttpClient = mock(HttpClient::class.java)


    @Nested
    inner class PatchMission {
        @Test
        fun `execute should update mission env with patch and observationsByUnit`() {
            val mission = MissionDataOutput(
                id = 761,
                missionTypes = listOf(MissionTypeEnum.SEA),
                controlUnits = listOf(),
                startDateTimeUtc = ZonedDateTime.parse("2022-03-15T04:50:09Z"),
                endDateTimeUtc = ZonedDateTime.parse("2022-03-27T04:50:09Z"),
                missionSource = MissionSourceEnum.MONITORENV,
                hasMissionOrder = false,
                isUnderJdp = false,
                isGeometryComputedFromControls = false,
                observationsByUnit = "my observationsByUnit"
            )
            val json = mapper.writeValueAsString(mission)
            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.body()).thenReturn(json)
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            )
                .thenReturn(httpResponse)
            val envRepo = APIEnvMissionRepository(mapper = mapper, clientFactory = httpClientFactory, host = host)
            envRepo.patchMission(
                missionId = 761,
                PatchMissionInput(
                    observationsByUnit = "MyObservations",
                    startDateTimeUtc = Instant.parse("2022-03-15T04:50:09Z"),
                    endDateTimeUtc = Instant.parse("2022-03-27T04:50:09Z")
                )
            )
            verify(httpClient).send(
                argThat { request -> request.uri().equals(URI.create("$host/api/v2/missions/761")) },
                Mockito.any<HttpResponse.BodyHandler<String>>()
            )
        }
    }

    @Nested
    inner class PatchAction {

        private val action = PatchedEnvActionEntity(
            id = UUID.randomUUID(),
            actionStartDateTimeUtc = Instant.parse("2022-03-15T02:50:09Z"),
            actionEndDateTimeUtc = Instant.parse("2022-03-15T04:50:09Z"),
            observationsByUnit = "dummy"
        )

        @Test
        fun `execute should update action env with patch and observationsByUnit`() {
            val json = mapper.writeValueAsString(action)

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
                    // Mock the response
            Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
            Mockito.`when`(httpResponse.body()).thenReturn(json)

            // Mock the client to return the response
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            // Mock the factory to return the mocked client
            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)

            // Create repository with mocked factory
            val envRepo = APIEnvMissionRepository(
                mapper = mapper,
                clientFactory = httpClientFactory,
                host = host
            )

            // Execute the method
            envRepo.patchAction(
                actionId = action.id.toString(),
                PatchActionInput(
                    observationsByUnit = "MyObservations",
                    actionStartDateTimeUtc = Instant.parse("2022-03-15T04:50:09Z"),
                    actionEndDateTimeUtc = Instant.parse("2022-03-27T04:50:09Z")
                )
            )

            // Verify the interaction
            verify(httpClient).send(
                argThat { request ->
                    request.uri() == URI.create("$host/api/v1/actions/${action.id}")
                },
                Mockito.any<HttpResponse.BodyHandler<String>>()
            )
        }
    }

    @Nested
    inner class GetMissions {

        private val mission = MissionDataOutput(
            id = 1,
            startDateTimeUtc = ZonedDateTime.now(),
            endDateTimeUtc = ZonedDateTime.now(),
            missionTypes = listOf(MissionTypeEnum.AIR),
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            hasMissionOrder = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false
        )


        @Test
        fun `execute call MonitorEnv API missions`() {
            val realMapper = JsonMapper.builder()
                .addModule(KotlinModule.Builder().build())
                .build()
            val json = realMapper.writeValueAsString(listOf(mission))
            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
            Mockito.`when`(httpResponse.body()).thenReturn(json)
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            )
                .thenReturn(httpResponse)
            val envRepo = APIEnvMissionRepository(mapper = realMapper, clientFactory = httpClientFactory, host = host)
            envRepo.findAllMissions(
                startedAfterDateTime = Instant.parse("2025-01-31T23:00:00.000Z"),
                startedBeforeDateTime = Instant.parse("2025-02-28T22:59:59.999Z"),
                missionSources = listOf(MissionSourceEnum.MONITORENV, MissionSourceEnum.RAPPORT_NAV, MissionSourceEnum.MONITORFISH).map { it.toString() }
            )
            verify(httpClient).send(
                argThat { request ->
                    request.uri().equals(URI.create("$host/api/v1/missions?startedAfterDateTime=2025-01-31T23:00:00Z&startedBeforeDateTime=2025-02-28T22:59:59.999Z&missionSource=MONITORENV,RAPPORT_NAV,MONITORFISH"))
                },
                Mockito.any<HttpResponse.BodyHandler<String>>()
            )
        }

        @Test
        fun `should throw BackendInternalException when API returns 404`() {
            val realMapper = JsonMapper.builder()
                .addModule(KotlinModule.Builder().build())
                .build()
            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(404)
            Mockito.`when`(httpResponse.body()).thenReturn("Not Found")
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val envRepo = APIEnvMissionRepository(mapper = realMapper, clientFactory = httpClientFactory, host = host)

            val exception = assertThrows(BackendInternalException::class.java) {
                envRepo.findAllMissions(
                    startedAfterDateTime = Instant.parse("2025-01-31T23:00:00.000Z"),
                    startedBeforeDateTime = Instant.parse("2025-02-28T22:59:59.999Z")
                )
            }
            assertTrue(exception.message.contains("404"))
        }

        @Test
        fun `should throw BackendInternalException when API returns 500`() {
            val realMapper = JsonMapper.builder()
                .addModule(KotlinModule.Builder().build())
                .build()
            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(500)
            Mockito.`when`(httpResponse.body()).thenReturn("Internal Server Error")
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val envRepo = APIEnvMissionRepository(mapper = realMapper, clientFactory = httpClientFactory, host = host)

            val exception = assertThrows(BackendInternalException::class.java) {
                envRepo.findAllMissions(
                    startedAfterDateTime = Instant.parse("2025-01-31T23:00:00.000Z"),
                    startedBeforeDateTime = Instant.parse("2025-02-28T22:59:59.999Z")
                )
            }
            assertTrue(exception.message.contains("500"))
        }

        @Test
        fun `should return empty list when API returns empty array`() {
            val realMapper = JsonMapper.builder()
                .addModule(KotlinModule.Builder().build())
                .build()
            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
            Mockito.`when`(httpResponse.body()).thenReturn("[]")
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val envRepo = APIEnvMissionRepository(mapper = realMapper, clientFactory = httpClientFactory, host = host)

            val result = envRepo.findAllMissions(
                startedAfterDateTime = Instant.parse("2025-01-31T23:00:00.000Z"),
                startedBeforeDateTime = Instant.parse("2025-02-28T22:59:59.999Z")
            )

            assertTrue(result!!.isEmpty())
        }

        @Test
        fun `should return mission entities when API returns valid data`() {
            val realMapper = JsonMapper.builder()
                .addModule(KotlinModule.Builder().build())
                .build()
            val json = realMapper.writeValueAsString(listOf(mission))
            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
            Mockito.`when`(httpResponse.body()).thenReturn(json)
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val envRepo = APIEnvMissionRepository(mapper = realMapper, clientFactory = httpClientFactory, host = host)

            val result = envRepo.findAllMissions(
                startedAfterDateTime = Instant.parse("2025-01-31T23:00:00.000Z"),
                startedBeforeDateTime = Instant.parse("2025-02-28T22:59:59.999Z")
            )

            assertEquals(1, result!!.size)
            assertEquals(1, result[0].id)
        }

    }
}

