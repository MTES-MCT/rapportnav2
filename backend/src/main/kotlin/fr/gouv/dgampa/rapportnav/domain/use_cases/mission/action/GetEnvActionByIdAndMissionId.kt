package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissionById
//import org.n52.jackson.datatype.jts.JtsModule
//import java.net.URI
//import java.net.http.HttpClient
//import java.net.http.HttpRequest
//import java.net.http.HttpResponse
import java.util.*


@UseCase
class GetEnvActionByIdAndMissionId(
    private val getEnvMissionById: GetEnvMissionById,
    private val attachControlsToActionControl: AttachControlsToActionControl,
) {
    fun execute(id: UUID, missionId: Int): ExtendedEnvActionEntity? {
        // MonitorEnv doesn't have an action endpoint so fetch the mission
        val mission = getEnvMissionById.execute(missionId = missionId)

        // and then filter the actions by id
        return mission?.actions?.firstOrNull {
            (it?.controlAction?.action?.id == id) ||
                    (it?.surveillanceAction?.action?.id == id)
        }?.let {
            if (it.surveillanceAction != null) {
                it.surveillanceAction.computeCompleteness()
                it
            } else {
                attachControlsToActionControl.toEnvAction(
                    actionId = id.toString(),
                    action = it
                )
                // recompute completeness once controls have been attached
                it.controlAction?.computeCompleteness()
                it
            }
        }
    }
}
