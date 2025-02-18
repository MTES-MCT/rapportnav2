package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnitResource.IEnvControlUnitResourceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnitResourceEnv
import org.n52.jackson.datatype.jts.JtsModule
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class APIEnvControlUnitResourceRepository(
    clientFactory: HttpClientFactory,
    private val mapper: ObjectMapper,
): IEnvControlUnitResourceRepository {

    private val logger = LoggerFactory.getLogger(APIEnvControlUnitResourceRepository::class.java)
    private val client = clientFactory.create();

    private val host = "https://monitorenv.din.developpement-durable.gouv.fr"
  //  private val host = "http://localhost:8089" // TODO: add env var

    override fun findAll(): List<ControlUnitResourceEnv>? {
        val url = "$host/api/v1/control_unit_resources";
        logger.info("Sending GET request for Env control unit resources fetching URL: $url")
        return try {

            val request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .build();

            val response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("APIEnvControlUnitResourceRepository::findAll Response received, Status code: ${response.statusCode()}");

            val body = response.body()
            logger.info("APIEnvControlUnitResourceRepository::findAll Response received, Content: $body")

            if (response.statusCode() == 400 || response.statusCode() == 500) {
                throw Exception("Error while fetching control unit resources from env, please check the logs")
            }

            mapper.registerModule(JtsModule())

            mapper.readValue(body);
        } catch (e: Exception) {
            logger.error("Failed to GET request for Env legacy control units fetching. URL: $url", e);
            null;
        }
    }
}
