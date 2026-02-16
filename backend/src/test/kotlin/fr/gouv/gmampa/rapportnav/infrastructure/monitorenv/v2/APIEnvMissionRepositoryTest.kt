package fr.gouv.gmampa.rapportnav.infrastructure.monitorenv.v2

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.config.JacksonConfig
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.APIEnvMissionRepositoryV2
import fr.gouv.gmampa.rapportnav.mocks.mission.LegacyControlUnitEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MultiPolygonMock
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchMissionInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import tools.jackson.databind.json.JsonMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Instant


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [APIEnvMissionRepositoryV2::class])
@ContextConfiguration(classes = [JacksonConfig::class])
class APIEnvMissionRepositoryTest {

    val host = "https://url.developpement-durable.gouv.fr"

    @MockitoBean
    private lateinit var objectMapper: JsonMapper

    @MockitoBean
    private var httpResponse = mock<HttpResponse<String>>()

    @MockitoBean
    private lateinit var httpClientFactory: HttpClientFactory

    @MockitoBean
    private var httpClient: HttpClient = mock(HttpClient::class.java)


    @Test
    fun `execute should create mission env`() {

        val resource = LegacyControlUnitResourceEntity(
            id = 1,
            controlUnitId = 1,
            name = "urus"
        )

        `when`(httpClientFactory.create()).thenReturn(httpClient)
        `when`(httpResponse.statusCode()).thenReturn(200)
        `when`(httpResponse.body()).thenReturn(getMissionString())
        `when`(
            httpClient.send(
                any(HttpRequest::class.java),
                any<HttpResponse.BodyHandler<String>>()
            )
        )
            .thenReturn(httpResponse)
        val envRepo = APIEnvMissionRepositoryV2(clientFactory = httpClientFactory, host = host, mapper = objectMapper)
        val mission = MissionEnv(
            missionTypes = listOf(MissionTypeEnum.SEA),
            controlUnits = listOf(LegacyControlUnitEntityMock.create(resources = mutableListOf(resource))),
            startDateTimeUtc = Instant.parse("2024-04-17T07:00:00Z"),
            endDateTimeUtc = Instant.parse("2024-04-17T09:00:00Z"),
            missionSource = MissionSourceEnum.MONITORENV,
            isUnderJdp = false,
            isGeometryComputedFromControls = false,
            hasMissionOrder = false
        )

        envRepo.createMission(mission)

        val requestCaptor = ArgumentCaptor.forClass(HttpRequest::class.java)

        verify(httpClient).send(
            requestCaptor.capture(),
            any<HttpResponse.BodyHandler<String>>()
        )

        val capturedRequest = requestCaptor.value

        assert(capturedRequest.uri() == URI.create("$host/api/v1/missions"))
        assert(capturedRequest.method() == "POST")
        assert(capturedRequest.headers().firstValue("Content-Type").orElse("") == "application/json")
    }

    @Test
    fun `execute should update mission env with MultiPolygon`() {

        `when`(httpClientFactory.create()).thenReturn(httpClient)
        `when`(httpResponse.statusCode()).thenReturn(200)
        `when`(httpResponse.body()).thenReturn(getMissionString())
        `when`(
            httpClient.send(
                any(HttpRequest::class.java),
                any<HttpResponse.BodyHandler<String>>()
            )
        )
            .thenReturn(httpResponse)
        val envRepo = APIEnvMissionRepositoryV2(clientFactory = httpClientFactory, host = host, mapper = objectMapper)
        val mission = MissionEnvEntity(
            id = 1,
            missionTypes = listOf(MissionTypeEnum.SEA),
            controlUnits = listOf(),
            startDateTimeUtc = Instant.parse("2024-04-17T07:00:00Z"),
            endDateTimeUtc = Instant.parse("2024-04-17T09:00:00Z"),
            missionSource = MissionSourceEnum.MONITORENV,
            isUnderJdp = false,
            isGeometryComputedFromControls = false,
            hasMissionOrder = false,
            geom = MultiPolygonMock.create()
        )

        envRepo.update(mission)

        val requestCaptor = ArgumentCaptor.forClass(HttpRequest::class.java)


        verify(httpClient).send(
            requestCaptor.capture(),
            any<HttpResponse.BodyHandler<String>>()
        )

        val capturedRequest = requestCaptor.value

        assert(capturedRequest.uri() == URI.create("$host/api/v1/missions/${mission.id}"))
        assert(capturedRequest.method() == "POST")
        assert(capturedRequest.headers().firstValue("Content-Type").orElse("") == "application/json")
    }


    private fun getMissionString(): String {
        val path = Paths.get("src/test/resources/missions/mission.json")
        return String(Files.readAllBytes(path))
    }

    @Nested
    inner class PatchMission {

        @Test
        fun `should send PATCH request for mission update`() {
            `when`(httpClientFactory.create()).thenReturn(httpClient)
            `when`(httpResponse.statusCode()).thenReturn(200)
            `when`(httpResponse.body()).thenReturn(getMissionString())
            `when`(
                httpClient.send(
                    any(HttpRequest::class.java),
                    any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val envRepo = APIEnvMissionRepositoryV2(clientFactory = httpClientFactory, host = host, mapper = objectMapper)

            val patchInput = PatchMissionInput(
                observationsByUnit = "Updated observations",
                startDateTimeUtc = Instant.parse("2024-01-15T10:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-15T18:00:00Z")
            )

            envRepo.patchMission(missionId = 123, mission = patchInput)

            val requestCaptor = ArgumentCaptor.forClass(HttpRequest::class.java)

            verify(httpClient).send(
                requestCaptor.capture(),
                any<HttpResponse.BodyHandler<String>>()
            )

            val capturedRequest = requestCaptor.value

            assert(capturedRequest.uri() == URI.create("$host/api/v2/missions/123"))
            assert(capturedRequest.method() == "PATCH")
            assert(capturedRequest.headers().firstValue("Content-Type").orElse("") == "application/json")
        }

        @Test
        fun `should throw BackendInternalException when PATCH returns 400`() {
            `when`(httpClientFactory.create()).thenReturn(httpClient)
            `when`(httpResponse.statusCode()).thenReturn(400)
            `when`(httpResponse.body()).thenReturn("Bad Request")
            `when`(
                httpClient.send(
                    any(HttpRequest::class.java),
                    any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val envRepo = APIEnvMissionRepositoryV2(clientFactory = httpClientFactory, host = host, mapper = objectMapper)

            val exception = assertThrows(BackendInternalException::class.java) {
                envRepo.patchMission(
                    missionId = 123,
                    mission = PatchMissionInput(observationsByUnit = "test")
                )
            }

            assertTrue(exception.message.contains("400"))
        }

        @Test
        fun `should throw BackendInternalException when PATCH returns 500`() {
            `when`(httpClientFactory.create()).thenReturn(httpClient)
            `when`(httpResponse.statusCode()).thenReturn(500)
            `when`(httpResponse.body()).thenReturn("Internal Server Error")
            `when`(
                httpClient.send(
                    any(HttpRequest::class.java),
                    any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val envRepo = APIEnvMissionRepositoryV2(clientFactory = httpClientFactory, host = host, mapper = objectMapper)

            val exception = assertThrows(BackendInternalException::class.java) {
                envRepo.patchMission(
                    missionId = 123,
                    mission = PatchMissionInput(observationsByUnit = "test")
                )
            }

            assertTrue(exception.message.contains("500"))
        }
    }

    @Nested
    inner class DeleteMission {

        @Test
        fun `should send DELETE request for mission deletion`() {
            `when`(httpClientFactory.create()).thenReturn(httpClient)
            `when`(httpResponse.statusCode()).thenReturn(200)
            `when`(httpResponse.body()).thenReturn("")
            `when`(
                httpClient.send(
                    any(HttpRequest::class.java),
                    any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val envRepo = APIEnvMissionRepositoryV2(clientFactory = httpClientFactory, host = host, mapper = objectMapper)

            envRepo.deleteMission(missionId = 123, source = MissionSourceEnum.MONITORENV)

            val requestCaptor = ArgumentCaptor.forClass(HttpRequest::class.java)

            verify(httpClient).send(
                requestCaptor.capture(),
                any<HttpResponse.BodyHandler<String>>()
            )

            val capturedRequest = requestCaptor.value

            assert(capturedRequest.uri() == URI.create("$host/api/v2/missions/123?source=MONITORENV"))
            assert(capturedRequest.method() == "DELETE")
        }

        @Test
        fun `should throw BackendInternalException when DELETE returns 404`() {
            `when`(httpClientFactory.create()).thenReturn(httpClient)
            `when`(httpResponse.statusCode()).thenReturn(404)
            `when`(httpResponse.body()).thenReturn("Not Found")
            `when`(
                httpClient.send(
                    any(HttpRequest::class.java),
                    any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val envRepo = APIEnvMissionRepositoryV2(clientFactory = httpClientFactory, host = host, mapper = objectMapper)

            val exception = assertThrows(BackendInternalException::class.java) {
                envRepo.deleteMission(missionId = 999, source = MissionSourceEnum.MONITORENV)
            }

            assertTrue(exception.message.contains("404"))
        }

        @Test
        fun `should throw BackendInternalException when DELETE returns 500`() {
            `when`(httpClientFactory.create()).thenReturn(httpClient)
            `when`(httpResponse.statusCode()).thenReturn(500)
            `when`(httpResponse.body()).thenReturn("Internal Server Error")
            `when`(
                httpClient.send(
                    any(HttpRequest::class.java),
                    any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val envRepo = APIEnvMissionRepositoryV2(clientFactory = httpClientFactory, host = host, mapper = objectMapper)

            val exception = assertThrows(BackendInternalException::class.java) {
                envRepo.deleteMission(missionId = 123, source = MissionSourceEnum.MONITORENV)
            }

            assertTrue(exception.message.contains("500"))
        }

        @Test
        fun `should not throw exception when DELETE returns 204`() {
            `when`(httpClientFactory.create()).thenReturn(httpClient)
            `when`(httpResponse.statusCode()).thenReturn(204)
            `when`(httpResponse.body()).thenReturn("")
            `when`(
                httpClient.send(
                    any(HttpRequest::class.java),
                    any<HttpResponse.BodyHandler<String>>()
                )
            ).thenReturn(httpResponse)

            val envRepo = APIEnvMissionRepositoryV2(clientFactory = httpClientFactory, host = host, mapper = objectMapper)

            assertDoesNotThrow {
                envRepo.deleteMission(missionId = 123, source = MissionSourceEnum.MONITORENV)
            }
        }
    }
}

