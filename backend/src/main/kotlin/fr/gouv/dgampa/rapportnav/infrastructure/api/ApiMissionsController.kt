package fr.gouv.dgampa.rapportnav.infrastructure.api

import fr.gouv.dgampa.rapportnav.domain.entities.mission.Mission
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.inputs.UpdateMissionDataInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.outputs.MissionDataOutput
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.websocket.server.PathParam
import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.ZonedDateTime

@RestController
@RequestMapping("/api/v1/missions")
@Tag(description = "API Missions", name = "Missions")
class ApiMissionsController(
    private val getEnvMissions: GetEnvMissions,
    private val getNavMissionById: GetNavMissionById,
    private val getEnvMissionById: GetEnvMissionById,
    private val getFishMissionById: GetFishMissionById,
    private val updateEnvMission: UpdateEnvMission,
    ) 
{
    private val logger = LoggerFactory.getLogger(ApiMissionsController::class.java)

    @GetMapping("")
    @Operation(summary = "Get missions")
    fun getMissionsController(
        @Parameter(description = "User")
        @RequestParam(name = "userId")
        userId: Int?,
        @Parameter(description = "page number")
        @RequestParam(name = "pageNumber")
        pageNumber: Int?,
        @Parameter(description = "page size")
        @RequestParam(name = "pageSize")
        pageSize: Int?,
        @Parameter(description = "Mission started after date")
        @RequestParam(name = "startedAfterDateTime", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        startedAfterDateTime: ZonedDateTime?,
        @Parameter(description = "Mission started before date")
        @RequestParam(name = "startedBeforeDateTime", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        startedBeforeDateTime: ZonedDateTime?,
    ): List<MissionDataOutput> {
        val missions = getEnvMissions.execute(
            userId = userId,
            startedAfterDateTime = startedAfterDateTime,
            startedBeforeDateTime = startedBeforeDateTime,
            pageNumber = pageNumber,
            pageSize = pageSize,
        )
        return missions.map { MissionDataOutput.fromMission(it) }
    }

//    @PostMapping("", consumes = ["application/json"])
//    @Operation(summary = "Create a new mission")
//    fun createMissionController(
//        @RequestBody
//        createMissionDataInput: CreateOrUpdateMissionDataInput,
//    ): MissionDataOutput {
//        val newMission = createMissionDataInput.toMissionEntity()
//        val createdMission = createOrUpdateMission.execute(mission = newMission)
//        return MissionDataOutput.fromMission(createdMission)
//    }

    @GetMapping("/{missionId}")
    @Operation(summary = "Get mission by Id")
    fun getMissionByIdController(
        @PathParam("Mission id")
        @PathVariable(name = "missionId")
        missionId: Int,
    ): MissionDataOutput {
        val envMission = getEnvMissionById.execute(missionId = missionId)
        val fishMission = getFishMissionById.execute(missionId = missionId)
        val navMission = getNavMissionById.execute(missionId = missionId)

        val mission = Mission(envMission, navMission, fishMission)

        return MissionDataOutput.fromMission(mission)
    }

    @PostMapping(value = ["/{missionId}"], consumes = ["application/json"])
    @Operation(summary = "Update a mission")
    fun updateOperationController(
        @PathParam("Mission Id")
        @PathVariable(name = "missionId")
        missionId: Int,
        @RequestBody
        updateMissionDataInput: UpdateMissionDataInput,
    ): Any {

        // 1. fetch mission from Env
        val envMission = getEnvMissionById.execute(missionId = missionId)

        // 2. if dates have changed - update in Env
        if (envMission.startDateTimeUtc != updateMissionDataInput.startDateTimeUtc || envMission.endDateTimeUtc != updateMissionDataInput.endDateTimeUtc) {
            val missionToSave = envMission.copy(
                startDateTimeUtc = updateMissionDataInput.startDateTimeUtc,
                endDateTimeUtc = updateMissionDataInput.endDateTimeUtc
            )
            updateEnvMission.execute(mission = missionToSave)
        }

        // 3. update actions in own database
        // TODO

        // 4. return data to client
        // TODO

        return 1
    }
}
