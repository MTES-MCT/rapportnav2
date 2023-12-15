package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.ExtendedEnvMissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.EnvMission
import org.n52.jackson.datatype.jts.JtsModule
import org.slf4j.LoggerFactory
import org.springframework.web.util.UriUtils
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.ZonedDateTime


@UseCase
class GetEnvMissions(private val mapper: ObjectMapper) {
    private val logger = LoggerFactory.getLogger(GetEnvMissions::class.java)

    fun execute(
        startedAfterDateTime: ZonedDateTime? = null,
        startedBeforeDateTime: ZonedDateTime? = null,
        pageNumber: Int? = null,
        pageSize: Int? = null,
        controlUnits: List<Int>? = null
    ): List<MissionEntity> {

//        try {
        val uriBuilder = StringBuilder("https://monitorenv.din.developpement-durable.gouv.fr/api/v1/missions")

        // Append query parameters
        uriBuilder.append("?")

        // Append optional parameters
        startedAfterDateTime?.let {
            uriBuilder.append(
                "startedAfterDateTime=${
                    UriUtils.encodeQueryParam(
                        it.toString(),
                        "UTF-8"
                    )
                }&"
            )
        }
        startedBeforeDateTime?.let {
            uriBuilder.append(
                "startedBeforeDateTime=${
                    UriUtils.encodeQueryParam(
                        it.toString(),
                        "UTF-8"
                    )
                }&"
            )
        }
        pageNumber?.let { uriBuilder.append("pageNumber=${UriUtils.encodeQueryParam(it.toString(), "UTF-8")}&") }
        pageSize?.let { uriBuilder.append("pageSize=${UriUtils.encodeQueryParam(it.toString(), "UTF-8")}&") }

        // Append controlUnits if present
        controlUnits?.let {
            for (controlUnit in it) {
                uriBuilder.append("controlUnits=${UriUtils.encodeQueryParam(controlUnit.toString(), "UTF-8")}&")
            }
        }

        // Remove trailing "&" if present
        if (uriBuilder.endsWith("&")) {
            uriBuilder.deleteCharAt(uriBuilder.length - 1)
        }

        // Build the URI
        val uri = URI.create(uriBuilder.toString())

        val client: HttpClient = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(uri)
            .build();

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        mapper.registerModule(JtsModule())

        val envMissions: List<EnvMission> =
            mapper.readValue(response.body(), object : TypeReference<List<EnvMission>>() {})

        logger.info("Found ${envMissions.size} missions ")

        val missions = envMissions.map { MissionEntity(envMission = ExtendedEnvMissionEntity.fromEnvMission(it)) }

        return missions
//        } catch (e: Exception) {
//            logger.error("GetEnvMissions failed loading Missions", e)
//            Sentry.captureMessage("GetEnvMissions failed loading Missions")
//            Sentry.captureException(e)
//
//            val mission1 = EnvMission(
//                id = 10,
//                missionTypes = listOf(MissionTypeEnum.SEA),
//                facade = "Outre-Mer",
//                observationsCacem = null,
//                startDateTimeUtc = ZonedDateTime.parse("2022-03-15T04:50:09Z"),
////            endDateTimeUtc = ZonedDateTime.parse("2022-03-23T20:29:03Z"),
//                isClosed = false,
//                isDeleted = false,
//                missionSource = MissionSourceEnum.RAPPORTNAV,
//                hasMissionOrder = false,
//                isUnderJdp = false,
//                isGeometryComputedFromControls = false,
//            )
//            val mission2 = EnvMission(
//                id = 11,
//                missionTypes = listOf(MissionTypeEnum.SEA),
//                facade = "Outre-Mer",
//                observationsCacem = null,
//                startDateTimeUtc = ZonedDateTime.parse("2022-02-15T04:50:09Z"),
//                endDateTimeUtc = ZonedDateTime.parse("2022-02-23T20:29:03Z"),
//                isClosed = true,
//                isDeleted = false,
//                missionSource = MissionSourceEnum.MONITORENV,
//                hasMissionOrder = false,
//                isUnderJdp = false,
//                isGeometryComputedFromControls = false
//            )
//            val mission3 = EnvMission(
//                id = 12,
//                missionTypes = listOf(MissionTypeEnum.SEA),
//                facade = "Outre-Mer",
//                observationsCacem = null,
//                startDateTimeUtc = ZonedDateTime.parse("2022-01-15T04:50:09Z"),
//                endDateTimeUtc = ZonedDateTime.parse("2022-01-23T20:29:03Z"),
//                isClosed = true,
//                isDeleted = false,
//                missionSource = MissionSourceEnum.MONITORFISH,
//                hasMissionOrder = false,
//                isUnderJdp = false,
//                isGeometryComputedFromControls = false
//            )
//
//            val envMissions = listOf(mission1, mission2, mission3)
//
//            val missions = envMissions.map { MissionEntity(envMission = ExtendedEnvMissionEntity.fromEnvMission(it)) }
//
//            logger.info("Found ${missions.size} missions ")
//
//            return missions
//        }


    }
}
