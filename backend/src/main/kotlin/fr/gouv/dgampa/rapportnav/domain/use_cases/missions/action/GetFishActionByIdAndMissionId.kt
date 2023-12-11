package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.GetFishActionsByMissionId
//import org.n52.jackson.datatype.jts.JtsModule
//import java.net.URI
//import java.net.http.HttpClient
//import java.net.http.HttpRequest
//import java.net.http.HttpResponse


@UseCase
class GetFishActionByIdAndMissionId(
    private val getFishActionsByMissionId: GetFishActionsByMissionId,
    private val mapper: ObjectMapper
) {
    fun execute(id: Int, missionId: Int): FishAction? {
        val actions = getFishActionsByMissionId.execute(missionId = missionId)
        return actions.firstOrNull { it.id == id }
    }
}
