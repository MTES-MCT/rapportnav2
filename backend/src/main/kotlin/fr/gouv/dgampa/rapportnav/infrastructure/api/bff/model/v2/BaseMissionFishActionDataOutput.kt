package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*

interface BaseMissionFishActionDataOutput {
    val vesselId: Int?
    val vesselName: String?
    val internalReferenceNumber: String?
    val externalReferenceNumber: String?
    val districtCode: String?
    val faoAreas: List<String>?
    val fishActionType: MissionActionType?
    val emitsVms: ControlCheck?
    val emitsAis: ControlCheck?
    val logbookMatchesActivity: ControlCheck?
    val licencesMatchActivity: ControlCheck?
    val speciesWeightControlled: Boolean?
    val speciesSizeControlled: Boolean?
    val separateStowageOfPreservedSpecies: ControlCheck?
    val logbookInfractions: List<LogbookInfraction>?
    val licencesAndLogbookObservations: String?
    val gearInfractions: List<GearInfraction>?
    val speciesInfractions: List<SpeciesInfraction>?
    val speciesObservations: String?
    val seizureAndDiversion: Boolean?
    val otherInfractions: List<OtherInfraction>?
    val numberOfVesselsFlownOver: Int?
    val unitWithoutOmegaGauge: Boolean?
    val controlQualityComments: String?
    val feedbackSheetRequired: Boolean?
    val userTrigram: String?
    val segments: List<FleetSegment>
    val facade: String?
    val longitude: Double?
    val latitude: Double?
    val portLocode: String?
    var portName: String?
    val vesselTargeted: ControlCheck?
    val seizureAndDiversionComments: String?
    val otherComments: String?
    val gearOnboard: List<GearControl>?
    val speciesOnboard: List<SpeciesControl>?
    val isFromPoseidon: Boolean?
    val isDeleted: Boolean?
    val hasSomeGearsSeized: Boolean?
    val hasSomeSpeciesSeized: Boolean?
    val completedBy: String?
    val completion: Completion?
    val isAdministrativeControl: Boolean?
    val isComplianceWithWaterRegulationsControl: Boolean?
    val isSafetyEquipmentAndStandardsComplianceControl: Boolean?
    val isSeafarersControl: Boolean?
    var observationsByUnit: String?
    var speciesQuantitySeized: Int ?
}
