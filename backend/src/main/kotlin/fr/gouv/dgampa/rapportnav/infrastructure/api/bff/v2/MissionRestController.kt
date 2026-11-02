package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetServiceForUser
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Mission
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionListPage
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/v2/missions")
class MissionRestController(
    private val getComputeEnvMission: GetComputeEnvMission,
    private val getServiceForUser: GetServiceForUser,
    private val createMission: CreateMission,
    private val getMissionList: GetMissionList,
    private val getComputeNavMission: GetComputeNavMission,
    private val deleteMission: DeleteMission,
    private val getMissionByExternalId: GetMissionByExternalId
) {

    /**
     * Retrieves one "load more" page of the user's missions, newest first, combining MonitorEnv missions (scoped to
     * the user's control units) with the user's local nav-only missions.
     *
     * Dates are optional: with no date range the most recent missions overall are returned. Pagination is driven by
     * `offset` / `limit` over the merged stream; the response carries `hasMore` / `nextOffset` for the next page.
     *
     * @param startDateTimeUtc Optional start of the date range (UTC) to narrow the list.
     * @param endDateTimeUtc Optional end of the date range (UTC) to narrow the list.
     * @param statuses Optional multi-select filter on the computed mission status.
     * @param completenessStatuses Optional multi-select filter on the stats-completeness status.
     * @param reportTypes Optional multi-select filter on the mission report type.
     * @param offset Index into the merged stream for this page (default 0).
     * @param limit Page size (default 15).
     * @return A [MissionListPage] with the page items and `hasMore` / `nextOffset` paging metadata.
     */
    @GetMapping("")
    @Operation(summary = "Get a paginated list of missions for a specific user")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found missions", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = MissionListPage::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any missions", content = [Content()])
        ]
    )
    fun getMissions(
        @RequestParam(name = "startDateTimeUtc", required = false) startDateTimeUtc: Instant? = null,
        @RequestParam(name = "endDateTimeUtc", required = false) endDateTimeUtc: Instant? = null,
        @RequestParam(name = "statuses", required = false) statuses: List<MissionStatusEnum>? = null,
        @RequestParam(name = "completenessStatuses", required = false) completenessStatuses: List<CompletenessForStatsStatusEnum>? = null,
        @RequestParam(name = "reportTypes", required = false) reportTypes: List<MissionReportTypeEnum>? = null,
        @RequestParam(name = "offset", required = false, defaultValue = "0") offset: Int = 0,
        @RequestParam(name = "limit", required = false, defaultValue = "15") limit: Int = 15,
    ) : MissionListPage {
        return getMissionList.execute(
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            filter = MissionListFilter(
                statuses = statuses,
                completenessStatuses = completenessStatuses,
                reportTypes = reportTypes,
            ),
            offset = offset,
            limit = limit,
        )
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
                        schema = Schema(implementation = Mission::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any mission", content = [Content()])
        ]
    )
    fun getMissionById(
        @PathVariable(name = "missionId") missionId: String
    ): Mission {
        val mission = if (isValidUUID(missionId)) {
            getComputeNavMission.execute(missionId = UUID.fromString(missionId))
        } else {
            getComputeEnvMission.execute(missionId = Integer.valueOf(missionId))
        }
        return Mission.fromMissionEntity(mission)
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
                        schema = Schema(implementation = Mission::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not create no mission", content = [Content()])
        ]
    )
    fun create(
        @RequestBody body: MissionGeneralInfo2
    ): Mission {
        val mission = createMission.execute(
            generalInfo2 = body,
            service = getServiceForUser.execute()
        )
        return Mission.fromMissionEntity(mission)
    }


    @DeleteMapping("{missionId}")
    @Operation(summary = "Delete a mission created by the unit")
    @ApiResponse(responseCode = "404", description = "Could not delete mission", content = [Content()])
    fun delete(
        @PathVariable missionId: String
    ) {
        val serviceId = getServiceForUser.execute()?.id
        // Resolve to the local mission UUID (env missions still come in by their MonitorEnv Int id),
        // then delete through the single DeleteMission path, which removes MonitorEnv + the local row.
        val id = if (isValidUUID(missionId)) {
            UUID.fromString(missionId)
        } else {
            getMissionByExternalId.execute(missionId)?.id
        }
        deleteMission.execute(id = id, serviceId = serviceId)
    }
}
