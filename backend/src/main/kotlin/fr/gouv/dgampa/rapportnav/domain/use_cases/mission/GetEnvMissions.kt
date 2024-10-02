package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.EnvMission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import java.time.Instant


@UseCase
class GetEnvMissions(
    private val monitorEnvApiRepo: IEnvMissionRepository,
) {
    private val logger = LoggerFactory.getLogger(GetEnvMissions::class.java)

    val mission1 = EnvMission(
        id = 10,
        missionTypes = listOf(MissionTypeEnum.SEA),
        facade = "Outre-Mer",
        observationsCacem = null,
        startDateTimeUtc = Instant.parse("2022-03-15T04:50:09Z"),
//            endDateTimeUtc = Instant.parse("2022-03-23T20:29:03Z"),
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
        startDateTimeUtc = Instant.parse("2022-02-15T04:50:09Z"),
        endDateTimeUtc = Instant.parse("2022-02-23T20:29:03Z"),
        isDeleted = false,
        missionSource = MissionSourceEnum.MONITORENV,
        hasMissionOrder = false,
        isUnderJdp = false,
        isGeometryComputedFromControls = false,
        observationsByUnit = "lorem ipsum",
        openBy = "Dummy service"
    )
    val mission3 = EnvMission(
        id = 12,
        missionTypes = listOf(MissionTypeEnum.SEA),
        facade = "Outre-Mer",
        observationsCacem = null,
        startDateTimeUtc = Instant.parse("2022-01-15T04:50:09Z"),
        endDateTimeUtc = Instant.parse("2022-01-23T20:29:03Z"),
        isDeleted = false,
        missionSource = MissionSourceEnum.MONITORFISH,
        hasMissionOrder = false,
        isUnderJdp = false,
        isGeometryComputedFromControls = false
    )
    val mission4 = EnvMission(
        id = 13,
        missionTypes = listOf(MissionTypeEnum.SEA),
        facade = "Outre-Mer",
        observationsCacem = null,
        startDateTimeUtc = Instant.parse("2024-03-11T07:00:00Z"),
        endDateTimeUtc = Instant.parse("2024-03-22T18:00:00Z"),
        isDeleted = false,
        missionSource = MissionSourceEnum.MONITORFISH,
        hasMissionOrder = false,
        isUnderJdp = false,
        isGeometryComputedFromControls = false
    )

    // far in the future
    val mission5 = EnvMission(
        id = 14,
        missionTypes = listOf(MissionTypeEnum.SEA),
        facade = "Outre-Mer",
        observationsCacem = null,
        startDateTimeUtc = Instant.parse("2026-04-11T07:00:00Z"),
        endDateTimeUtc = Instant.parse("2026-03-22T18:00:00Z"),
        isDeleted = false,
        missionSource = MissionSourceEnum.RAPPORTNAV,
        hasMissionOrder = false,
        isUnderJdp = false,
        isGeometryComputedFromControls = false
    )

    // in progress
    val mission6 = EnvMission(
        id = 15,
        missionTypes = listOf(MissionTypeEnum.SEA),
        facade = "Outre-Mer",
        observationsCacem = null,
        startDateTimeUtc = Instant.parse("2024-04-17T07:00:00Z"),
        endDateTimeUtc = Instant.parse("2026-03-22T18:00:00Z"),
        isDeleted = false,
        missionSource = MissionSourceEnum.MONITORENV,
        hasMissionOrder = false,
        isUnderJdp = false,
        isGeometryComputedFromControls = false
    )

    val mockedMissions = listOf(mission1, mission2, mission3, mission4, mission5, mission6)


    @Cacheable(value = ["envMissions"])
    fun execute(
        startedAfterDateTime: Instant? = null,
        startedBeforeDateTime: Instant? = null,
        pageNumber: Int? = null,
        pageSize: Int? = null,
        controlUnits: List<Int>? = null
    ): List<MissionEntity>? {
        try {
            val envMissions: List<MissionEntity>? =
                monitorEnvApiRepo.findAllMissions(
                    startedAfterDateTime = startedAfterDateTime,
                    startedBeforeDateTime = startedBeforeDateTime,
                    pageNumber = pageNumber,
                    pageSize = pageSize,
                    controlUnits = controlUnits,
                )

            return envMissions
        } catch (e: Exception) {
            logger.error("GetEnvMissions failed loading Missions", e)
            return null
//            return mockedMissions
        }


    }
}
