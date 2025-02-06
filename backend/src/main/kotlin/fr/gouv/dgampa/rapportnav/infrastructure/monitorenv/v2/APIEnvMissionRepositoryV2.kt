package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.MissionDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.utils.GsonSerializer
import org.n52.jackson.datatype.jts.JtsModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class APIEnvMissionRepositoryV2(
    private val mapper: ObjectMapper,
    clientFactory: HttpClientFactory
): IEnvMissionRepository {

    private val logger: Logger = LoggerFactory.getLogger(APIEnvMissionRepositoryV2::class.java);

    private val gson = GsonSerializer().create()

    private val client = clientFactory.create();

   // private val host = System.getenv("MONITORENV_HOST")

    private val host = "https://monitorenv.din.developpement-durable.gouv.fr"

    override fun createMission(mission: MissionEnv): MissionEnvEntity? {
        val url = "$host/api/v1/missions";
        logger.info("Sending POST request for Env mission creation URL: $url")
        return try {

            logger.info("Body request for Mission env create : ${gson.toJson(mission)}")

            val request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .method("POST", HttpRequest.BodyPublishers.ofString(gson.toJson(mission)))
                .header("Content-Type", "application/json")
                .build();

            val response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Response received, Status code: ${response.statusCode()}");

            val body = response.body()
            logger.info("Response received, Content: $body")

            if (response.statusCode() == 400 || response.statusCode() == 500) {
                throw Exception("Error while creating mission from env, please check the logs")
            }

            mapper.registerModule(JtsModule())
            val missionDataOutput: MissionDataOutput? = mapper.readValue(body);
            missionDataOutput?.toMissionEnvEntity();
        } catch (e: Exception) {
            logger.error("Failed to POST request for Env action creation. URL: $url", e);
            null;
        }
    }
}
