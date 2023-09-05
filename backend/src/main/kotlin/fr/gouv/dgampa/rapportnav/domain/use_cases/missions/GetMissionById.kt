package fr.gouv.dgampa.rapportnav.domain.use_cases.missions

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.monitorenv.mission.*
import java.time.ZonedDateTime
import java.util.*


@UseCase
class GetMissionById {
    fun execute(missionId: Int): MissionEntity {
        // 1. MonitorEnv - Get main Mission and MissionActions
        // TODO replace with API call
        val controlTheme1 = ThemeEntity(
            theme = "Rejets illicites",
            subThemes = listOf("avitaillement", "soutage"),
            protectedSpecies = listOf("fish1", "fish2")
        )
        val controlTheme2 = ThemeEntity(
        )
        val envMissionActionControl1 = EnvActionControlEntity(
            id = UUID.randomUUID(),
            actionStartDateTimeUtc = ZonedDateTime.parse("2022-02-16T04:50:09Z"),
            actionEndDateTimeUtc = ZonedDateTime.parse("2022-02-16T06:50:09Z"),
            themes = listOf(controlTheme1),
            actionNumberOfControls = 5,
            actionTargetType = ActionTargetTypeEnum.VEHICLE,
            vehicleType = VehicleTypeEnum.VESSEL,
            observations = "blablabla",
            infractions = listOf(
                InfractionEntity(
                    id = "1234",
                    infractionType = InfractionTypeEnum.WITHOUT_REPORT,
                    formalNotice = FormalNoticeEnum.PENDING,
                    toProcess = true
                )
            )
        )
        val envMissionActionControl2 = EnvActionControlEntity(
            id = UUID.randomUUID(),
            actionStartDateTimeUtc = ZonedDateTime.parse("2022-02-19T04:50:09Z"),
            actionEndDateTimeUtc = ZonedDateTime.parse("2022-02-19T06:50:09Z"),
            themes = listOf(controlTheme2),
            actionNumberOfControls = null,
            actionTargetType = null,
            vehicleType = null,
            observations = null
        )
        val envMissionActionSurveillance = EnvActionSurveillanceEntity(
            id = UUID.randomUUID(),
            actionStartDateTimeUtc = ZonedDateTime.parse("2022-02-17T04:50:09Z"),
            actionEndDateTimeUtc = ZonedDateTime.parse("2022-02-17T06:50:09Z")
        )
        val envMissionActionNote = EnvActionNoteEntity(
            id = UUID.randomUUID(),
            actionStartDateTimeUtc = ZonedDateTime.parse("2022-02-18T04:50:09Z"),
            actionEndDateTimeUtc = ZonedDateTime.parse("2022-02-18T06:50:09Z")
        )
        val envMission = MissionEntity(
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
            isGeometryComputedFromControls = false,
            envActions = listOf(envMissionActionControl1,envMissionActionControl2, envMissionActionSurveillance, envMissionActionNote)
        )

        // 2. MonitorFish - Get and add up MissionActions
        // TODO replace with API call

        // 3. RapportNav - add all remaining Mission-related data
        // TODO add logic (crew, missionActions, ...)

        return envMission
//        return missionRepository.findMissionById(missionId)
    }
}
