package fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorFish.ControlUnit
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorFish.fishActions.*
import java.time.ZonedDateTime

data class FishMissionActionDataOutput(
    val id: Int? = null,
    val vesselId: Int? = null,
    val vesselName: String? = null,
    val internalReferenceNumber: String? = null,
    val externalReferenceNumber: String? = null,
    val ircs: String? = null,
    val flagState: String? = null,
    val districtCode: String? = null,
    val faoAreas: List<String> = listOf(),
    val flightGoals: List<FlightGoal> = listOf(),
    val missionId: Int,
    val actionType: MissionActionType,
    val actionDatetimeUtc: ZonedDateTime,
    val emitsVms: ControlCheck? = null,
    val emitsAis: ControlCheck? = null,
    val logbookMatchesActivity: ControlCheck? = null,
    val licencesMatchActivity: ControlCheck? = null,
    val speciesWeightControlled: Boolean? = null,
    val speciesSizeControlled: Boolean? = null,
    val separateStowageOfPreservedSpecies: ControlCheck? = null,
    val logbookInfractions: List<LogbookInfraction> = listOf(),
    val licencesAndLogbookObservations: String? = null,
    val gearInfractions: List<GearInfraction> = listOf(),
    val speciesInfractions: List<SpeciesInfraction> = listOf(),
    val speciesObservations: String? = null,
    val seizureAndDiversion: Boolean? = null,
    val otherInfractions: List<OtherInfraction> = listOf(),
    val numberOfVesselsFlownOver: Int? = null,
    val unitWithoutOmegaGauge: Boolean? = null,
    val controlQualityComments: String? = null,
    val feedbackSheetRequired: Boolean? = null,
    val segments: List<FleetSegment> = listOf(),
    val facade: String? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val portLocode: String? = null,
    val portName: String? = null,
    val seizureAndDiversionComments: String? = null,
    val otherComments: String? = null,
    val gearOnboard: List<GearControl> = listOf(),
    val speciesOnboard: List<SpeciesControl> = listOf(),
    val controlUnits: List<ControlUnit> = listOf(),
    val userTrigram: String? = null,
    val vesselTargeted: ControlCheck? = null,
    val hasSomeGearsSeized: Boolean,
    val hasSomeSpeciesSeized: Boolean,
) {
    companion object {
        fun fromMissionAction(missionAction: MissionAction) = FishMissionActionDataOutput(
            id = missionAction.id,
            vesselId = missionAction.vesselId,
            vesselName = missionAction.vesselName,
            internalReferenceNumber = missionAction.internalReferenceNumber,
            externalReferenceNumber = missionAction.externalReferenceNumber,
            ircs = missionAction.ircs,
            flagState = missionAction.flagState,
            districtCode = missionAction.districtCode,
            faoAreas = missionAction.faoAreas,
            flightGoals = missionAction.flightGoals,
            missionId = missionAction.missionId,
            actionType = missionAction.actionType,
            actionDatetimeUtc = missionAction.actionDatetimeUtc,
            emitsVms = missionAction.emitsVms,
            emitsAis = missionAction.emitsAis,
            logbookMatchesActivity = missionAction.logbookMatchesActivity,
            licencesMatchActivity = missionAction.licencesMatchActivity,
            speciesWeightControlled = missionAction.speciesWeightControlled,
            speciesSizeControlled = missionAction.speciesSizeControlled,
            separateStowageOfPreservedSpecies = missionAction.separateStowageOfPreservedSpecies,
            logbookInfractions = missionAction.logbookInfractions,
            licencesAndLogbookObservations = missionAction.licencesAndLogbookObservations,
            gearInfractions = missionAction.gearInfractions,
            speciesInfractions = missionAction.speciesInfractions,
            speciesObservations = missionAction.speciesObservations,
            seizureAndDiversion = missionAction.seizureAndDiversion,
            otherInfractions = missionAction.otherInfractions,
            numberOfVesselsFlownOver = missionAction.numberOfVesselsFlownOver,
            unitWithoutOmegaGauge = missionAction.unitWithoutOmegaGauge,
            controlQualityComments = missionAction.controlQualityComments,
            feedbackSheetRequired = missionAction.feedbackSheetRequired,
            segments = missionAction.segments,
            facade = missionAction.facade,
            longitude = missionAction.longitude,
            latitude = missionAction.latitude,
            portLocode = missionAction.portLocode,
            portName = missionAction.portName,
            seizureAndDiversionComments = missionAction.seizureAndDiversionComments,
            otherComments = missionAction.otherComments,
            gearOnboard = missionAction.gearOnboard,
            speciesOnboard = missionAction.speciesOnboard,
            controlUnits = missionAction.controlUnits,
            userTrigram = missionAction.userTrigram,
            vesselTargeted = missionAction.vesselTargeted,
            hasSomeGearsSeized = missionAction.hasSomeGearsSeized,
            hasSomeSpeciesSeized = missionAction.hasSomeSpeciesSeized,
        )
    }
}