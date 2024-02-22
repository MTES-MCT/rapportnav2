package fr.gouv.dgampa.rapportnav.infrastructure.rapportnav1

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IRpnExportRepository
import fr.gouv.dgampa.rapportnav.infrastructure.rapportnav1.adapters.outputs.RpnExportOdtOutput
import org.springframework.stereotype.Repository
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers

@Repository
class APIRpnExportRepository(
    private val mapper: ObjectMapper
): IRpnExportRepository {
    override fun exportOdt(
        service: String?,
        id: String,
        startDateTime: String?,
        endDateTime: String?,
        presenceMer: Map<String, Int>,
        presenceQuai: Map<String, Int>,
        indisponibilite: Map<String, Int>,
        nbJoursMer: Int,
        dureeMission: Int,
        patrouilleEnv: Int,
        patrouilleMigrant: Int,
        distanceMilles: Float?,
        goMarine: Float?,
        essence: Float?,
        crew: List<MissionCrewEntity>
    ) {
        val url = "http://127.0.0.1:8000/public_api/export/odt"
        val client = HttpClient.newHttpClient()

        val content = RpnExportOdtOutput(
            service,
            id,
            startDateTime,
            endDateTime,
            presenceMer,
            presenceQuai,
            indisponibilite,
            nbJoursMer,
            dureeMission,
            patrouilleEnv,
            patrouilleMigrant,
            distanceMilles,
            goMarine,
            essence,
            crew
        )

        val gson = Gson();
        val json = gson.toJson(content)

        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(json))
            .build()

        val response = client.send(request, BodyHandlers.ofString())
    }
}
