package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.inputs.CreateOrUpdateMissionDataInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs.MissionDataOutput
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

    private val host = System.getenv("MONITORENV_HOST")

    override fun createMission(input: CreateOrUpdateMissionDataInput): MissionEntity? {
        val url = "$host/api/v1/missions";
        logger.info("Sending POST request for Env mission creation URL: $url")
        return try {

            val request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .method("POST", HttpRequest.BodyPublishers.ofString(gson.toJson(input)))
                .header("Content-Type", "application/json")
                .build();

            val response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.debug("Response received, Status code: ${response.statusCode()}");

            val body = response.body()
            logger.debug(body)

            mapper.registerModule(JtsModule())
            val output: MissionDataOutput? = mapper.readValue(body);
            output?.toMissionEntity()
        } catch (e: Exception) {
            logger.error("Failed to POST request for Env action creation. URL: $url", e);
            null;
        }
    }
}
