package fr.gouv.dgampa.rapportnav.domain.use_cases.missions

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.EnvMission
import org.n52.jackson.datatype.jts.JtsModule
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.ZonedDateTime


@UseCase
class GetEnvMissions(private val mapper: ObjectMapper) {
    private val logger = LoggerFactory.getLogger(GetEnvMissions::class.java
    )

    fun execute(
        userId: Int?,
        startedAfterDateTime: ZonedDateTime?,
        startedBeforeDateTime: ZonedDateTime?,
        pageNumber: Int?,
        pageSize: Int?,
    ): List<MissionEntity> {

        val client: HttpClient = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
          .uri(URI.create(
            "https://monitorenv.din.developpement-durable.gouv.fr/api/v1/missions"
          ))
          .build();

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        mapper.registerModule(JtsModule())

        val envMissions: List<EnvMission> = mapper.readValue(response.body(), object : TypeReference<List<EnvMission>>() {})

        val missions = envMissions.map { MissionEntity(envMission = it) }

        logger.info("Found ${missions.size} missions ")

        return missions
    }
}
