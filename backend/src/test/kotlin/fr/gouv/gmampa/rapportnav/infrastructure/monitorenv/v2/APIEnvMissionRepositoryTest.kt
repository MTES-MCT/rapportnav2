package fr.gouv.gmampa.rapportnav.infrastructure.v2

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.MissionDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.APIEnvMissionRepositoryV2
import fr.gouv.dgampa.rapportnav.infrastructure.utils.GsonSerializer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.argThat
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.ZonedDateTime


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [APIEnvMissionRepositoryV2::class])
class APIEnvMissionRepositoryTest {

    val host = "https://monitorenv.din.developpement-durable.gouv.fr";

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

    @MockBean
    private lateinit var objectMapper: ObjectMapper;

    @Mock
    private var httpResponse = mock<HttpResponse<String>>()


    @MockBean
    private lateinit var httpClientFactory: HttpClientFactory;

    @Mock
    private var httpClient: HttpClient = mock(HttpClient::class.java);


    @Test
    fun `execute should create mission env`() {

        Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient);
        Mockito.`when`(httpResponse.body()).thenReturn(getMissionString());
        Mockito.`when`(
            httpClient.send(
                Mockito.any(HttpRequest::class.java),
                Mockito.any<HttpResponse.BodyHandler<String>>()
            )
        )
            .thenReturn(httpResponse);
        val envRepo = APIEnvMissionRepositoryV2(mapper = objectMapper, clientFactory = httpClientFactory)
        val mission = MissionEnv(
            missionTypes = listOf(MissionTypeEnum.SEA),
            controlUnits = listOf(),
            startDateTimeUtc = ZonedDateTime.parse("2024-04-17T07:00:00Z"),
            endDateTimeUtc = ZonedDateTime.parse("2024-04-17T09:00:00Z"),
            missionSource = MissionSourceEnum.MONITORENV,
            isUnderJdp = false,
            isGeometryComputedFromControls = false,
            hasMissionOrder = false
        )
        envRepo.createMission(mission)
        verify(httpClient).send(
            argThat { request ->
                request.uri() == URI.create("$host/api/v1/missions") &&
                    request.method() == "POST" &&
                    request.headers().firstValue("Content-Type").orElse("") == "application/json"
            },
            Mockito.any<HttpResponse.BodyHandler<String>>()
        )

    }


    private fun getMissionString(): String {
        val gson: Gson = GsonSerializer().create()
        return gson.toJson(mission);
    }
}

