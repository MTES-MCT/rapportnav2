package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import org.locationtech.jts.geom.Coordinate
import java.time.Instant
import java.util.*

@UseCase
class FakeActionData {
    val controlTheme1 = EnvActionControlPlanEntity(
        themeId = 1,
        subThemeIds = listOf(1),
        tagIds = listOf(1),
    )
    val controlTheme2 = EnvActionControlPlanEntity(
        themeId = 2,
        subThemeIds = listOf(2, 3),
        tagIds = listOf(2),
    )

    fun getFakeEnvSurveillance(): List<EnvActionSurveillanceEntity> {
        val envMissionActionSurveillance1 = EnvActionSurveillanceEntity(
            id = UUID.fromString("226d84bc-e6c5-4d29-8a5f-7db642f99d99"),
            actionStartDateTimeUtc = Instant.parse("2022-02-17T04:50:09Z"),
            actionEndDateTimeUtc = Instant.parse("2022-02-17T06:50:09Z"),
            completion = ActionCompletionEnum.COMPLETED,
            controlPlans = listOf(controlTheme2),
        )

        return listOf(envMissionActionSurveillance1)
    }

    fun getFakeEnvControls(): List<EnvActionControlEntity> {
        val envActionControl1 = EnvActionControlEntity(
            id = UUID.fromString("226d84bc-e6c5-4d29-8a5f-7db642f99d16"),
            actionStartDateTimeUtc = Instant.parse("2023-12-15T10:00:00Z"),
            actionEndDateTimeUtc = null,  // Set to null if not provided in your API response
            completion = ActionCompletionEnum.COMPLETED,
            geom = createMockMultiPoint(listOf(Coordinate(-8.52318191, 48.30305604))),
            actionNumberOfControls = 1,
            actionTargetType = ActionTargetTypeEnum.VEHICLE,
            vehicleType = VehicleTypeEnum.VESSEL,
            observations = "Observation test",
            isSeafarersControl = false,  // Adjust based on your API response
            isAdministrativeControl = false,  // Adjust based on your API response
            controlPlans = null,
            infractions = listOf(
                InfractionEntity(
                    id = "91200795-2823-46b3-8814-a5b3bca29a47",
                    natinf = listOf("0", "118"),
                    registrationNumber = "kjhfieohfgs",
                    companyName = null,
                    relevantCourt = null,
                    infractionType = InfractionTypeEnum.WITHOUT_REPORT,
                    formalNotice = FormalNoticeEnum.NO,
                    toProcess = false,
                    controlledPersonIdentity = "Mr Loutre",
                    vesselType = VesselTypeEnum.COMMERCIAL,
                    vesselSize = VesselSizeEnum.FROM_24_TO_46m
                )
            )
        )

        var envMissionActionControl1 = EnvActionControlEntity(
            id = UUID.fromString("17997de8-b0df-4095-b209-e8758df71b67"),
            actionStartDateTimeUtc = Instant.parse("2022-02-16T04:50:09Z"),
            actionEndDateTimeUtc = Instant.parse("2022-02-16T06:50:09Z"),
            controlPlans = listOf(controlTheme1),
            geom = createMockMultiPoint(listOf(Coordinate(-8.52318191, 48.30305604))),
            actionNumberOfControls = 5,
            actionTargetType = ActionTargetTypeEnum.INDIVIDUAL,
            vehicleType = null,
            observations = "blablabla",
            isSeafarersControl = true,
            isAdministrativeControl = true,
            completion = ActionCompletionEnum.COMPLETED,
            infractions = listOf(
                InfractionEntity(
                    id = "12341",
                    natinf = listOf("2593", "27773"),
                    registrationNumber = "09AB23",
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
            id = UUID.fromString("aa997de8-b0df-3095-b209-e8758df71bbb"),
            actionStartDateTimeUtc = Instant.parse("2022-02-19T04:50:09Z"),
            actionEndDateTimeUtc = Instant.parse("2022-02-19T06:50:09Z"),
            controlPlans = listOf(controlTheme1),
            actionNumberOfControls = 10,
            geom = createMockMultiPoint(listOf(Coordinate(-8.52318191, 48.30305604))),
            actionTargetType = ActionTargetTypeEnum.COMPANY,
            vehicleType = null,
            observations = null,
            observationsByUnit = "dummy",
            isSeafarersControl = true,
            isAdministrativeControl = true,
            completion = ActionCompletionEnum.COMPLETED,
            infractions = listOf(
                InfractionEntity(
                    id = "12342",
                    natinf = listOf("2593", "27773"),
                    registrationNumber = "27773",
                    companyName = "dummy company",
                    relevantCourt = "tribunal",
                    infractionType = InfractionTypeEnum.WITHOUT_REPORT,
                    formalNotice = FormalNoticeEnum.NO,
                    toProcess = true,
                    controlledPersonIdentity = "Jean Robert",
                    vesselType = VesselTypeEnum.COMMERCIAL,
                    vesselSize = VesselSizeEnum.LESS_THAN_12m,
                )
            ),
        )

        val envMissionActionControl3 = EnvActionControlEntity(
            id = UUID.fromString("aa997de8-b0df-4095-b209-e8758df71bbb"),
            actionStartDateTimeUtc = Instant.parse("2022-02-21T04:50:09Z"),
            actionEndDateTimeUtc = Instant.parse("2022-02-21T06:50:09Z"),
            controlPlans = listOf(controlTheme2),
            geom = createMockMultiPoint(listOf(Coordinate(-8.52318191, 48.30305604))),
            actionNumberOfControls = 8,
            actionTargetType = ActionTargetTypeEnum.INDIVIDUAL,
            vehicleType = VehicleTypeEnum.VESSEL,
            observations = null,
            isSafetyEquipmentAndStandardsComplianceControl = true,
            isComplianceWithWaterRegulationsControl = true,
            infractions = listOf(),
            completion = ActionCompletionEnum.COMPLETED,
        )

        return listOf(envActionControl1, envMissionActionControl1, envMissionActionControl2, envMissionActionControl3)
    }

    fun getFakeFishActions(missionId: Int): List<MissionAction> {
        val gearControlInstance = GearControl()
        gearControlInstance.gearCode = "ABC123"
        gearControlInstance.gearName = "Example Gear"
        gearControlInstance.declaredMesh = 10.5
        gearControlInstance.controlledMesh = 9.0
        gearControlInstance.hasUncontrolledMesh = true
        gearControlInstance.gearWasControlled = false
        gearControlInstance.comments = "Some comments"

        val species1 = SpeciesControl()
        species1.underSized = true
        species1.speciesCode = "RJN – Raie fleurie"
        species1.controlledWeight = 329.2
        species1.declaredWeight = 244.0
        val species2 = SpeciesControl()
        species2.underSized = true
        species2.speciesCode = "HKE – Merlu européen"
        species2.declaredWeight = 2964.0

        val missionAction1 = MissionAction(
            id = missionId + 1,
            missionId = missionId,
            completion = Completion.COMPLETED,
            vesselId = 5232556,
            vesselName = "Le Stella",
            latitude = 48.389999,
            longitude = -4.490000,
            facade = "Outre-Mer",
            portLocode = "LR",
            portName = "La Rochelle",
            actionType = MissionActionType.LAND_CONTROL,
            actionDatetimeUtc = Instant.parse("2024-01-09T14:00:00Z"),
            flightGoals = listOf(),
            logbookInfractions = listOf(),
            gearInfractions = listOf(),
            speciesInfractions = listOf(
                SpeciesInfraction(),
                SpeciesInfraction()
            ),
            otherInfractions = listOf(
                OtherInfraction(),
                OtherInfraction(),
            ),
            controlUnits = listOf(),
            isDeleted = false,
            isAdministrativeControl = true,
            isSafetyEquipmentAndStandardsComplianceControl = true,
            emitsVms = ControlCheck.YES,
            emitsAis = ControlCheck.NO,
            logbookMatchesActivity = ControlCheck.NOT_APPLICABLE,
            licencesMatchActivity = ControlCheck.NO,
            licencesAndLogbookObservations = "Lorem ipsum",
            gearOnboard = listOf(gearControlInstance),
            speciesObservations = "Lorem ipsum ",
            speciesSizeControlled = true,
            speciesWeightControlled = false,
            separateStowageOfPreservedSpecies = ControlCheck.NOT_APPLICABLE,
            speciesOnboard = listOf(species1, species2),
            seizureAndDiversion = true,
            seizureAndDiversionComments = "navire dérouté",
            hasSomeGearsSeized = true,
            hasSomeSpeciesSeized = false,
            otherComments = "",
            vesselTargeted = ControlCheck.YES,
            unitWithoutOmegaGauge = true,
            controlQualityComments = "Lorem ipsum bla bla Lorem ipsum bla blaLorem ipsum bla blaLorem ipsum bla bla Lorem ipsum bla bla",
            feedbackSheetRequired = false,
            userTrigram = "LKH",
            faoAreas = listOf("38.1", "38.2", "38.3", "38.4", "38.5"),
            segments = listOf(
                FleetSegment("MED01", "Chaluts de fond"),
                FleetSegment("MED02", "Chaluts pélagique"),
                FleetSegment("MED03", "Chaluts de bord")
            ),
            flagState = CountryCode.FR,
            isFromPoseidon = false,

            )
        val missionAction2 = MissionAction(
            id = missionId + 2,
            missionId = missionId,
            completion = Completion.TO_COMPLETE,
            vesselId = 5232556,
            vesselName = "Le POILLET",
            latitude = 48.389999,
            longitude = -4.490000,
            facade = "Outre-Mer",
            actionType = MissionActionType.SEA_CONTROL,
            actionDatetimeUtc = Instant.parse("2024-01-09T11:00:00Z"),
            flightGoals = listOf(),
            logbookInfractions = listOf(
                LogbookInfraction(infractionType = InfractionType.WITHOUT_RECORD, natinf = 2212),
                LogbookInfraction(infractionType = InfractionType.WITH_RECORD)
            ),
            gearInfractions = listOf(
                GearInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 943),
                GearInfraction(infractionType = InfractionType.PENDING),
            ),
            speciesInfractions = listOf(),
            otherInfractions = listOf(),
            controlUnits = listOf(),
            isDeleted = false,
            isAdministrativeControl = true,
            isSafetyEquipmentAndStandardsComplianceControl = true,
            emitsVms = ControlCheck.YES,
            emitsAis = ControlCheck.NO,
            logbookMatchesActivity = ControlCheck.NOT_APPLICABLE,
            licencesMatchActivity = ControlCheck.NO,
            licencesAndLogbookObservations = null,
            gearOnboard = listOf(GearControl()),
            speciesObservations = null,
            speciesSizeControlled = false,
            speciesWeightControlled = false,
            separateStowageOfPreservedSpecies = ControlCheck.NOT_APPLICABLE,
            speciesOnboard = listOf(),
            seizureAndDiversion = false,
            seizureAndDiversionComments = null,
            hasSomeGearsSeized = false,
            hasSomeSpeciesSeized = false,
            otherComments = null,
            vesselTargeted = ControlCheck.NOT_APPLICABLE,
            unitWithoutOmegaGauge = false,
            controlQualityComments = "tout est ok",
            feedbackSheetRequired = false,
            userTrigram = "THC",
            faoAreas = listOf("38.1"),
            segments = listOf(FleetSegment("MED01", "Chaluts de fond")),
            isComplianceWithWaterRegulationsControl = true,
            isSeafarersControl = true,
            flagState = CountryCode.FR,
            isFromPoseidon = false,
        )

        val missionAction3 = MissionAction(
            id = missionId + 3,
            missionId = missionId,
            completion = Completion.COMPLETED,
            vesselId = 5232556,
            vesselName = "Le ROSE",
            latitude = 48.389999,
            longitude = -4.490000,
            actionType = MissionActionType.OBSERVATION,
            actionDatetimeUtc = Instant.parse("2024-01-09T12:00:00Z"),
            isDeleted = false,
            isAdministrativeControl = true,
            licencesAndLogbookObservations = null,
            hasSomeGearsSeized = false,
            hasSomeSpeciesSeized = false,
            isComplianceWithWaterRegulationsControl = true,
            isSafetyEquipmentAndStandardsComplianceControl = true,
            isSeafarersControl = true,
            flagState = CountryCode.RU,
            isFromPoseidon = false,
            userTrigram = "ACK"
        )
        return listOf(missionAction1, missionAction2, missionAction3)
    }
}
