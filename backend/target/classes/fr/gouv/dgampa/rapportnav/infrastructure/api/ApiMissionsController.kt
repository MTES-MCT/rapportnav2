package fr.gouv.dgampa.rapportnav.infrastructure.api

import fr.gouv.dgampa.rapportnav.domain.entities.monitorenv.mission.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.GetMonitorEnvMissions
import fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.outputs.MissionDataOutput
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime

@RestController
@RequestMapping("/api/v1/missions")
@Tag(description = "API Missions", name = "Missions")
class ApiMissionsController(
    private val getMissions: GetMonitorEnvMissions,
) {
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
        val missions = getMissions.execute(
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
}
