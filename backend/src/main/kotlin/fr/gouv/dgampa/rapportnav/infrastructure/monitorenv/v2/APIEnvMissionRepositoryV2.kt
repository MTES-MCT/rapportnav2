package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.MissionDataOutput
import org.n52.jackson.datatype.jts.JtsModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class APIEnvMissionRepositoryV2(
    clientFactory: HttpClientFactory,
    @Value("\${MONITORENV_HOST}") private val host: String,
    ): IEnvMissionRepository {

    private val logger: Logger = LoggerFactory.getLogger(APIEnvMissionRepositoryV2::class.java);

    private val mapper = ObjectMapper()

    private val client = clientFactory.create();

    override fun createMission(mission: MissionEnv): MissionEnvEntity? {
        val url = "$host/api/v1/missions";
        logger.info("Sending POST request for Env mission creation URL: $url")
        return try {

            mapper.registerModules(JtsModule(), JavaTimeModule())

            val json = mapper.writeValueAsString(mission)

            logger.info("Body request for Mission env create as json : $json}")

            logger.info("Body request for Mission env create as entity : $mission}")

            val request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .method("POST", HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

            val response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Response received, Status code: ${response.statusCode()}");

            val body = response.body()
            logger.info("Response received, Content: $body")

            if (response.statusCode() == 400 || response.statusCode() == 500) {
                throw Exception("Error while creating mission from env, please check the logs")
            }

            val missionDataOutput: MissionDataOutput? = mapper.readValue(body);
            missionDataOutput?.toMissionEnvEntity();
        } catch (e: Exception) {
            logger.error("Failed to POST request for Env action creation. URL: $url", e);
            null;
        }
    }

    override fun update(mission: MissionEnvEntity): MissionEnvEntity? {
        val url = "$host/api/v1/missions/${mission.id}";
        logger.info("Sending POST request for Env mission update URL: $url")
        return try {

            mapper.registerModules(JtsModule(), JavaTimeModule())

            val json = mapper.writeValueAsString(mission)
            logger.info("Mission created")
            logger.info("Body request for Mission env update as json : $json")
            logger.info("Body request for Mission env update as entity : $mission")

            val request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .method("POST", HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

            val response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Response received, Status code: ${response.statusCode()}");

            val body = response.body()
            logger.info("Response received, Content: $body")

            if (response.statusCode() == 400 || response.statusCode() == 500 || response.statusCode() == 405) {
                throw Exception("Error while updating mission from env, please check the logs")
            }

            val missionDataOutput: MissionDataOutput? = mapper.readValue(body);
            missionDataOutput?.toMissionEnvEntity();
        } catch (e: Exception) {
            logger.error("Failed to PUT request for Env action update. URL: $url", e);
            null;
        }
    }
}
