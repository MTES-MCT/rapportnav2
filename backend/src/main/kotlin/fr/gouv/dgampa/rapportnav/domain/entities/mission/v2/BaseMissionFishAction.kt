package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.ControlUnit
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import java.time.Instant
import java.util.*

interface BaseMissionFishAction {
    val id: Int?
    val ownerId: UUID
    val vesselId: Int?
    val vesselName: String?
    val internalReferenceNumber: String?
    val externalReferenceNumber: String?
    val ircs: String?
    val flagState: CountryCode?
    val districtCode: String?
    val faoAreas: List<String>?
    val fishActionType: MissionActionType
    val actionDatetimeUtc: Instant?
    val actionEndDatetimeUtc: Instant?
    val emitsVms: ControlCheck?
    val emitsAis: ControlCheck?
    val vmsEmissionControlBeforeArrival: ControlCheck?
    val portEntranceAndLandingAuthorized: ControlCheck?
    val logbookFilledPriorToControl: ControlCheck?
    val flightGoals: List<FlightGoal>?
    val logbookMatchesActivity: ControlCheck?
    val licencesMatchActivity: ControlCheck?
    val speciesWeightControlled: ControlCheck?
    val speciesSizeControlled: ControlCheck?
    val separateStowageOfPreservedSpecies: ControlCheck?
    val propulsionEnginePowerControl: ControlCheck?
    val fishingLicencesMatchActivity: ControlCheck?
    val stowagePlanPresent: ControlCheck?
    val onboardWeighingPermit: ControlCheck?
    val weighingCertificateAndSystemsValid: ControlCheck?
    val underSizedSeparateStowage: ControlCheck?
    val underSizedSeparateRecording: ControlCheck?
    val minimumConservationReferenceSizeControlled: ControlCheck?
    val cratesWeighingSamplingControl: ControlCheck?
    val approvedWeighingOperatorInformation: ControlCheck?
    val holdControlledAfterUnloading: ControlCheck?
    val catchesWeighedAtLanding: ControlCheck?
    val licencesAndLogbookObservations: String?
    val speciesObservations: String?
    val seizureAndDiversion: Boolean?
    val numberOfVesselsFlownOver: Int?
    val unitWithoutOmegaGauge: Boolean?
    val controlQualityComments: String?
    val feedbackSheetRequired: Boolean?
    val userTrigram: String?
    val segments: List<FleetSegment>?
    val facade: String?
    val longitude: Double?
    val latitude: Double?
    val portLocode: String?
    // This field is only used when fetching missions
    var portName: String?
    val vesselTargeted: ControlCheck?
    val seizureAndDiversionComments: String?
    val otherComments: String?
    val gearOnboard: List<GearControl>?
    val speciesOnboard: List<SpeciesOnboardControl>?
    val discardedSpecies: List<DiscardedSpeciesControl>?
    val isFromPoseidon: Boolean?
    /**
     * This field is only used by the `GetVesselControls` use-case.
     * /!\ Do not use it to get `controlUnits` as the field will be empty be default.
     */
    var controlUnits: List<ControlUnit>?
    val isDeleted: Boolean?
    val hasSomeGearsSeized: Boolean?
    val hasSomeSpeciesSeized: Boolean?
    val completedBy: String?
    val completion: Completion?
    val isLastHaul: Boolean?
    val isINNControl: Boolean?
    val isGangwayDeployed: Boolean?
    var observationsByUnit: String?
    var speciesQuantitySeized: Int ?
    val fishInfractions: List<FishInfraction>
    val sati: SatiEntity?
}
