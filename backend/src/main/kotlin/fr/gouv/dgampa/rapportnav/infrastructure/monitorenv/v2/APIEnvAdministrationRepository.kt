package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.IEnvAdministrationRepository
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs.FullAdministrationDataOutput
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.readValue
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class APIEnvAdministrationRepository(
    clientFactory: HttpClientFactory,
    private val mapper: JsonMapper,
    @param:Value("\${MONITORENV_HOST}") private val host: String,
): IEnvAdministrationRepository {

    private val logger = LoggerFactory.getLogger(IEnvAdministrationRepository::class.java)

    private val client = clientFactory.create()

    override fun findById(administrationId: Int): FullAdministrationDataOutput? {
        val url = "$host/api/v1/administrations/$administrationId"
        logger.info("Sending GET request for Env administration by id fetching URL: $url")

        val request = HttpRequest.newBuilder().uri(URI.create(url)).build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        logger.info("APIEnvAdministrationRepository::findById Response received, Status code: ${response.statusCode()}")

        if (response.statusCode() == 404) return null
        if (response.statusCode() !in 200..299) {
            throw BackendInternalException(
                message = "APIEnvAdministrationRepository.findById failed with status ${response.statusCode()}",
                originalException = RuntimeException(response.body())
            )
        }

        return mapper.readValue(response.body())
    }

    override fun findAll(): List<FullAdministrationDataOutput> {
        val url = "$host/api/v1/administrations"
        logger.info("Sending GET request for Env administration getAll fetching URL: $url")

        val request = HttpRequest.newBuilder().uri(URI.create(url)).build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        logger.info("APIEnvAdministrationRepository::findAll Response received, Status code: ${response.statusCode()}")

        if (response.statusCode() !in 200..299) {
            throw BackendInternalException(
                message = "APIEnvAdministrationRepository.findAll failed with status ${response.statusCode()}",
                originalException = RuntimeException(response.body())
            )
        }

        return mapper.readValue(response.body())
    }


}
