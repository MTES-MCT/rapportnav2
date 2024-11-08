package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.ControlUnit
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import java.time.Instant

interface BaseMissionFishAction {
    val id: Int?
    val missionId: Int
    val vesselId: Int?
    val vesselName: String?
    val internalReferenceNumber: String?
    val externalReferenceNumber: String?
    val ircs: String?
    val flagState: CountryCode
    val districtCode: String?
    val faoAreas: List<String>
    val fishActionType: MissionActionType
    val actionDatetimeUtc: Instant
    val actionEndDatetimeUtc: Instant?
    val emitsVms: ControlCheck?
    val emitsAis: ControlCheck?
    val flightGoals: List<FlightGoal>
    val logbookMatchesActivity: ControlCheck?
    val licencesMatchActivity: ControlCheck?
    val speciesWeightControlled: Boolean?
    val speciesSizeControlled: Boolean?
    val separateStowageOfPreservedSpecies: ControlCheck?
    val logbookInfractions: List<LogbookInfraction>
    val licencesAndLogbookObservations: String?
    val gearInfractions: List<GearInfraction>
    val speciesInfractions: List<SpeciesInfraction>
    val speciesObservations: String?
    val seizureAndDiversion: Boolean?
    val otherInfractions: List<OtherInfraction>
    val numberOfVesselsFlownOver: Int?
    val unitWithoutOmegaGauge: Boolean?
    val controlQualityComments: String?
    val feedbackSheetRequired: Boolean?
    val userTrigram: String
    val segments: List<FleetSegment>
    val facade: String?
    val longitude: Double?
    val latitude: Double?
    val portLocode: String?
    // This field is only used when fetching missions
    var portName: String?
    val vesselTargeted: ControlCheck?
    val seizureAndDiversionComments: String?
    val otherComments: String?
    val gearOnboard: List<GearControl>
    val speciesOnboard: List<SpeciesControl>
    val isFromPoseidon: Boolean
    /**
     * This field is only used by the `GetVesselControls` use-case.
     * /!\ Do not use it to get `controlUnits` as the field will be empty be default.
     */
    var controlUnits: List<ControlUnit>
    val isDeleted: Boolean
    val hasSomeGearsSeized: Boolean
    val hasSomeSpeciesSeized: Boolean
    val completedBy: String?
    val completion: Completion
    val isAdministrativeControl: Boolean?
    val isComplianceWithWaterRegulationsControl: Boolean?
    val isSafetyEquipmentAndStandardsComplianceControl: Boolean?
    val isSeafarersControl: Boolean?
    var observationsByUnit: String?
    var speciesQuantitySeized: Int ?
}
