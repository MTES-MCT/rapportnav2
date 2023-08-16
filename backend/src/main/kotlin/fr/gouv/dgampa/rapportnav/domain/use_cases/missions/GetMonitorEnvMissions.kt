package fr.gouv.dgampa.rapportnav.domain.use_cases.missions

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.monitorenv.controlResources.ControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.monitorenv.mission.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.monitorenv.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.monitorenv.mission.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.monitorenv.mission.MissionTypeEnum
import org.locationtech.jts.geom.MultiPolygon
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.time.ZonedDateTime

@UseCase
class GetMonitorEnvMissions() {
    private val logger = LoggerFactory.getLogger(GetMonitorEnvMissions::class.java)

    fun execute(
        startedAfterDateTime: ZonedDateTime?,
        startedBeforeDateTime: ZonedDateTime?,
        missionSources: List<MissionSourceEnum>?,
        missionTypes: List<String>?,
        missionStatuses: List<String>?,
        pageNumber: Int?,
        pageSize: Int?,
        seaFronts: List<String>?,
    ): List<MissionEntity> {


        // TODO add API call to MonitorEnv

        val mission1 = MissionEntity(
            id = 10,
            missionTypes = listOf(MissionTypeEnum.SEA),
            facade = "Outre-Mer",
            observationsCacem = null,
            startDateTimeUtc = ZonedDateTime.parse("2022-02-15T04:50:09Z"),
            endDateTimeUtc = ZonedDateTime.parse("2022-02-23T20:29:03Z"),
            isClosed = false,
            isDeleted = false,
            missionSource = MissionSourceEnum.RAPPORTNAV,
            hasMissionOrder = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false
        )
        val mission2 = MissionEntity(
            id = 11,
            missionTypes = listOf(MissionTypeEnum.SEA),
            facade = "Outre-Mer",
            observationsCacem = null,
            startDateTimeUtc = ZonedDateTime.parse("2022-01-15T04:50:09Z"),
            endDateTimeUtc = ZonedDateTime.parse("2022-01-23T20:29:03Z"),
            isClosed = true,
            isDeleted = false,
            missionSource = MissionSourceEnum.MONITORENV,
            hasMissionOrder = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false
        )

        val missions = listOf(mission1, mission2)

        logger.info("Found ${missions.size} missions ")

        return missions
    }
}
