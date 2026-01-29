package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnitResource.IEnvControlUnitResourceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnitResourceEnv
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.readValue
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class APIEnvControlUnitResourceRepository(
    clientFactory: HttpClientFactory,
    private val mapper: JsonMapper,
    @param:Value("\${MONITORENV_HOST}") private val host: String,
): IEnvControlUnitResourceRepository {

    private val logger = LoggerFactory.getLogger(APIEnvControlUnitResourceRepository::class.java)
    private val client = clientFactory.create()

    override fun findAll(): List<ControlUnitResourceEnv> {
        val url = "$host/api/v1/control_unit_resources"
        logger.info("Sending GET request for Env control unit resources fetching URL: $url")

        val request = HttpRequest.newBuilder().uri(URI.create(url)).build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        logger.info("APIEnvControlUnitResourceRepository::findAll Response received, Status code: ${response.statusCode()}")

        if (response.statusCode() !in 200..299) {
            throw BackendInternalException(
                message = "APIEnvControlUnitResourceRepository.findAll failed with status ${response.statusCode()}",
                originalException = RuntimeException(response.body())
            )
        }

        return mapper.readValue(response.body())
    }
}
