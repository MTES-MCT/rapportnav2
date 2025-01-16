package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.CreateEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.FakeMissionData
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissions
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetControlUnitsForUser
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.Mission
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.time.Instant
import kotlin.collections.plus

@RestController
@RequestMapping("/api/v2/missions")
class MissionRestController(
    private val createEnvMission: CreateEnvMission,
    private val getEnvMissionById2: GetEnvMissionById2,
    private val getControlUnitsForUser: GetControlUnitsForUser,
    private val getUserFromToken: GetUserFromToken,
    private val getEnvMissions: GetEnvMissions,
    private val getMission: GetMission,
    private val fakeMissionData: FakeMissionData,
) {

    private val logger = LoggerFactory.getLogger(MissionRestController::class.java)


    /**
     * Retrieves a list of missions from the environment and combines them with fictive missions specific to the user.
     *
     * This endpoint accepts query parameters `startDateTimeUtc` (required) and `endDateTimeUtc` (optional) to filter missions
     * within a specific date range. It queries the environment for missions matching the user's control units and enriches
     * the data to ensure completeness. Additionally, it appends fictive missions tailored for the user.
     *
     * @param startDateTimeUtc The start of the date range (UTC) for filtering missions.
     * @param endDateTimeUtc The end of the date range (UTC) for filtering missions. Optional.
     * @return A response containing a list of enriched missions, both retrieved and fictive, or an error status if the process fails.
     */
    @GetMapping("")
    @Operation(summary = "Get the list of actions on a mission Id")
    fun getMissions(
        @RequestParam startDateTimeUtc: Instant,
        @RequestParam(required = false) endDateTimeUtc: Instant? = null
    ): List<Mission?> {
        try {
            // query MonitorEnv with the following filters
            val envMissions = getEnvMissions.execute(
                startedAfterDateTime = startDateTimeUtc,
                startedBeforeDateTime = endDateTimeUtc,
                pageNumber = null,
                pageSize = null,
                controlUnits = getControlUnitsForUser.execute()
            )

            // reconstruct the Missions with full data in order to have the right status/completeness
            val fullMissions = envMissions?.map { getMission.execute(it.id, it) }

            // transform the data for the API
            val missions = fullMissions?.mapNotNull { it?.let { Mission.fromMissionEntity(it) } } ?: emptyList()


            // temporarily add fictive missions
            val user = getUserFromToken.execute()
            val fakeMissions = fakeMissionData.getFakeMissionsforUser(user).map { Mission.fromMissionEntity(it) }

            return missions + fakeMissions
        } catch (e: Exception) {
            logger.error("MissionRestController - failed to load missions from MonitorEnv", e)
            throw Exception(e)
        }
    }

    /**
     * Retrieves a specific mission by its ID.
     *
     * This endpoint fetches a mission identified by the provided `missionId` path variable. If the mission exists, it
     * returns the mission data transformed into the API response format. If the mission does not exist or an error occurs,
     * it returns null.
     *
     * @param missionId The unique identifier of the mission to retrieve.
     * @return The mission data as a `MissionEnv` object, or null if not found or an error occurs.
     */
    @GetMapping("{missionId}")
    fun getMissionById(
        @PathVariable(name = "missionId") missionId: Int
    ): MissionEnv? {
        try {
            val mission = getEnvMissionById2.execute(missionId) ?: return null
            return MissionEnv.fromMissionEntity(mission)
        } catch (e: Exception) {
            logger.error("Error while creating MonitorEnv mission : ", e)
            return null
        }
    }


    /**
     * Creates a new mission in the MonitorEnv system.
     *
     * This endpoint accepts a request body containing the general information about the mission to be created. It uses
     * the user's control units to associate the mission with the appropriate entities. Upon successful creation, it returns
     * the newly created mission in the API response format.
     *
     * @param body The general information required to create a new mission.
     * @return The created mission as a `MissionEnv` object, or null if an error occurs during creation.
     */
    @PostMapping("")
    @Operation(summary = "Create a new MonitorEnv mission")
    fun createMission(
        @RequestBody body: MissionGeneralInfo2
    ): MissionEnv? {
        try {
            val mission = createEnvMission.execute(
                missionGeneralInfo = body,
                controlUnitIds = getControlUnitsForUser.execute()
            ) ?: return null
            return MissionEnv.fromMissionEnvEntity(mission)
        } catch (e: Exception) {
            logger.error("Error while creating MonitorEnv mission : ", e)
            return null
        }
    }
}
