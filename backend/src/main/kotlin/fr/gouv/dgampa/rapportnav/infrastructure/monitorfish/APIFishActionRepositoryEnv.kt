package fr.gouv.dgampa.rapportnav.infrastructure.monitorfish

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import org.n52.jackson.datatype.jts.JtsModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class APIFishActionRepositoryEnv(
    private val mapper: ObjectMapper
) : IFishActionRepository {
    private val logger: Logger = LoggerFactory.getLogger(APIFishActionRepositoryEnv::class.java)
    override fun findFishActions(missionId: Int): List<MissionAction> {
        val client: HttpClient = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(
                URI.create(
                    "https://monitorfish.din.developpement-durable.gouv.fr/api/v1/mission_actions?missionId=$missionId"
                )
            )
            .build();

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() in 200..299) {
            mapper.registerModule(JtsModule())
            val fishActions = mapper.readValue(response.body(), object : TypeReference<List<MissionAction>>() {})
            return fishActions
        } else {
            logger.info("Failed to fetch data. Status code: ${response.statusCode()}")
            return listOf()
        }
    }
}
