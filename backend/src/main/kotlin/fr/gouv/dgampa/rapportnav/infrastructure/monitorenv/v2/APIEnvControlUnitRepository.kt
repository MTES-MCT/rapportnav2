package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnit.IEnvControlUnitRepository
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs.LegacyControlUnitDataOutput
import org.n52.jackson.datatype.jts.JtsModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class APIEnvControlUnitRepository(
    clientFactory: HttpClientFactory,
    private val mapper: ObjectMapper
): IEnvControlUnitRepository {

    private val logger: Logger = LoggerFactory.getLogger(APIEnvControlUnitRepository::class.java);

    private val client = clientFactory.create();

    // private val host = System.getenv("MONITORENV_HOST")

    private val host = "https://monitorenv.din.developpement-durable.gouv.fr"

    override fun findAll(): List<LegacyControlUnitEntity>? {
        val url = "$host/api/v1/control_units";
        logger.info("Sending GET request for Env legacy control units fetching URL: $url")
        return try {

            val request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .build();

            val response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("EnvLegacyControlUnitRepository::findAll Response received, Status code: ${response.statusCode()}");

            val body = response.body()
            logger.info("EnvLegacyControlUnitRepository::findAll Response received, Content: $body")

            if (response.statusCode() == 400 || response.statusCode() == 500) {
                throw Exception("Error while fetching legacy control units from env, please check the logs")
            }

            mapper.registerModule(JtsModule())
            val outputs: List<LegacyControlUnitDataOutput>? = mapper.readValue(body);
            outputs?.map { it.toLegacyControlUnitEntity() }
        } catch (e: Exception) {
            logger.error("Failed to GET request for Env legacy control units fetching. URL: $url", e);
            null;
        }
    }

    override fun findById(id: Int): LegacyControlUnitEntity? {
        val url = "$host/api/v1/control_units/${id}";
        logger.info("Sending GET request for Env legacy control unit by id fetching URL: $url")
        return try {

            val request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .build();

            val response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("EnvLegacyControlUnitRepository::findById Response received, Status code: ${response.statusCode()}");

            val body = response.body()
            logger.info("EnvLegacyControlUnitRepository::findAll Response received, Content: $body")

            if (response.statusCode() == 400 || response.statusCode() == 500) {
                throw Exception("Error while fetching legacy control unit by id from env, please check the logs")
            }

            mapper.registerModule(JtsModule())
            val output: LegacyControlUnitDataOutput? = mapper.readValue(body);
            output?.toLegacyControlUnitEntity()
        } catch (e: Exception) {
            logger.error("Failed to GET request for Env legacy control unit by id fetching. URL: $url", e);
            null;
        }
    }
}
