package fr.gouv.dgampa.rapportnav.domain.use_cases.missions

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.Mission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorEnv.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorEnv.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorEnv.MissionTypeEnum
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime

@UseCase
class GetEnvMissions() {
    private val logger = LoggerFactory.getLogger(GetEnvMissions::class.java)

    fun execute(
        userId: Int?,
        startedAfterDateTime: ZonedDateTime?,
        startedBeforeDateTime: ZonedDateTime?,
        pageNumber: Int?,
        pageSize: Int?,
    ): List<Mission> {
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

        val envMissions = listOf(mission1, mission2)

        val missions = envMissions.map { Mission(envMission = it) }

        logger.info("Found ${missions.size} missions ")

        return missions
    }
}
