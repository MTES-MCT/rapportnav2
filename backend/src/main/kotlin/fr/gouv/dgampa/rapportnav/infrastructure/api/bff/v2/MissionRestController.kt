package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetServiceForUser
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Mission2
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/v2/missions")
class MissionRestController(
    private val getComputeEnvMission: GetComputeEnvMission,
    private val getServiceForUser: GetServiceForUser,
    private val createMission: CreateMission,
    private val getMissions: GetMissions,
    private val getComputeNavMission: GetComputeNavMission,
    private val deleteMissionNav: DeleteMissionNav
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
    ): List<Mission2?>? {
        try {
            val missions = getMissions.execute(
                startDateTimeUtc = startDateTimeUtc,
                endDateTimeUtc = endDateTimeUtc,
            )

            return (missions.filterNotNull()).map { Mission2.fromMissionEntity((it)) }
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
        @PathVariable(name = "missionId") missionId: String
    ): Mission2? {
        try {
            val mission = if (isValidUUID(missionId)) getComputeNavMission.execute(
                missionId = UUID.fromString(missionId)
            ) else getComputeEnvMission.execute(
                missionId = Integer.valueOf(missionId)
            )
            return mission?.let { Mission2.fromMissionEntity(it) }
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
    fun create(
        @RequestBody body: MissionGeneralInfo2
    ): Mission2? {
        return try {
            createMission.execute(
                generalInfo2 = body,
                service = getServiceForUser.execute()
            )?.let { Mission2.fromMissionEntity(it) }
        } catch (e: Exception) {
            logger.error("Error while creating MonitorEnv mission : ", e)
            return null
        }
    }


    @DeleteMapping("{missionId}")
    @Operation(summary = "Delete a mission create by the unity")
    fun delete(
        @PathVariable(name = "missionId") missionId: String
    ) {
        if (!isValidUUID(missionId)) return //TODO: not deleting env mission create by the unit yet.
        return try {
            deleteMissionNav.execute(
                id = UUID.fromString(missionId),
                serviceId = getServiceForUser.execute()?.id
            )
        } catch (e: Exception) {
            logger.error("Error while creating MonitorEnv mission : ", e)
        }
    }
}
