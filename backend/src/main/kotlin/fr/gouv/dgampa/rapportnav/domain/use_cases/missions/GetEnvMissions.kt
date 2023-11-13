package fr.gouv.dgampa.rapportnav.domain.use_cases.missions

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.EnvMission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
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
    ): List<MissionEntity> {
        // TODO add API call to MonitorEnv

        val mission1 = EnvMission(
            id = 10,
            missionTypes = listOf(MissionTypeEnum.SEA),
            facade = "Outre-Mer",
            observationsCacem = null,
            startDateTimeUtc = ZonedDateTime.parse("2022-03-15T04:50:09Z"),
//            endDateTimeUtc = ZonedDateTime.parse("2022-03-23T20:29:03Z"),
            isClosed = false,
            isDeleted = false,
            missionSource = MissionSourceEnum.RAPPORTNAV,
            hasMissionOrder = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false,
        )
        val mission2 = EnvMission(
            id = 11,
            missionTypes = listOf(MissionTypeEnum.SEA),
            facade = "Outre-Mer",
            observationsCacem = null,
            startDateTimeUtc = ZonedDateTime.parse("2022-02-15T04:50:09Z"),
            endDateTimeUtc = ZonedDateTime.parse("2022-02-23T20:29:03Z"),
            isClosed = true,
            isDeleted = false,
            missionSource = MissionSourceEnum.MONITORENV,
            hasMissionOrder = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false
        )
        val mission3 = EnvMission(
            id = 12,
            missionTypes = listOf(MissionTypeEnum.SEA),
            facade = "Outre-Mer",
            observationsCacem = null,
            startDateTimeUtc = ZonedDateTime.parse("2022-01-15T04:50:09Z"),
            endDateTimeUtc = ZonedDateTime.parse("2022-01-23T20:29:03Z"),
            isClosed = true,
            isDeleted = false,
            missionSource = MissionSourceEnum.MONITORFISH,
            hasMissionOrder = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false
        )

        val envMissions = listOf(mission1, mission2, mission3)

        val missions = envMissions.map { MissionEntity(envMission = it) }

        logger.info("Found ${missions.size} missions ")

        return missions
    }
}
