package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.ControlUnit
import java.time.Instant

typealias FishAction = MissionAction

data class MissionAction(
    val id: Int? = null,
    val missionId: Int,
    val vesselId: Int? = null,
    val vesselName: String? = null,
    val internalReferenceNumber: String? = null,
    val externalReferenceNumber: String? = null,
    val ircs: String? = null,
    val flagState: CountryCode? = null,
    val districtCode: String? = null,
    val faoAreas: List<String> = listOf(),
    val actionType: MissionActionType,
    val actionDatetimeUtc: Instant,
    val actionEndDatetimeUtc: Instant? = null,
    val emitsVms: ControlCheck? = null,
    val emitsAis: ControlCheck? = null,
    val flightGoals: List<FlightGoal> = listOf(),
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
    val userTrigram: String,
    val segments: List<FleetSegment> = listOf(),
    val facade: String? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val portLocode: String? = null,
    // This field is only used when fetching missions
    var portName: String? = null,
    val vesselTargeted: ControlCheck? = null,
    val seizureAndDiversionComments: String? = null,
    val otherComments: String? = null,
    val gearOnboard: List<GearControl> = listOf(),
    val speciesOnboard: List<SpeciesControl> = listOf(),
    val isFromPoseidon: Boolean,
    /**
     * This field is only used by the `GetVesselControls` use-case.
     * /!\ Do not use it to get `controlUnits` as the field will be empty be default.
     */
    var controlUnits: List<ControlUnit> = listOf(),
    val isDeleted: Boolean,
    val hasSomeGearsSeized: Boolean,
    val hasSomeSpeciesSeized: Boolean,
    val completedBy: String? = null,
    val completion: Completion,
    val isAdministrativeControl: Boolean? = null,
    val isComplianceWithWaterRegulationsControl: Boolean? = null,
    val isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
    val isSeafarersControl: Boolean? = null,
    var observationsByUnit: String? = null,
    var speciesQuantitySeized: Int ? = null
)
