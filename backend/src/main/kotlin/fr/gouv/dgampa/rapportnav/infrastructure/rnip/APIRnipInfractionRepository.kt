package fr.gouv.dgampa.rapportnav.infrastructure.rnip

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.rnip.IRnipInfractionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.rnip.inputs.RnipInfractionInput
import fr.gouv.dgampa.rapportnav.infrastructure.rnip.outputs.RnipInfractionResultOutput
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.readValue
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class APIRnipInfractionRepository(
    clientFactory: HttpClientFactory,
    private val mapper: JsonMapper,
    @param:Value("\${RNIP_HOST:}") private val host: String,
) : IRnipInfractionRepository {

    private val logger = LoggerFactory.getLogger(APIRnipInfractionRepository::class.java)

    private val client = clientFactory.create()

    override fun send(infractions: List<RnipInfractionInput>): List<RnipInfractionResultOutput> {
        val url = "$host/api/v1/infractions"
        logger.info("Sending POST request for ${infractions.size} RNIP infractions URL: $url")
        try {
            val json = mapper.writeValueAsString(infractions)
            logger.debug("Body request for RNIP infractions as json : $json")

            val request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .method("POST", HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            logger.info("APIRnipInfractionRepository::send Response received, Status code: ${response.statusCode()}")

            val body = response.body()
            logger.debug("Response received, Content: $body")

            if (response.statusCode() !in 200..299) {
                throw BackendInternalException(
                    message = "RNIP API returned status=${response.statusCode()} for POST infractions"
                )
            }

            return mapper.readValue(body)
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to send RNIP infractions",
                originalException = e
            )
        }
    }
}
