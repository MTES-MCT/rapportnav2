package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@UseCase
class GetNatinfs(
    private val mapper: ObjectMapper,
    @param:Value("\${MONITORENV_HOST}") private val host: String,
) {

    @Cacheable(value = ["natinfs"])
    fun execute(): List<NatinfEntity> {

        // TODO: move this into infrastructure/APIMonitorEnv
        // TODO: and then move @Cacheable in that newly created function
        val client: HttpClient = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder().uri(
            URI.create(
                "$host/bff/v1/natinfs"
            )
        ).build();


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
//        val natinf4 = NatinfEntity(
//            infraction = "filet peche blabla mise en danger blabla mise en danger blabla mise en danger blabla mise en danger blabla mise en danger blabla mise en danger blabla",
//            natinfCode = 0
//        )
//        val natinf5 = NatinfEntity(
//            infraction = "filet peche blabla mise en danger blabla mise en danger blabla mise en danger blabla mise en danger blabla mise en danger blabla mise en danger blabla",
//            natinfCode = 118
//        )
//        val natinf6 = NatinfEntity(
//            infraction = "filet peche blabla mise en danger blabla mise en danger blabla mise en danger blabla mise en danger blabla mise en danger blabla mise en danger blabla",
//            natinfCode = 666
//        )
//        return listOf(natinf1, natinf2, natinf3, natinf4, natinf5, natinf6)
    }
}
