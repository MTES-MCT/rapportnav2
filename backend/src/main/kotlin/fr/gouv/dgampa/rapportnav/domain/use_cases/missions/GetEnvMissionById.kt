package fr.gouv.dgampa.rapportnav.domain.use_cases.missions

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.EnvMission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import org.n52.jackson.datatype.jts.JtsModule
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


@UseCase
class GetEnvMissionById(private val mapper: ObjectMapper) {
    fun execute(missionId: Int): MissionEntity {

      val client: HttpClient = HttpClient.newBuilder().build()
      val request = HttpRequest.newBuilder()
        .uri(URI.create(
          "https://monitorenv.din.developpement-durable.gouv.fr/api/v1/missions/$missionId"
        ))
        .build();

      val response = client.send(request, HttpResponse.BodyHandlers.ofString())

      mapper.registerModule(JtsModule())

      return mapper.readValue(response.body(), object : TypeReference<EnvMission>() {})
    }
}
