package fr.gouv.dgampa.rapportnav.infrastructure.bff.model

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.ControlUnit
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import org.locationtech.jts.geom.Geometry
import java.time.ZonedDateTime
import java.util.*


sealed class ActionData
data class EnvActionData(
    val id: UUID?,
    val actionStartDateTimeUtc: ZonedDateTime,
    val actionEndDateTimeUtc: ZonedDateTime?,
    val actionType: ActionTypeEnum,
    val department: String? = null,
    val facade: String? = null,
    val geom: Geometry? = null,
    val themes: List<ThemeEntity>? = listOf(),
    val observations: String? = null,
    val actionNumberOfControls: Int? = null,
    val actionTargetType: ActionTargetTypeEnum? = null,
    val vehicleType: VehicleTypeEnum? = null,
    val infractions: List<InfractionEntity>? = listOf(),
) : ActionData()

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
    val hasSomeSpeciesSeized: Boolean
) : ActionData()



data class NavActionStatus(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val status: ActionStatusType,
    val reason: ActionStatusReason?,
    val isStart: Boolean,
    val observations: String?
) : ActionData()
data class NavActionControl(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime?,
    val controlMethod: ControlMethod?,
    val observations: String? = null,
    val vesselIdentifier: String? = null,
    val vesselType: VesselTypeEnum? = null,
    val vesselSize: VesselSizeEnum? = null,
    val identityControlledPerson: String? = null,
    val controlAdministrative: ControlAdministrative?,
    val controlGensDeMer: ControlGensDeMer?,
    val controlNavigation: ControlNavigation?,
    val controlSecurity: ControlSecurity?
) : ActionData()

//data class NavActionData(
//    val id: UUID?,
//    val actionType: ActionType,
//    val controlAction: ActionControl? = null,
//    val statusAction: ActionStatus? = null
//) : ActionData()
