package fr.gouv.gmampa.rapportnav.infrastructure.monitorenv

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.config.ZonedDateTimeTypeAdapter
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.APIEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.outputs.MissionDataOutput
import org.assertj.core.api.Assertions.assertThat
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
@SpringBootTest(classes = [APIEnvMissionRepository::class])
class APIEnvMissionRepositoryTest {

    val host = "https://monitorenv.din.developpement-durable.gouv.fr";

    val mission = MissionDataOutput(
        id = 761,
        missionTypes = listOf(MissionTypeEnum.SEA),
        controlUnits = listOf(),
        startDateTimeUtc = ZonedDateTime.parse("2022-03-15T04:50:09Z"),
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
    fun `execute should update mission env with patch and observationByUnit`() {

        Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient);
        Mockito.`when`(httpResponse.body()).thenReturn(getMissionString());
        Mockito.`when`(
            httpClient.send(
                Mockito.any(HttpRequest::class.java),
                Mockito.any<HttpResponse.BodyHandler<String>>()
            )
        )
            .thenReturn(httpResponse);
        val envRepo = APIEnvMissionRepository(mapper = objectMapper, clientFactory = httpClientFactory)
        envRepo.updateMission(missionId = 761, MissionEnvEntity(observationsByUnit = "MyObservations"))
        verify(httpClient).send(
            argThat { request -> request.uri().equals(URI.create("$host/api/v1/missions/761")) },
            Mockito.any<HttpResponse.BodyHandler<String>>()
        )
    }

    //TODO: write test on verofying the response value.

    private fun getMissionString(): String {
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeTypeAdapter())
            .create();
        return gson.toJson(mission);
    }
}

