package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetServiceForUser
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Mission2
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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
    private val deleteNavMission: DeleteNavMission,
    private val deleteEnvMission: DeleteEnvMission
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
    @Operation(summary = "Get the list of missions for a specific user")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found missions", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Mission2::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any missions", content = [Content()])
        ]
    )
    fun getMissions(
        @RequestParam("startDateTimeUtc") startDateTimeUtc: Instant,
        @RequestParam(name = "endDateTimeUtc", required = false) endDateTimeUtc: Instant? = null
    ) : List<Mission2> {
        val missions = getMissions.execute(
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
        )

        return (missions.filterNotNull()).map { Mission2.fromMissionEntity((it)) }

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
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found mission by id", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Mission2::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any mission", content = [Content()])
        ]
    )
    fun getMissionById(
        @PathVariable(name = "missionId") missionId: String
    ): Mission2 {
        val mission = if (isValidUUID(missionId)) {
            getComputeNavMission.execute(missionId = UUID.fromString(missionId))
        } else {
            getComputeEnvMission.execute(missionId = Integer.valueOf(missionId))
        }
        return Mission2.fromMissionEntity(mission)
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
    @Operation(summary = "Create Mission")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Create mission", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Mission2::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not create no mission", content = [Content()])
        ]
    )
    fun create(
        @RequestBody body: MissionGeneralInfo2
    ): Mission2 {
        val mission = createMission.execute(
            generalInfo2 = body,
            service = getServiceForUser.execute()
        )
        return Mission2.fromMissionEntity(mission)
    }


    @DeleteMapping("{missionId}")
    @Operation(summary = "Delete a mission create by the unity")
    @ApiResponse(responseCode = "404", description = "Could not delete mission", content = [Content()])
    fun delete(
        @PathVariable(name = "missionId") missionId: String
    ) {
        val serviceId = getServiceForUser.execute()?.id
        return try {
            if (isValidUUID(missionId)) {
                deleteNavMission.execute(id = UUID.fromString(missionId), serviceId = serviceId)
            } else {
                deleteEnvMission.execute(id = Integer.parseInt(missionId), serviceId = serviceId)
            }
        } catch (e: Exception) {
            logger.error("Error while creating MonitorEnv mission : ", e)
        }
    }
}
