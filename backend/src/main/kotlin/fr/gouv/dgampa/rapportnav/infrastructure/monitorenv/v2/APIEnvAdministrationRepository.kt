package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.IEnvAdministrationRepository
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs.FullAdministrationDataOutput
import org.n52.jackson.datatype.jts.JtsModule
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class APIEnvAdministrationRepository(
    clientFactory: HttpClientFactory,
    private val mapper: ObjectMapper,
    @Value("\${monitorenv.host}") private val host: String,
): IEnvAdministrationRepository {

    private val logger = LoggerFactory.getLogger(IEnvAdministrationRepository::class.java)

    private val client = clientFactory.create()

    override fun findById(administrationId: Int): FullAdministrationDataOutput? {
        val url = "$host/api/v1/administrations/$administrationId";
        logger.info("Sending GET request for Env administration by id fetching URL: $url")
        return try {

            val request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .build();

            val response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("APIEnvAdministrationRepository::findById Response received, Status code: ${response.statusCode()}");

            val body = response.body()
            logger.info("APIEnvAdministrationRepository::findById Response received, Content: $body")

            if (response.statusCode() == 400 || response.statusCode() == 500) {
                throw Exception("Error while fetching administration by id from env, please check the logs")
            }

            mapper.registerModule(JtsModule())
            val output: FullAdministrationDataOutput = mapper.readValue(body)
            output
        } catch (e: Exception) {
            logger.error("Failed to GET request for Env administration by id fetching. URL: $url", e);
            null;
        }
    }

    override fun findAll(): List<FullAdministrationDataOutput>? {
        val url = "$host/api/v1/administrations";
        logger.info("Sending GET request for Env administration getAll fetching URL: $url")
        return try {

            val request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .build();

            val response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("APIEnvAdministrationRepository::findAll Response received, Status code: ${response.statusCode()}");

            val body = response.body()
            logger.info("APIEnvAdministrationRepository::findAll Response received, Content: $body")

            if (response.statusCode() == 400 || response.statusCode() == 500) {
                throw Exception("Error while fetching administrations from env, please check the logs")
            }

            mapper.registerModule(JtsModule())
            val output: List<FullAdministrationDataOutput> = mapper.readValue(body)
            output
        } catch (e: Exception) {
            logger.error("Failed to GET request for Env administrations fetching. URL: $url", e);
            null;
        }
    }


}
