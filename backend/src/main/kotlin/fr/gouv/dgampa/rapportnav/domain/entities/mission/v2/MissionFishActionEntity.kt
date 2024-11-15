package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.ControlUnit
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator
import java.time.Instant

data class FishInfraction(val natinf: Int?, val infractionType: InfractionType?)

class MissionFishActionEntity(
    override val id: Int?,
    override val missionId: Int,
    override val vesselId: Int?,
    override val vesselName: String?,
    override val internalReferenceNumber: String?,
    override val externalReferenceNumber: String?,
    override val ircs: String?,
    override val flagState: CountryCode,
    override val districtCode: String?,
    override val faoAreas: List<String>,
    override val fishActionType: MissionActionType,
    override val actionDatetimeUtc: Instant,
    override val actionEndDatetimeUtc: Instant?,
    override val emitsVms: ControlCheck?,
    override val emitsAis: ControlCheck?,
    override val flightGoals: List<FlightGoal>,
    override val logbookMatchesActivity: ControlCheck?,
    override val licencesMatchActivity: ControlCheck?,
    override val speciesWeightControlled: Boolean?,
    override val speciesSizeControlled: Boolean?,
    override val separateStowageOfPreservedSpecies: ControlCheck?,
    override val logbookInfractions: List<LogbookInfraction>,
    override val licencesAndLogbookObservations: String?,
    override val gearInfractions: List<GearInfraction>,
    override val speciesInfractions: List<SpeciesInfraction>,
    override val speciesObservations: String?,
    override val seizureAndDiversion: Boolean?,
    override val otherInfractions: List<OtherInfraction>,
    override val numberOfVesselsFlownOver: Int?,
    override val unitWithoutOmegaGauge: Boolean?,
    override val controlQualityComments: String?,
    override val feedbackSheetRequired: Boolean?,
    override val userTrigram: String,
    override val segments: List<FleetSegment>,
    override val facade: String?,
    override val longitude: Double?,
    override val latitude: Double?,
    override val portLocode: String?,
    override var portName: String?,
    override val vesselTargeted: ControlCheck?,
    override val seizureAndDiversionComments: String?,
    override val otherComments: String?,
    override val gearOnboard: List<GearControl>,
    override val speciesOnboard: List<SpeciesControl>,
    override val isFromPoseidon: Boolean,
    override var controlUnits: List<ControlUnit>,
    override val isDeleted: Boolean,
    override val hasSomeGearsSeized: Boolean,
    override val hasSomeSpeciesSeized: Boolean,
    override val completedBy: String?,
    override val completion: Completion,
    override val isAdministrativeControl: Boolean?,
    override val isComplianceWithWaterRegulationsControl: Boolean?,
    override val isSafetyEquipmentAndStandardsComplianceControl: Boolean?,
    override val isSeafarersControl: Boolean?,
    override var observationsByUnit: String?,
    override var speciesQuantitySeized: Int?,
) : MissionActionEntity(
    missionId = missionId,
    actionType = ActionType.CONTROL,
    isCompleteForStats = false,
    endDateTimeUtc = actionEndDatetimeUtc,
    startDateTimeUtc = actionDatetimeUtc,
    source = MissionSourceEnum.MONITORFISH
), BaseMissionFishAction {

    override fun getActionId(): String {
        return id.toString()
    }

    override fun computeSummaryTags() {
        val navInfractions = this.getControlInfractions()
        val fishInfractions: List<FishInfraction> = listOf(
            this.gearInfractions.map { FishInfraction(it.natinf, it.infractionType) },
            this.logbookInfractions.map { FishInfraction(it.natinf, it.infractionType) },
            this.speciesInfractions.map { FishInfraction(it.natinf, it.infractionType) },
            this.otherInfractions.map { FishInfraction(it.natinf, it.infractionType) }
        ).flatten()

        val fishWithReport = fishInfractions.count { it.infractionType == InfractionType.WITH_RECORD }
        val navWithReport = navInfractions.count { it.infractionType == InfractionTypeEnum.WITH_REPORT }

        val infractionTag = getInfractionTag(fishWithReport + navWithReport)
        val navNatinfs = navInfractions.flatMap { it.natinfs ?: emptyList() }
        val fishNatinfs = fishInfractions.map { it.natinf.toString() }
        val withReportNatinf = (navNatinfs + fishNatinfs).count { true }
        val natinfTag = getNatinfTag(withReportNatinf)

        this.summaryTags = listOf(infractionTag, natinfTag)
    }

    override fun computeCompleteness() {
        val sourcesOfMissingDataForStats = mutableListOf<MissionSourceEnum>()
        val rapportNavComplete = EntityCompletenessValidator.isCompleteForStats(this)
        val monitorFishComplete = this.completion === Completion.COMPLETED

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
