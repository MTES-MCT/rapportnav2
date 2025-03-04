package fr.gouv.gmampa.rapportnav.infrastructure.v2

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.MissionDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.APIEnvMissionRepositoryV2
import fr.gouv.dgampa.rapportnav.infrastructure.utils.GsonSerializer
import fr.gouv.gmampa.rapportnav.mocks.mission.MultiPolygonMock
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Instant
import java.time.ZonedDateTime


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [APIEnvMissionRepositoryV2::class])
class APIEnvMissionRepositoryTest {

    val host = "https://monitorenv.din.developpement-durable.gouv.fr"

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

    @MockitoBean
    private lateinit var objectMapper: ObjectMapper

    @Mock
    private var httpResponse = mock<HttpResponse<String>>()


    @MockitoBean
    private lateinit var httpClientFactory: HttpClientFactory

    @Mock
    private var httpClient: HttpClient = mock(HttpClient::class.java)


    @Test
    fun `execute should create mission env`() {

        `when`(httpClientFactory.create()).thenReturn(httpClient)
        `when`(httpResponse.body()).thenReturn(getMissionString())
        `when`(
            httpClient.send(
                any(HttpRequest::class.java),
                any<HttpResponse.BodyHandler<String>>()
            )
        )
            .thenReturn(httpResponse)
        val envRepo = APIEnvMissionRepositoryV2(clientFactory = httpClientFactory)
        val mission = MissionEnv(
            missionTypes = listOf(MissionTypeEnum.SEA),
            controlUnits = listOf(),
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
        `when`(httpResponse.body()).thenReturn(getMissionString())
        `when`(
            httpClient.send(
                any(HttpRequest::class.java),
                any<HttpResponse.BodyHandler<String>>()
            )
        )
            .thenReturn(httpResponse)
        val envRepo = APIEnvMissionRepositoryV2(clientFactory = httpClientFactory)
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
        val gson: Gson = GsonSerializer().create()
        return gson.toJson(mission)
    }
}

