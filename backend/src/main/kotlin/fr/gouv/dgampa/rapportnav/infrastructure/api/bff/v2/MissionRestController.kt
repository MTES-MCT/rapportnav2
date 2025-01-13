package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.CreateEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetControlUnitsForUser
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/missions")
class MissionRestController(
    private val createEnvMission: CreateEnvMission,
    private val getEnvMissionById2: GetEnvMissionById2,
    private val getControlUnitsForUser: GetControlUnitsForUser,
) {

    private val logger = LoggerFactory.getLogger(MissionRestController::class.java)

    @GetMapping("{missionId}")
    fun getMissionById(
        @PathVariable(name = "missionId") missionId: Int
    ): MissionEnv? {
         try {
            val mission = getEnvMissionById2.execute(missionId) ?: return null
            return MissionEnv.fromMissionEntity(mission)
        }
        catch (e: Exception) {
            logger.error("Error while creating MonitorEnv mission : ", e)
            return null
        }
    }

    @PostMapping("")
    @Operation(summary = "Create a new MonitorEnv mission")
    fun createMission(
        @RequestBody body: MissionGeneralInfo2
    ): MissionEnv? {
        try {
            val mission = createEnvMission.execute(
                missionGeneralInfo = body,
                controlUnitIds = getControlUnitsForUser.execute()
            )?: return null
            return MissionEnv.fromMissionEnvEntity(mission)
        }
        catch (e: Exception) {
            logger.error("Error while creating MonitorEnv mission : ", e)
            return null
        }
    }
}
