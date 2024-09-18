package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.PatchedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchActionInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchMissionInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.MissionDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.action.MissionEnvActionDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.controlPlans.ControlPlanDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.utils.GsonSerializer
import org.n52.jackson.datatype.jts.JtsModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.web.util.UriUtils
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Instant

@Repository
class APIEnvMissionRepository(
    private val mapper: ObjectMapper,
    private val clientFactory: HttpClientFactory
) : IEnvMissionRepository {
    private val logger: Logger = LoggerFactory.getLogger(APIEnvMissionRepository::class.java);

    private val gson = GsonSerializer().create()

    private val client = clientFactory.create();

    // TODO set as env var when available
    private val host = "https://monitorenv.din.developpement-durable.gouv.fr"

    override fun findMissionById(missionId: Int): MissionEntity? {
        val url = URI.create("$host/api/v1/missions/$missionId")

        logger.info("Sending GET request for Env mission id=$missionId. URL: $url")
        val request = HttpRequest
            .newBuilder()
            .uri(url)
            .build()

        return try {
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            logger.info("Response received for Env mission id=$missionId. Status code: ${response.statusCode()}. URL: $url")
            logger.debug(
                "Raw response body for Env mission id=$missionId: ${response.body()}",
                response.body().toString()
            )

            val contentType = response.headers().firstValue("Content-Type").orElse("")
            if (!contentType.startsWith("application/json")) {
                logger.error("Unexpected content type: $contentType for Env mission id=$missionId")
                throw Exception("Unexpected content type: $contentType for Env mission id=$missionId")
            }

            when (response.statusCode()) {
                200 -> {
                    try {
                        mapper.registerModule(JtsModule())
                        val missionDataOutput: MissionDataOutput = mapper.readValue(response.body())
                        logger.info("Successfully deserialized Env mission data for id=$missionId")
                        missionDataOutput.toMissionEntity()
                    } catch (e: Exception) {
                        logger.error("Failed to deserialize Env mission data for id=$missionId", e)
                        null
                    }
                }

                404 -> {
                    logger.warn("Env Mission with id=$missionId not found")
                    null
                }

                else -> {
                    logger.error("Unexpected status code ${response.statusCode()} for Env mission id=$missionId")
                    throw Exception("Unexpected status code ${response.statusCode()} for Env mission id=$missionId")
                }
            }
        } catch (e: Exception) {
            logger.error("Error while fetching Env mission with id=$missionId", e)
            throw Exception("Error while fetching Env mission with id=$missionId", e)
        }
    }


    override fun findAllMissions(
        pageNumber: Int?,
        pageSize: Int?,
        startedAfterDateTime: Instant?,
        startedBeforeDateTime: Instant?,
        missionSources: List<String>?,
        missionTypes: List<String>?,
        missionStatuses: List<String>?,
        seaFronts: List<String>?,
        controlUnits: List<Int>?
    ): List<MissionEntity>? {

        val uriBuilder = StringBuilder("$host/api/v1/missions")

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

        val request = HttpRequest.newBuilder()
            .uri(uri)
            .build()

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

            val missions = missionEntityList
//                .map {
//                MissionEntity(
//                    envMission = ExtendedEnvMissionEntity.fromEnvMission(
//                        it
//                    )
//                )
//            }

            return missions
        } else {
            logger.info("Failed to fetch data. Status code: ${response.statusCode()}")
        }

        return null

    }

    override fun findAllControlPlans(): ControlPlansEntity? {
        val url = "$host/api/v1/control_plans"
        logger.info("Starting to fetch ControlsPlans from MonitorEnv at $url")
        return try {
            val request = HttpRequest.newBuilder().uri(
                URI.create(url)
            ).build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            logger.info("Response received for Env ControlPlans. Status code: ${response.statusCode()}. URL: $url")

            val output: ControlPlanDataOutput = gson.fromJson(response.body(), ControlPlanDataOutput::class.java)
            output.toControlPlansEntity()
        } catch (ex: Exception) {
            logger.error("Failed to fetch ControlsPlans from MonitorEnv at $url", ex)
            null
        }
    }

    override fun patchMission(missionId: Int, mission: PatchMissionInput): MissionEntity? {
        val url = "$host/api/v2/missions/$missionId";
        logger.info("Sending PATCH request for Env mission id=$missionId. URL: $url")
        return try {
            val request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(gson.toJson(mission)))
                .header("Content-Type", "application/json")
                .build();

            val response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.debug("Response received, missionId: ${missionId}, Status code: ${response.statusCode()}");

            val body = response.body()
            logger.debug(body)

            mapper.registerModule(JtsModule())
            val missionDataOutput: MissionDataOutput? = mapper.readValue(body);
            missionDataOutput?.toMissionEntity();
        } catch (e: Exception) {
            logger.error("Failed to PATCH request for Env mission id=$missionId. URL: $url", e);
            null;
        }
    }

    override fun patchAction(actionId: String, action: PatchActionInput): PatchedEnvActionEntity? {
        val url = "$host/api/v1/actions/$actionId";
        logger.info("Sending PATCH request for Env mission id=$actionId. URL: $url")
        return try {

            val request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(gson.toJson(action)))
                .header("Content-Type", "application/json")
                .build();

            val response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.debug("Response received, actionId: ${actionId}, Status code: ${response.statusCode()}");

            val body = response.body()
            logger.debug(body)

            mapper.registerModule(JtsModule())
            val output: MissionEnvActionDataOutput? = mapper.readValue(body);
            output?.toPatchableEnvActionEntity();
        } catch (e: Exception) {
            logger.error("Failed to PATCH request for Env action id=$actionId. URL: $url", e);
            null;
        }
    }
}
