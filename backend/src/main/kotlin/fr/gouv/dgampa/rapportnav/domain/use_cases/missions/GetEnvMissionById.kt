package fr.gouv.dgampa.rapportnav.domain.use_cases.missions

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import java.time.ZonedDateTime
import java.util.*


@UseCase
class GetEnvMissionById {
    fun execute(missionId: Int): MissionEntity {
        // 1. MonitorEnv - Get main Mission and MissionActions
        // TODO replace with API call
        val controlTheme1 = ThemeEntity(
            theme = "Rejets illicites",
            subThemes = listOf("avitaillement", "soutage"),
            protectedSpecies = listOf("fish1", "fish2")
        )
        val controlTheme2 = ThemeEntity(
            theme = "Accès zone protégée",
            subThemes = listOf("avitaillement", "soutage"),
            protectedSpecies = listOf("fish1", "fish2")
        )
        var envMissionActionControl1 = EnvActionControlEntity(
            id = UUID.fromString("17997de8-b0df-4095-b209-e8758df71b67"),
            actionStartDateTimeUtc = ZonedDateTime.parse("2022-02-16T04:50:09Z"),
            actionEndDateTimeUtc = ZonedDateTime.parse("2022-02-16T06:50:09Z"),
            themes = listOf(controlTheme1),
            actionNumberOfControls = 5,
            actionTargetType = ActionTargetTypeEnum.VEHICLE,
            vehicleType = VehicleTypeEnum.VESSEL,
            observations = "blablabla",
            isSeafarersControl = true,
            isAdministrativeControl = true,
            infractions = listOf(
                InfractionEntity(
                    id = "1234",
                    natinf = listOf("2593", "27773"),
                    registrationNumber = "1234",
                    companyName = "dummy company",
                    relevantCourt = "tribunal",
                    infractionType = InfractionTypeEnum.WITH_REPORT,
                    formalNotice = FormalNoticeEnum.YES,
                    toProcess = true,
                    controlledPersonIdentity = "Jean Robert",
                    vesselType = VesselTypeEnum.COMMERCIAL,
                    vesselSize = VesselSizeEnum.LESS_THAN_12m,
                )
            ),
        )

        val envMissionActionControl2 = EnvActionControlEntity(
            id = UUID.fromString("aa997de8-b0df-4095-b209-e8758df71baa"),
            actionStartDateTimeUtc = ZonedDateTime.parse("2022-02-19T04:50:09Z"),
            actionEndDateTimeUtc = ZonedDateTime.parse("2022-02-19T06:50:09Z"),
            themes = listOf(controlTheme1),
            actionNumberOfControls = 10,
            actionTargetType = null,
            vehicleType = null,
            observations = null,
            isSeafarersControl = true,
            isAdministrativeControl = true,
            infractions = listOf(
                InfractionEntity(
                    id = "1234",
                    natinf = listOf("2593", "27773"),
                    registrationNumber = "1234",
                    companyName = "dummy company",
                    relevantCourt = "tribunal",
                    infractionType = InfractionTypeEnum.WITH_REPORT,
                    formalNotice = FormalNoticeEnum.YES,
                    toProcess = true,
                    controlledPersonIdentity = "Jean Robert",
                    vesselType = VesselTypeEnum.COMMERCIAL,
                    vesselSize = VesselSizeEnum.LESS_THAN_12m,
                )
            ),
        )

        val envMissionActionControl3 = EnvActionControlEntity(
            id = UUID.fromString("aa997de8-b0df-4095-b209-e8758df71bbb"),
            actionStartDateTimeUtc = ZonedDateTime.parse("2022-02-21T04:50:09Z"),
            actionEndDateTimeUtc = ZonedDateTime.parse("2022-02-21T06:50:09Z"),
            themes = listOf(controlTheme2),
            actionNumberOfControls = 13,
            actionTargetType = ActionTargetTypeEnum.INDIVIDUAL,
            vehicleType = VehicleTypeEnum.VESSEL,
            observations = null,
            isSafetyEquipmentAndStandardsComplianceControl = true,
            isComplianceWithWaterRegulationsControl = true,
            infractions = listOf(
                InfractionEntity(
                    id = "1234",
                    natinf = listOf("2593", "27773"),
                    registrationNumber = "1234",
                    companyName = "dummy company",
                    relevantCourt = "tribunal",
                    infractionType = InfractionTypeEnum.WITH_REPORT,
                    formalNotice = FormalNoticeEnum.YES,
                    toProcess = true,
                    controlledPersonIdentity = "Jean Robert",
                    vesselType = VesselTypeEnum.COMMERCIAL,
                    vesselSize = VesselSizeEnum.LESS_THAN_12m,
                )
            ),
        )
//        val envMissionActionSurveillance = EnvActionSurveillanceEntity(
//            id = UUID.randomUUID(),
//            actionStartDateTimeUtc = ZonedDateTime.parse("2022-02-17T04:50:09Z"),
//            actionEndDateTimeUtc = ZonedDateTime.parse("2022-02-17T06:50:09Z")
//        )
//        val envMissionActionNote = EnvActionNoteEntity(
//            id = UUID.randomUUID(),
//            actionStartDateTimeUtc = ZonedDateTime.parse("2022-02-18T04:50:09Z"),
//            actionEndDateTimeUtc = ZonedDateTime.parse("2022-02-18T06:50:09Z")
//        )
        val envMission = MissionEntity(
            id = 10,
            missionTypes = listOf(MissionTypeEnum.SEA),
            facade = "Outre-Mer",
            observationsCacem = null,
            startDateTimeUtc = ZonedDateTime.parse("2022-02-15T04:50:09Z"),
//            endDateTimeUtc = ZonedDateTime.parse("2022-02-23T20:29:03Z"),
            isClosed = false,
            isDeleted = false,
            missionSource = MissionSourceEnum.RAPPORTNAV,
            hasMissionOrder = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false,
            envActions = listOf(envMissionActionControl1,envMissionActionControl2, envMissionActionControl3)
        )

        // 2. MonitorFish - Get and add up MissionActions
        // TODO replace with API call



        return envMission
//        return missionRepository.findMissionById(missionId)
    }
}
