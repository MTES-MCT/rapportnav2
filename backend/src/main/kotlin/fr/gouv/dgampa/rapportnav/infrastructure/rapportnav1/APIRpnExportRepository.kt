package fr.gouv.dgampa.rapportnav.infrastructure.rapportnav1

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.ExportParams
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IRpnExportRepository
import fr.gouv.dgampa.rapportnav.infrastructure.rapportnav1.adapters.inputs.ExportMissionODTInput
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
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
    @Value("\${host.proxy.host}") private val proxyHost: String,
    @Value("\${host.proxy.port}") private val proxyPort: String
) : IRpnExportRepository {

    private val logger = LoggerFactory.getLogger(APIRpnExportRepository::class.java)
    override fun exportOdt(params: ExportParams): MissionExportEntity? {
//        val url = "https://rapport-mission-dcs.din.developpement-durable.gouv.fr/public_api/export/odt"
        val url = "https://rapportnav.kalik-sandbox.ovh/public_api/export/odt"


        // Create a Proxy object
        var proxy: Proxy? = null
        if (!proxyHost.isNullOrEmpty() && !proxyPort.isNullOrEmpty()) {
            proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(proxyHost, proxyPort.toInt()))
        }

        // Create a ProxySelector with the given Proxy
        val proxySelector = object : java.net.ProxySelector() {
            override fun select(uri: URI?): MutableList<Proxy> {
                val list = mutableListOf<Proxy>()
                if (proxy !== null) {
                    list.add(proxy)
                }
                return list
            }

            override fun connectFailed(uri: URI?, sa: SocketAddress?, ioe: IOException?) {
                // Handle connection failure here if needed
                logger.error("[RapportDePatrouille] - fail to connect to RapportNav1 at ${uri?.toString()}")
                throw Exception("[RapportDePatrouille] - fail to connect to RapportNav1")
            }
        }


        // Create an HttpClient with the proxy selector
        var client = HttpClient.newBuilder()
            .proxy(proxySelector)
            .build()

        val content = ExportMissionODTInput(
            service = params.service ?: "",
            id = params.id,
            startDateTime = params.startDateTime?.toLocalDate().toString(),
            endDateTime = params.endDateTime?.toLocalDate().toString(),
            presenceMer = params.presenceMer,
            presenceQuai = params.presenceQuai,
            indisponibilite = params.indisponibilite,
            nbJoursMer = params.nbJoursMer,
            dureeMission = params.dureeMission,
            patrouilleSurveillanceEnvInHours = params.patrouilleSurveillanceEnvInHours,
            patrouilleMigrantInHours = params.patrouilleMigrantInHours,
            distanceMilles = params.distanceMilles ?: 0.0f,
            goMarine = params.goMarine ?: 0.0f,
            essence = params.essence ?: 0.0f,
            crew = params.crew,
            timeline = params.timeline,
            rescueInfo = params.rescueInfo,
            nauticalEventsInfo = params.nauticalEventsInfo,
            antiPollutionInfo = params.antiPollutionInfo,
            baaemAndVigimerInfo = params.baaemAndVigimerInfo,
            observations = params.observations
        )

        val gson = Gson();
        val json = gson.toJson(content)

        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(json))
            .build()

        return try {
            logger.info("[RapportDePatrouille] - sending request to RapportNav1 at $url")
            logger.info(json)
            val response = client.send(request, BodyHandlers.ofString())
            logger.info("[RapportDePatrouille] - received response RapportNav1 at $url, status = ${response.statusCode()}")
            logger.info(response.body().toString())
            gson.fromJson(response.body(), MissionExportEntity::class.java)
        } catch (e: Exception) {
            logger.error("[RapportDePatrouille] - RapportNav1 returns error: ${e.message}", e)
            null // or handle the error according to your use case
        }
    }
}
