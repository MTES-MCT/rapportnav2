package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.CreateEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetControlUnitsForUser
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnv
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/missions")
class MissionRestController(
    private val createEnvMission: CreateEnvMission,
    private val getControlUnitsForUser: GetControlUnitsForUser,
) {

    private val logger = LoggerFactory.getLogger(MissionRestController::class.java)

    @PostMapping("")
    @Operation(summary = "Create a new MonitorEnv mission")
    fun create(
        @RequestBody body: MissionEnv
    ): MissionEnv? {
        try {
            val mission = createEnvMission.execute(
                mission = body,
                controlUnitIds = getControlUnitsForUser.execute()
            )

            if (mission != null) {
                return MissionEnv.fromMissionEntity(mission)
            }

            throw RuntimeException("Error - can't create new MonitorEnv mission")
        }
        catch (e: Exception) {
            logger.error("Error while creating MonitorEnv mission : ", e)
            return null
        }
    }
}
