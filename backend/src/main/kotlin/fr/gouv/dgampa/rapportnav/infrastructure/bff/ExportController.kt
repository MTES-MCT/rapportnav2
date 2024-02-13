package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.ExportMission
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class ExportController(
    private val exportMission: ExportMission
) {

    private val logger = LoggerFactory.getLogger(MissionController::class.java)

    @QueryMapping
    fun export(@Argument missionId: Int): Unit {
        try {
            exportMission.exportOdt(missionId)
        }
        catch (e: Exception) {
            logger.error("MissionController - failed to load missions from MonitorEnv", e)
            throw Exception(e)
        }
    }
}
