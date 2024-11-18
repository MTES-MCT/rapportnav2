package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import java.time.Instant

class MissionFishActionDataOutput(
    override val startDateTimeUtc: Instant,
    override val endDateTimeUtc: Instant? = null,
    override val vesselId: Int? = null,
    override val vesselName: String? = null,
    override val internalReferenceNumber: String? = null,
    override val externalReferenceNumber: String? = null,
    override val districtCode: String? = null,
    override val faoAreas: List<String> = listOf(),
    override val fishActionType: MissionActionType,
    override val emitsVms: ControlCheck? = null,
    override val emitsAis: ControlCheck? = null,
    override val logbookMatchesActivity: ControlCheck? = null,
    override val licencesMatchActivity: ControlCheck? = null,
    override val speciesWeightControlled: Boolean? = null,
    override val speciesSizeControlled: Boolean? = null,
    override val separateStowageOfPreservedSpecies: ControlCheck? = null,
    override val logbookInfractions: List<LogbookInfraction> = listOf(),
    override val licencesAndLogbookObservations: String? = null,
    override val gearInfractions: List<GearInfraction> = listOf(),
    override val speciesInfractions: List<SpeciesInfraction> = listOf(),
    override val speciesObservations: String? = null,
    override val seizureAndDiversion: Boolean? = null,
    override val otherInfractions: List<OtherInfraction> = listOf(),
    override val numberOfVesselsFlownOver: Int? = null,
    override val unitWithoutOmegaGauge: Boolean? = null,
    override val controlQualityComments: String? = null,
    override val feedbackSheetRequired: Boolean? = null,
    override val userTrigram: String,
    override val segments: List<FleetSegment> = listOf(),
    override val facade: String? = null,
    override val longitude: Double? = null,
    override val latitude: Double? = null,
    override val portLocode: String? = null,
    // This field is only used when fetching missions
    override var portName: String? = null,
    override val vesselTargeted: ControlCheck? = null,
    override val seizureAndDiversionComments: String? = null,
    override val otherComments: String? = null,
    override val gearOnboard: List<GearControl> = listOf(),
    override val speciesOnboard: List<SpeciesControl> = listOf(),
    override val isFromPoseidon: Boolean,
    override val isDeleted: Boolean,
    override val hasSomeGearsSeized: Boolean,
    override val hasSomeSpeciesSeized: Boolean,
    override val completedBy: String? = null,
    override val completion: Completion,
    override val isAdministrativeControl: Boolean? = null,
    override val isComplianceWithWaterRegulationsControl: Boolean? = null,
    override val isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
    override val isSeafarersControl: Boolean? = null,
    override var observationsByUnit: String? = null,
    override var speciesQuantitySeized: Int? = null,
    override val controlSecurity: ControlSecurityEntity? = null,
    override val controlGensDeMer: ControlGensDeMerEntity? = null,
    override val controlNavigation: ControlNavigationEntity? = null,
    override val controlAdministrative: ControlAdministrativeEntity? = null,
) : MissionActionDataOutput(
    startDateTimeUtc = startDateTimeUtc,
    endDateTimeUtc = endDateTimeUtc,
    controlSecurity = controlSecurity,
    controlGensDeMer = controlGensDeMer,
    controlNavigation = controlNavigation,
    controlAdministrative = controlAdministrative
), BaseMissionFishActionDataOutput
