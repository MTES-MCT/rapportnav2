package fr.gouv.gmampa.rapportnav.infrastructure.monitorenv

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.PatchedEnvActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.APIEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchActionInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchMissionInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.MissionDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.utils.GsonSerializer
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.argThat
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [APIEnvMissionRepository::class])
class APIEnvMissionRepositoryTest {

class APIEnvMissionRepositoryTest {

    val host = "https://url.developpement-durable.gouv.fr"

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


    @Nested
    inner class PatchMission {
        @Test
        fun `execute should update mission env with patch and observationsByUnit`() {

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.body()).thenReturn(getMissionString())
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            )
                .thenReturn(httpResponse)
            val envRepo = APIEnvMissionRepository(mapper = objectMapper, clientFactory = httpClientFactory, host = host)
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

        //TODO: write test on verifying the response value.

        private fun getMissionString(): String {
            val gson: Gson = GsonSerializer().create()
            return gson.toJson(mission)
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

        private fun getActionString(): String {
            val gson: Gson = GsonSerializer().create()
            return gson.toJson(action)
        }

        @Test
        fun `execute should update action env with patch and observationsByUnit`() {

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.body()).thenReturn(getActionString())
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            )
                .thenReturn(httpResponse)
            val envRepo = APIEnvMissionRepository(mapper = objectMapper, clientFactory = httpClientFactory, host=host)
            envRepo.patchAction(
                actionId = action.id.toString(),
                PatchActionInput(
                    observationsByUnit = "MyObservations",
                    actionStartDateTimeUtc = Instant.parse("2022-03-15T04:50:09Z"),
                    actionEndDateTimeUtc = Instant.parse("2022-03-27T04:50:09Z")
                )
            )
            verify(httpClient).send(
                argThat { request ->
                    request.uri().equals(URI.create("$host/api/v1/actions/${action.id}"))
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

        private fun getMissionString(): String {
            val gson: Gson = GsonSerializer().create()
            return gson.toJson(listOf(mission))
        }



        @Test
        fun `execute call MonitorEnv API missions`() {

            Mockito.`when`(httpClientFactory.create()).thenReturn(httpClient)
            Mockito.`when`(httpResponse.body()).thenReturn(getMissionString())
            Mockito.`when`(
                httpClient.send(
                    Mockito.any(HttpRequest::class.java),
                    Mockito.any<HttpResponse.BodyHandler<String>>()
                )
            )
                .thenReturn(httpResponse)
            val envRepo = APIEnvMissionRepository(mapper = objectMapper, clientFactory = httpClientFactory)
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

    }
}

