package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.AttachControlsToActionControl
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import java.time.ZonedDateTime


@UseCase
class GetFishActionsByMissionId(
    private val attachControlsToActionControl: AttachControlsToActionControl,
    private val fishActionRepo: IFishActionRepository,
) {

    private val logger = LoggerFactory.getLogger(GetFishActionsByMissionId::class.java)

    fun getFakeActions(missionId: Int): List<ExtendedFishActionEntity> {
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
            actionDatetimeUtc = ZonedDateTime.parse("2024-01-09T14:00:00Z"),
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
            )

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
            actionDatetimeUtc = ZonedDateTime.parse("2024-01-09T11:00:00Z"),
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
            controlQualityComments = null,
            feedbackSheetRequired = false,
            userTrigram = null,
            faoAreas = listOf("38.1"),
            segments = listOf(FleetSegment("MED01", "Chaluts de fond")),
            isComplianceWithWaterRegulationsControl = true,
            isSeafarersControl = true
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
            actionDatetimeUtc = ZonedDateTime.parse("2024-01-09T12:00:00Z"),
            isDeleted = false,
            isAdministrativeControl = true,
            licencesAndLogbookObservations = null,
            hasSomeGearsSeized = false,
            hasSomeSpeciesSeized = false,
            isComplianceWithWaterRegulationsControl = true,
            isSafetyEquipmentAndStandardsComplianceControl = true,
            isSeafarersControl = true
        )

//        val actions = listOf(missionAction1, missionAction2, missionAction3).filter {
        val actions = listOf(missionAction1).filter {
            listOf(
                MissionActionType.SEA_CONTROL,
                MissionActionType.LAND_CONTROL
            ).contains(it.actionType)
        }
            .map {
                var action = ExtendedFishActionEntity.fromMissionAction(it)
                action = attachControlsToActionControl.toFishAction(
                    actionId = it.id?.toString(),
                    action = action
                )
                // recompute completeness once controls have been attached
                action.controlAction?.computeCompleteness()
                action
            }

        return actions
    }

    /**
     * This function has two aims:
     * - only keep the allowed control types
     * - attach Nav data to a Fish Action
     */
    private fun filterAndAttachControls(fishActions: List<MissionAction>): List<ExtendedFishActionEntity> {
        return fishActions.filter {
            listOf(
                MissionActionType.SEA_CONTROL,
                MissionActionType.LAND_CONTROL
            ).contains(it.actionType)
        }.map {
            var action = ExtendedFishActionEntity.fromMissionAction(it)
            action = attachControlsToActionControl.toFishAction(
                actionId = it.id?.toString(),
                action = action
            )
            // recompute completeness once controls have been attached
            action.controlAction?.computeCompleteness()
            action
        }
    }

    @Cacheable(value = ["fishActions"], key = "#missionId")
    fun execute(missionId: Int?): List<ExtendedFishActionEntity> {
        if (missionId == null) {
            logger.error("GetFishActionsByMissionId received a null missionId")
            throw IllegalArgumentException("GetFishActionsByMissionId should not receive null missionId")
        }
        return try {
            val fishActions = fishActionRepo.findFishActions(missionId = missionId)
            // Filtering actions based on action type
            val actions = filterAndAttachControls(fishActions)
            actions
        } catch (e: Exception) {
            logger.error("GetFishActionsByMissionId failed loading Actions", e)
            return listOf()
//            return getFakeActions(missionId)
        }


    }
}
