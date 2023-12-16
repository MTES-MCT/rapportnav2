package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository
import org.n52.jackson.datatype.jts.JtsModule
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@UseCase
class GetNatinfs(private val mapper: ObjectMapper) {

    fun execute(): List<NatinfEntity> {

        val client: HttpClient = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(
                URI.create(
                    "https://monitorenv.din.developpement-durable.gouv.fr/bff/v1/natinfs"
                )
            )
            .build();


        val response = client.send(request, HttpResponse.BodyHandlers.ofString())


        return mapper.readValue(response.body(), object : TypeReference<List<NatinfEntity>>() {})

//        val natinf1 = NatinfEntity(
//            infraction = "non respect blabla",
//            natinfCode = 111
//        )
//        val natinf2 = NatinfEntity(
//            infraction = "mise en danger blabla",
//            natinfCode = 222
//        )
//        val natinf3 = NatinfEntity(
//            infraction = "filet peche blabla",
//            natinfCode = 333
//        )
//        return listOf(natinf1, natinf2, natinf3)
    }
}
