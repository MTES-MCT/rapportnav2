package fr.gouv.dgampa.rapportnav.infrastructure.rapportnav1

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.ExportParams
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IRpnExportRepository
import fr.gouv.dgampa.rapportnav.infrastructure.rapportnav1.adapters.inputs.ExportMissionODTInput
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.SocketAddress
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers


@Repository
class APIRpnExportRepository(
    private val mapper: ObjectMapper,
) : IRpnExportRepository {

    private val logger = LoggerFactory.getLogger(APIRpnExportRepository::class.java)
    override fun exportOdt(params: ExportParams): MissionExportEntity? {
//        val url = "https://rapport-mission-dcs.din.developpement-durable.gouv.fr/public_api/export/odt"
        val url = "https://rapportnav.kalik-sandbox.ovh/public_api/export/odt"

        // Set the proxy host and port
        val proxyHost = "172.27.229.197"
        val proxyPort = 8090

        // Create a Proxy object
        val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(proxyHost, proxyPort))

        // Create a ProxySelector with the given Proxy
        val proxySelector = object : java.net.ProxySelector() {
            override fun select(uri: URI?): MutableList<Proxy> {
                val list = mutableListOf<Proxy>()
                list.add(proxy)
                return list
            }

            override fun connectFailed(uri: URI?, sa: SocketAddress?, ioe: IOException?) {
                // Handle connection failure here if needed
                throw Exception("ExportMission - connectFailed")
            }
        }

        // Create an HttpClient with the proxy selector
        val client = HttpClient.newBuilder()
            .proxy(proxySelector)
            .build()

        val content = ExportMissionODTInput(
            service = params.service,
            id = params.id,
            startDateTime = params.startDateTime,
            endDateTime = params.endDateTime,
            presenceMer = params.presenceMer,
            presenceQuai = params.presenceQuai,
            indisponibilite = params.indisponibilite,
            nbJoursMer = params.nbJoursMer,
            dureeMission = params.dureeMission,
            patrouilleEnv = params.patrouilleEnv,
            patrouilleMigrant = params.patrouilleMigrant,
            distanceMilles = params.distanceMilles,
            goMarine = params.goMarine,
            essence = params.essence,
            crew = params.crew,
            timeline = params.timeline
        )

        val gson = Gson();
        val json = gson.toJson(content)

        logger.info(json)

        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(json))
            .build()

        val response = client.send(request, BodyHandlers.ofString())

        return gson.fromJson(response.body(), MissionExportEntity::class.java)
    }
}
