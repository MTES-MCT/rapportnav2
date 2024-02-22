package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.ControlUnit
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlSecurity
import java.time.ZonedDateTime

data class FishActionData(
    val id: String?,
    val missionId: Int,
    val actionType: MissionActionType,
    val vesselId: Int?,
    val vesselName: String?,
    val internalReferenceNumber: String?,
    val externalReferenceNumber: String?,
    val ircs: String?,
    val flagState: String?,
    val districtCode: String?,
    val faoAreas: List<String>,
    val actionDatetimeUtc: ZonedDateTime,
    val emitsVms: ControlCheck?,
    val emitsAis: ControlCheck?,
    val flightGoals: List<FlightGoal>,
    val logbookMatchesActivity: ControlCheck?,
    val licencesMatchActivity: ControlCheck?,
    val speciesWeightControlled: Boolean?,
    val speciesSizeControlled: Boolean?,
    val separateStowageOfPreservedSpecies: ControlCheck?,
    val logbookInfractions: List<LogbookInfraction>,
    val licencesAndLogbookObservations: String?,
    val gearInfractions: List<GearInfraction>,
    val speciesInfractions: List<SpeciesInfraction>,
    val speciesObservations: String?,
    val seizureAndDiversion: Boolean?,
    val otherInfractions: List<OtherInfraction>,
    val numberOfVesselsFlownOver: Int?,
    val unitWithoutOmegaGauge: Boolean?,
    val controlQualityComments: String?,
    val feedbackSheetRequired: Boolean?,
    val userTrigram: String?,
    val segments: List<FleetSegment>,
    val facade: String?,
    val longitude: Double?,
    val latitude: Double?,
    val portLocode: String?,
    val portName: String?,
    val vesselTargeted: ControlCheck?,
    val seizureAndDiversionComments: String?,
    val otherComments: String?,
    val gearOnboard: List<GearControl>,
    val speciesOnboard: List<SpeciesControl>,
    val controlUnits: List<ControlUnit>,
    val isDeleted: Boolean,
    val hasSomeGearsSeized: Boolean,
    val hasSomeSpeciesSeized: Boolean,
    val controlAdministrative: ControlAdministrative? = null,
    val controlGensDeMer: ControlGensDeMer? = null,
    val controlNavigation: ControlNavigation? = null,
    val controlSecurity: ControlSecurity? = null,
    val isAdministrativeControl: Boolean? = null,
    val isComplianceWithWaterRegulationsControl: Boolean? = null,
    val isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
    val isSeafarersControl: Boolean? = null,
) : ActionData()
