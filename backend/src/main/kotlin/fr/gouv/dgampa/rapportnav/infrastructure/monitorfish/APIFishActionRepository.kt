package fr.gouv.dgampa.rapportnav.infrastructure.monitorfish

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.input.PatchActionInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output.MissionActionDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.utils.GsonSerializer
import org.n52.jackson.datatype.jts.JtsModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class APIFishActionRepository(
    private val mapper: ObjectMapper,
    private val clientFactory: HttpClientFactory
) : IFishActionRepository {
    private val logger: Logger = LoggerFactory.getLogger(APIFishActionRepository::class.java)

    private val gson = GsonSerializer().create()


    @Value("\${MONITORFISH_API_KEY}")
    private lateinit var monitorFishApiKey: String

    // TODO set as env var when available
    private val host = "https://monitorfish.din.developpement-durable.gouv.fr"

    override fun findFishActions(missionId: Int): List<MissionAction> {
        logger.info("Fetching Fish Actions for Mission id=$missionId")
        val request = HttpRequest.newBuilder()
            .uri(
                URI.create(
                    "$host/api/v1/mission_actions?missionId=$missionId"
                )
            )
            .header("x-api-key", monitorFishApiKey)
            .build();

        val response = clientFactory.create().send(request, HttpResponse.BodyHandlers.ofString())

        return if (response.statusCode() in 200..299) {
            mapper.registerModule(JtsModule())
            mapper.readValue(response.body(), object : TypeReference<List<MissionAction>>() {})
        } else {
            logger.info("Failed to fetch Fish Actions. Status code: ${response.statusCode()}")
            emptyList()
        }
    }

    override fun patchAction(actionId: String, action: PatchActionInput): MissionAction? {
        val url = "$host/api/v1/mission_actions/$actionId";
        logger.info("Sending PATCH request for Fish Action id=$actionId. URL: $url")
        return try {

            val request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(gson.toJson(action)))
                .header("Content-Type", "application/json")
                .header("x-api-key", monitorFishApiKey)
                .build();

            logger.info("Request headers: ${request.headers()}");

            val response = clientFactory.create().send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Response received, actionId: ${actionId}, Status code: ${response.statusCode()}");
            logger.info("Response headers: ${response.headers()}");

            val body = response.body()
            logger.info(body)

            mapper.registerModule(JtsModule())
            val output: MissionActionDataOutput =
                mapper.readValue(body, object : TypeReference<MissionActionDataOutput>() {});
            val missionAction: MissionAction = output.toMissionAction()
            missionAction
        } catch (e: Exception) {
            logger.error("Failed to PATCH request for Fish Action id=$actionId. URL: $url", e);
            null;
        }
    }
}
