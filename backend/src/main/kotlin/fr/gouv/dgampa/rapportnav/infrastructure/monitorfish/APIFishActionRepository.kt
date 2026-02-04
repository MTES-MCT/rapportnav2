package fr.gouv.dgampa.rapportnav.infrastructure.monitorfish

import fr.gouv.cnsp.monitorfish.infrastructure.api.outputs.VesselIdentityDataOutput
import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import io.sentry.Sentry
import io.sentry.SentryLevel
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.input.PatchActionInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output.MissionActionDataOutput
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.json.JsonMapper
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class APIFishActionRepository(
    private val mapper: JsonMapper,
    private val clientFactory: HttpClientFactory,
    @param:Value("\${MONITORFISH_HOST}") private val host: String,
    @param:Value("\${MONITORFISH_API_KEY}") private var monitorFishApiKey: String,
) : IFishActionRepository {
    private val logger: Logger = LoggerFactory.getLogger(APIFishActionRepository::class.java)

    override fun findFishActions(missionId: Int): List<MissionAction> {
        val url = "$host/api/v1/mission_actions?missionId=$missionId"
        logger.info("Fetching Fish Actions for Mission id=$missionId")

        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("x-api-key", monitorFishApiKey)
            .build()

        val response = clientFactory.create().send(request, HttpResponse.BodyHandlers.ofString())
        logger.info("APIFishActionRepository::findFishActions Response received, Status code: ${response.statusCode()}")

        if (response.statusCode() !in 200..299) {
            throw BackendInternalException(
                message = "APIFishActionRepository.findFishActions failed with status ${response.statusCode()} for missionId=$missionId",
                originalException = RuntimeException(response.body())
            )
        }

        val body = response.body()

        if (body.isNullOrBlank()) {
            logger.error("APIFishActionRepository.findFishActions returned null/blank body for missionId=$missionId")
        }

        val output: List<MissionActionDataOutput> = mapper.readValue(
            body,
            object : TypeReference<List<MissionActionDataOutput>>() {}
        )

        if (output.isEmpty()) {
            val message = "APIFishActionRepository.findFishActions returned 0 actions for missionId=$missionId"
            logger.warn(message)
            Sentry.captureMessage(message, SentryLevel.WARNING)
        }

        return output.map { it.toMissionAction() }
    }

    override fun patchAction(actionId: String, action: PatchActionInput): MissionAction? {
        val url = "$host/api/v1/mission_actions/$actionId"
        logger.info("Sending PATCH request for Fish Action id=$actionId. URL: $url")

        val json = mapper.writeValueAsString(action)
        logger.info("Body request send as json : $json")

        val request = HttpRequest
            .newBuilder()
            .uri(URI.create(url))
            .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
            .header("Content-Type", "application/json")
            .header("x-api-key", monitorFishApiKey)
            .build()

        val response = clientFactory.create().send(request, HttpResponse.BodyHandlers.ofString())
        logger.info("APIFishActionRepository::patchAction Response received, actionId: $actionId, Status code: ${response.statusCode()}")

        if (response.statusCode() !in 200..299) {
            throw BackendInternalException(
                message = "APIFishActionRepository.patchAction failed with status ${response.statusCode()} for actionId=$actionId",
                originalException = RuntimeException(response.body())
            )
        }

        val output: MissionActionDataOutput = mapper.readValue(
            response.body(),
            object : TypeReference<MissionActionDataOutput>() {}
        )
        return output.toMissionAction()
    }

    @Cacheable(value = ["vessels"])
    override fun getVessels(): List<VesselIdentityDataOutput> {
        val url = "$host/api/v1/vessels"
        logger.info("Fetching vessel Referential from URL: $url")

        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("x-api-key", monitorFishApiKey)
            .build()

        val response = clientFactory.create().send(request, HttpResponse.BodyHandlers.ofString())
        logger.info("APIFishActionRepository::getVessels Response received, Status code: ${response.statusCode()}")

        if (response.statusCode() !in 200..299) {
            throw BackendInternalException(
                message = "APIFishActionRepository.getVessels failed with status ${response.statusCode()}",
                originalException = RuntimeException(response.body())
            )
        }

        return mapper.readValue(response.body(), object : TypeReference<List<VesselIdentityDataOutput>>() {})
    }
}
