package fr.gouv.dgampa.rapportnav.domain.use_cases.missions

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FishMission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.Mission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.MissionSource
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.MissionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import org.n52.jackson.datatype.jts.JtsModule
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.ZonedDateTime


@UseCase
class GetFishMissionById(private val mapper: ObjectMapper) {
    fun execute(missionId: Int): FishMission {

      val client: HttpClient = HttpClient.newBuilder().build()
      val request = HttpRequest.newBuilder()
        .uri(
          URI.create(
          "https://monitorfish.din.developpement-durable.gouv.fr/bff/v1/missions/$missionId"
        ))
        .build();

      val response = client.send(request, HttpResponse.BodyHandlers.ofString())

      mapper.registerModule(JtsModule())

      return mapper.readValue(response.body(), object : TypeReference<FishMission>() {})
    }
}
