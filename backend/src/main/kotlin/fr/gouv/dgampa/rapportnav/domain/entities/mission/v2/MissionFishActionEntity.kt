package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.ControlUnit
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator
import java.time.Instant

data class FishInfraction(val natinf: Int?, val infractionType: InfractionType?)

class MissionFishActionEntity(
    override val id: Int?,
    override val missionId: Int,
    override val vesselId: Int? = null,
    override val vesselName: String? = null,
    override val internalReferenceNumber: String? = null,
    override val externalReferenceNumber: String? = null,
    override val ircs: String? = null,
    override val flagState: CountryCode? = null,
    override val districtCode: String? = null,
    override val faoAreas: List<String>? = null,
    override val fishActionType: MissionActionType,
    override val actionDatetimeUtc: Instant? = null,
    override val actionEndDatetimeUtc: Instant? = null,
    override val emitsVms: ControlCheck? = null,
    override val emitsAis: ControlCheck? = null,
    override val flightGoals: List<FlightGoal>? = listOf(),
    override val logbookMatchesActivity: ControlCheck? = null,
    override val licencesMatchActivity: ControlCheck? = null,
    override val speciesWeightControlled: Boolean? = null,
    override val speciesSizeControlled: Boolean? = null,
    override val separateStowageOfPreservedSpecies: ControlCheck? = null,
    override val logbookInfractions: List<LogbookInfraction>? = listOf(),
    override val licencesAndLogbookObservations: String? = null,
    override val gearInfractions: List<GearInfraction>? = listOf(),
    override val speciesInfractions: List<SpeciesInfraction>? = listOf(),
    override val speciesObservations: String? = null,
    override val seizureAndDiversion: Boolean? = null,
    override val otherInfractions: List<OtherInfraction>? = listOf(),
    override val numberOfVesselsFlownOver: Int? = null,
    override val unitWithoutOmegaGauge: Boolean? = null,
    override val controlQualityComments: String? = null,
    override val feedbackSheetRequired: Boolean? = null,
    override val userTrigram: String? = null,
    override val segments: List<FleetSegment>? = listOf(),
    override val facade: String? = null,
    override val longitude: Double? = null,
    override val latitude: Double? = null,
    override val portLocode: String? = null,
    override var portName: String? = null,
    override val vesselTargeted: ControlCheck? = null,
    override val seizureAndDiversionComments: String? = null,
    override val otherComments: String? = null,
    override val gearOnboard: List<GearControl>? = listOf(),
    override val speciesOnboard: List<SpeciesControl>? = listOf(),
    override val isFromPoseidon: Boolean? = null,
    override var controlUnits: List<ControlUnit>? = listOf(),
    override val isDeleted: Boolean? = null,
    override val hasSomeGearsSeized: Boolean? = null,
    override val hasSomeSpeciesSeized: Boolean? = null,
    override val completedBy: String? = null,
    override val completion: Completion? = null,
    override val isAdministrativeControl: Boolean? = null,
    override val isComplianceWithWaterRegulationsControl: Boolean? = null,
    override val isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
    override val isSeafarersControl: Boolean? = null,
    override var observationsByUnit: String? = null,
    override var speciesQuantitySeized: Int? = null,
    override var targets: List<TargetEntity2>? = null
) : MissionActionEntity(
    missionId = missionId,
    actionType = ActionType.CONTROL,
    isCompleteForStats = false,
    endDateTimeUtc = actionEndDatetimeUtc,
    startDateTimeUtc = actionDatetimeUtc,
    source = MissionSourceEnum.MONITORFISH,
    isSeafarersControl = isSeafarersControl,
    isAdministrativeControl = isAdministrativeControl,
    isComplianceWithWaterRegulationsControl = isComplianceWithWaterRegulationsControl,
    isSafetyEquipmentAndStandardsComplianceControl = isSafetyEquipmentAndStandardsComplianceControl,
    targets = targets
), BaseMissionFishAction {
    override fun getActionId(): String {
        return id.toString()
    }

    override fun computeSummaryTags() {
        val nav = this.getNavSummaryTags()
        val fish = this.getFishSummaryTags()
        this.summaryTags = listOf(
            getInfractionTag(nav.withReport + fish.withReport),
            getNatinfTag(nav.natInfSize + fish.natInfSize)
        )
    }

    private fun getFishSummaryTags(): SummaryTag {
        val fishInfractions: List<FishInfraction> = listOfNotNull(
            this.gearInfractions?.map { FishInfraction(it.natinf, it.infractionType) },
            this.logbookInfractions?.map { FishInfraction(it.natinf, it.infractionType) },
            this.speciesInfractions?.map { FishInfraction(it.natinf, it.infractionType) },
            this.otherInfractions?.map { FishInfraction(it.natinf, it.infractionType) }
        ).flatten()
        val withReport = fishInfractions.count { it.infractionType == InfractionType.WITH_RECORD }
        val natInfSize = fishInfractions.map { it.natinf.toString() }.count { true }
        return SummaryTag(withReport = withReport, natInfSize = natInfSize)
    }

    override fun computeCompleteness() {
        this.computeControlsToComplete()

        val sourcesOfMissingDataForStats = mutableListOf<MissionSourceEnum>()
        // Fish endDateTime is not set in MonitorFish so MonitorFish considers the Action as complete
        // so it has to be set by the units
        val rapportNavComplete =
            EntityCompletenessValidator.isCompleteForStats(this)
                && this.isStartDateEndDateOK()
                && this.controlsToComplete.isNullOrEmpty() == true

        val monitorFishComplete = this.completion == Completion.COMPLETED

        if (!rapportNavComplete) {
            sourcesOfMissingDataForStats.add(MissionSourceEnum.RAPPORTNAV)
        }
        if (!monitorFishComplete) {
            sourcesOfMissingDataForStats.add(MissionSourceEnum.MONITORFISH)
        }
        this.isCompleteForStats = rapportNavComplete && monitorFishComplete
        this.sourcesOfMissingDataForStats = sourcesOfMissingDataForStats
        this.computeSummaryTags()

        this.computeCompletenessForStats()
    }


    override fun isControlInValid(control: ControlEntity2?): Boolean {
        return control?.hasBeenDone == null
    }


    companion object {
        fun fromFishAction(action: FishAction) = MissionFishActionEntity(
            id = action.id,
            missionId = action.missionId,
            fishActionType = action.actionType,
            actionDatetimeUtc = action.actionDatetimeUtc,
            actionEndDatetimeUtc = action.actionEndDatetimeUtc,
            vesselId = action.vesselId,
            vesselName = action.vesselName,
            internalReferenceNumber = action.internalReferenceNumber,
            externalReferenceNumber = action.externalReferenceNumber,
            ircs = action.ircs,
            flagState = action.flagState,
            districtCode = action.districtCode,
            faoAreas = action.faoAreas,
            emitsVms = action.emitsVms,
            emitsAis = action.emitsAis,
            flightGoals = action.flightGoals,
            logbookMatchesActivity = action.logbookMatchesActivity,
            licencesMatchActivity = action.licencesMatchActivity,
            speciesWeightControlled = action.speciesWeightControlled,
            speciesSizeControlled = action.speciesSizeControlled,
            separateStowageOfPreservedSpecies = action.separateStowageOfPreservedSpecies,
            logbookInfractions = action.logbookInfractions,
            licencesAndLogbookObservations = action.licencesAndLogbookObservations,
            gearInfractions = action.gearInfractions,
            speciesInfractions = action.speciesInfractions,
            speciesObservations = action.speciesObservations,
            seizureAndDiversion = action.seizureAndDiversion,
            otherInfractions = action.otherInfractions,
            numberOfVesselsFlownOver = action.numberOfVesselsFlownOver,
            unitWithoutOmegaGauge = action.unitWithoutOmegaGauge,
            controlQualityComments = action.controlQualityComments,
            feedbackSheetRequired = action.feedbackSheetRequired,
            userTrigram = action.userTrigram,
            segments = action.segments,
            facade = action.facade,
            longitude = action.longitude,
            latitude = action.latitude,
            portLocode = action.portLocode,
            // This field is only used when fetching missions
            portName = action.portName,
            vesselTargeted = action.vesselTargeted,
            seizureAndDiversionComments = action.seizureAndDiversionComments,
            otherComments = action.otherComments,
            gearOnboard = action.gearOnboard,
            speciesOnboard = action.speciesOnboard,
            isFromPoseidon = action.isFromPoseidon,
            controlUnits = action.controlUnits,
            isDeleted = action.isDeleted,
            hasSomeGearsSeized = action.hasSomeGearsSeized,
            hasSomeSpeciesSeized = action.hasSomeSpeciesSeized,
            completedBy = action.completedBy,
            completion = action.completion,
            isAdministrativeControl = action.isAdministrativeControl,
            isComplianceWithWaterRegulationsControl = action.isComplianceWithWaterRegulationsControl,
            isSafetyEquipmentAndStandardsComplianceControl = action.isSafetyEquipmentAndStandardsComplianceControl,
            isSeafarersControl = action.isSeafarersControl,
            observationsByUnit = action.observationsByUnit,
            speciesQuantitySeized = action.speciesQuantitySeized
        )
    }
}
