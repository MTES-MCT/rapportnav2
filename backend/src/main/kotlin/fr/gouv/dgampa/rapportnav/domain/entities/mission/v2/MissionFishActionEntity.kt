package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.ControlUnit
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.EndAfterStart
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateThrowsBeforeSave
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateWhenMissionFinished
import fr.gouv.dgampa.rapportnav.domain.validation.WithinMissionDateRange
import java.time.Instant

@EndAfterStart(groups = [ValidateThrowsBeforeSave::class])
@WithinMissionDateRange(groups = [ValidateThrowsBeforeSave::class])
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
    override val licencesAndLogbookObservations: String? = null,
    override val speciesObservations: String? = null,
    override val seizureAndDiversion: Boolean? = null,
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
    override var targets: List<TargetEntity>? = null,
    override val fishInfractions: List<FishInfraction> = listOf(),
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
        val withReport = fishInfractions.count { it.infractionType == InfractionType.WITH_RECORD }
        val natInfSize = fishInfractions.map { it.natinf.toString() }.count { true }
        return SummaryTag(withReport = withReport, natInfSize = natInfSize)
    }

    /**
     * Computes validity for statistics using the new unified validation system.
     * For Fish actions, validates both RAPPORT_NAV and MONITORFISH sources.
     *
     * @param isMissionFinished When true, also checks required fields (ValidateWhenMissionFinished group)
     */
    override fun computeValidity(isMissionFinished: Boolean) {
        this.computeControlsToComplete()

        val sourcesOfMissingData = mutableListOf<MissionSourceEnum>()

        // Run the new unified validation
        val groups = mutableListOf<Class<*>>(ValidateThrowsBeforeSave::class.java)
        if (isMissionFinished) {
            groups.add(ValidateWhenMissionFinished::class.java)
        }

        val rapportNavCompleteness = EntityValidityValidator.validateWithSourceStatic(
            this,
            MissionSourceEnum.RAPPORT_NAV,
            *groups.toTypedArray()
        )

        // Fish endDateTime is not set in MonitorFish so MonitorFish considers the Action as complete
        // so it has to be set by the units
        // Check date validity: both dates must be present and end must be after start
        val datesAreValid = this.startDateTimeUtc != null && this.endDateTimeUtc != null &&
            this.endDateTimeUtc!!.isAfter(this.startDateTimeUtc)

        val rapportNavComplete = rapportNavCompleteness.isComplete
            && datesAreValid
            && this.controlsToComplete.isNullOrEmpty() == true

        val monitorFishComplete = this.completion == Completion.COMPLETED

        if (!rapportNavComplete) {
            sourcesOfMissingData.add(MissionSourceEnum.RAPPORT_NAV)
        }
        if (!monitorFishComplete) {
            sourcesOfMissingData.add(MissionSourceEnum.MONITORFISH)
        }

        this.isCompleteForStats = rapportNavComplete && monitorFishComplete
        this.sourcesOfMissingDataForStats = sourcesOfMissingData

        // Update completenessForStats - status should reflect overall completeness
        val overallStatus = if (rapportNavComplete && monitorFishComplete)
            CompletenessForStatsStatusEnum.COMPLETE
        else CompletenessForStatsStatusEnum.INCOMPLETE

        this.completenessForStats = rapportNavCompleteness.copy(
            status = overallStatus,
            sources = sourcesOfMissingData.ifEmpty { null }
        )

        this.computeSummaryTags()
    }


    override fun isControlInValid(control: ControlEntity?): Boolean {
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
            licencesAndLogbookObservations = action.licencesAndLogbookObservations,
            speciesObservations = action.speciesObservations,
            seizureAndDiversion = action.seizureAndDiversion,
            numberOfVesselsFlownOver = action.numberOfVesselsFlownOver,
            unitWithoutOmegaGauge = action.unitWithoutOmegaGauge,
            controlQualityComments = action.controlQualityComments,
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
            speciesQuantitySeized = action.speciesQuantitySeized,
            fishInfractions = action.infractions,
        )
    }
}
