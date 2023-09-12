package fr.gouv.dgampa.rapportnav.infrastructure.api

import fr.gouv.dgampa.rapportnav.domain.entities.mission.Mission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorEnv.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.GetFishMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.GetEnvMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.GetEnvMissions
import fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.outputs.MissionDataOutput
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.websocket.server.PathParam
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.ZonedDateTime
import org.slf4j.LoggerFactory

@RestController
@RequestMapping("/api/v1/missions")
@Tag(description = "API Missions", name = "Missions")
class ApiMissionsController(
    private val getEnvMissions: GetEnvMissions,
    private val getEnvMissionById: GetEnvMissionById,
    private val getFishMissionById: GetFishMissionById,
    ) 
{
    private val logger = LoggerFactory.getLogger(ApiMissionsController::class.java)

    @GetMapping("")
    @Operation(summary = "Get missions")
    fun getMissionsController(
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
        @Parameter(description = "Origine")
        @RequestParam(name = "missionSource", required = false)
        missionSources: List<MissionSourceEnum>?,
        @Parameter(description = "Types de mission")
        @RequestParam(name = "missionTypes", required = false)
        missionTypes: List<String>?,
        @Parameter(description = "Statut de mission")
        @RequestParam(name = "missionStatus", required = false)
        missionStatuses: List<String>?,
        @Parameter(description = "Facades")
        @RequestParam(name = "seaFronts", required = false)
        seaFronts: List<String>?,
    ): List<MissionDataOutput> {
        val missions = getEnvMissions.execute(
            startedAfterDateTime = startedAfterDateTime,
            startedBeforeDateTime = startedBeforeDateTime,
            missionSources = missionSources,
            missionStatuses = missionStatuses,
            missionTypes = missionTypes,
            seaFronts = seaFronts,
            pageNumber = pageNumber,
            pageSize = pageSize,
        )
        return missions.map { MissionDataOutput.fromMission(it) }
    }

    @GetMapping("/{missionId}")
    @Operation(summary = "Get mission by Id")
    fun getMissionByIdController(
        @PathParam("Mission id")
        @PathVariable(name = "missionId")
        missionId: Int,
    ): MissionDataOutput {
        val envMission = getEnvMissionById.execute(missionId = missionId)
        val fishMission = getFishMissionById.execute(missionId = missionId)

        val mission = Mission(envMission, fishMission)

        return MissionDataOutput.fromMission(mission)
    }
}
