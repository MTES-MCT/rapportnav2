package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.INatinfRepository
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
class APIEnvNatinfRepository(
    clientFactory: HttpClientFactory,
    private val mapper: JsonMapper,
    @param:Value("\${MONITORENV_HOST}") private val host: String,
) : INatinfRepository {

    private val logger = LoggerFactory.getLogger(APIEnvNatinfRepository::class.java)
    private val client = clientFactory.create()

    @Cacheable(value = ["natinfs"])
    override fun findAll(): List<NatinfEntity> {
        val url = "$host/bff/v1/natinfs"
        logger.info("Sending GET request for Natinfs fetching URL: $url")

        val request = HttpRequest.newBuilder().uri(URI.create(url)).build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        logger.info("APIEnvNatinfRepository::findAll Response received, Status code: ${response.statusCode()}")

        if (response.statusCode() !in 200..299) {
            throw BackendInternalException(
                message = "APIEnvNatinfRepository.findAll failed with status ${response.statusCode()}",
                originalException = RuntimeException(response.body())
            )
        }

        return mapper.readValue(response.body())
    }
}
