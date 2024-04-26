package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fr.gouv.dgampa.rapportnav.domain.entities.mission.ExtendedEnvMissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.MissionDataOutput
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.web.util.UriUtils
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Repository
class APIEnvMissionRepository(
    private val mapper: ObjectMapper
) : IEnvMissionRepository {
    private val logger: Logger = LoggerFactory.getLogger(APIEnvMissionRepository::class.java)
    private val zoneDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.000X")


    override fun findAllMissions(
        pageNumber: Int?,
        pageSize: Int?,
        startedAfterDateTime: ZonedDateTime?,
        startedBeforeDateTime: ZonedDateTime?,
        missionSources: List<String>?,
        missionTypes: List<String>?,
        missionStatuses: List<String>?,
        seaFronts: List<String>?,
        controlUnits: List<Int>?
    ): List<MissionEntity>? {

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

        logger.info("Fetching missions at URL: $uri")

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        // Check if the request was successful (status code 2xx)
        if (response.statusCode() in 200..299) {
            // Parse JSON response into a list of MissionDataOutput
            val missionDataOutputList: List<MissionDataOutput> = mapper.readValue(response.body())

            // Transform each MissionDataOutput to MissionEntity
            val missionEntityList: List<fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity> =
                missionDataOutputList.map { it.toMissionEntity() }

            // Use the transformed data as needed
            missionEntityList.forEach { missionEntity ->
                logger.info("Transformed MissionEntity: $missionEntity")
            }

            val missions = missionEntityList.map {
                MissionEntity(
                    envMission = ExtendedEnvMissionEntity.fromEnvMission(
                        it
                    )
                )
            }

            return missions
        } else {
            logger.info("Failed to fetch data. Status code: ${response.statusCode()}")
        }

        return null

    }

}
