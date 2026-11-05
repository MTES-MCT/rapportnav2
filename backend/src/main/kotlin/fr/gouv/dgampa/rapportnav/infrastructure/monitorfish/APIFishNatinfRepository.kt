package fr.gouv.dgampa.rapportnav.infrastructure.monitorfish

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.INatinfRepository
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output.InfractionDataOutput
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.readValue
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class APIFishNatinfRepository(
    clientFactory: HttpClientFactory,
    private val mapper: JsonMapper,
    @param:Value("\${MONITORFISH_HOST}") private val host: String,
    @param:Value("\${MONITORFISH_API_KEY}") private val monitorFishApiKey: String,
) : INatinfRepository {

    private val logger = LoggerFactory.getLogger(APIFishNatinfRepository::class.java)
    private val client = clientFactory.create()

    @Cacheable(value = ["natinfs"])
    override fun findAll(): List<NatinfEntity> {
        val url = "$host/api/v1/infractions"
        logger.info("Sending GET request for Natinfs fetching URL: $url")

        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("x-api-key", monitorFishApiKey)
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        logger.info("APIFishNatinfRepository::findAll Response received, Status code: ${response.statusCode()}")

        if (response.statusCode() !in 200..299) {
            throw BackendInternalException(
                message = "APIFishNatinfRepository.findAll failed with status ${response.statusCode()}",
                originalException = RuntimeException(response.body())
            )
        }

        val output: List<InfractionDataOutput> = mapper.readValue(response.body())
        return output.mapNotNull { it.toNatinfEntity() }
    }
}
