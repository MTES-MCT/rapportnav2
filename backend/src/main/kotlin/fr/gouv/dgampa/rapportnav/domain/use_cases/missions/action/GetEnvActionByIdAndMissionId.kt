package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.GetEnvMissionById
//import org.n52.jackson.datatype.jts.JtsModule
//import java.net.URI
//import java.net.http.HttpClient
//import java.net.http.HttpRequest
//import java.net.http.HttpResponse
import java.util.*


@UseCase
class GetEnvActionByIdAndMissionId(
    private val getEnvMissionById: GetEnvMissionById,
    private val mapper: ObjectMapper
) {
    fun execute(id: UUID, missionId: Int): EnvActionEntity? {
        val mission = getEnvMissionById.execute(missionId = missionId)

        return mission.envActions?.firstOrNull { it.id == id }
    }
}
