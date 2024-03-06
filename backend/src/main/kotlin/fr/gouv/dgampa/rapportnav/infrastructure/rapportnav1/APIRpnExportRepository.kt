package fr.gouv.dgampa.rapportnav.infrastructure.rapportnav1

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.ExportParams
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IRpnExportRepository
import fr.gouv.dgampa.rapportnav.infrastructure.rapportnav1.adapters.inputs.ExportMissionODTInput
import org.springframework.stereotype.Repository
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers

@Repository
class APIRpnExportRepository(
    private val mapper: ObjectMapper
) : IRpnExportRepository {
    override fun exportOdt(params: ExportParams): MissionExportEntity? {
        val url = "https://rapport-mission-dcs.din.developpement-durable.gouv.fr/public_api/export/odt"
        val client = HttpClient.newHttpClient()

        val content = ExportMissionODTInput(
            service = params.service,
            id = params.id,
            startDateTime = params.startDateTime?.toString(),
            endDateTime = params.endDateTime?.toString(),
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

        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(json))
            .build()

        val response = client.send(request, BodyHandlers.ofString())

        return gson.fromJson(response.body(), MissionExportEntity::class.java)
    }
}
