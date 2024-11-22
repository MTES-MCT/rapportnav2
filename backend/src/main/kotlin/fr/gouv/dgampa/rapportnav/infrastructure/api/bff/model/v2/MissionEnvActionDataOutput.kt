package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTargetTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.InfractionsByVessel
import org.locationtech.jts.geom.Geometry
import java.time.Instant



class MissionEnvActionDataOutput(
    override val startDateTimeUtc: Instant? = null,
    override val endDateTimeUtc: Instant? = null,
    override val completedBy: String? = null,
    override val geom: Geometry? = null,
    override val facade: String? = null,
    override val department: String? = null,
    override val isAdministrativeControl: Boolean? = null,
    override val isComplianceWithWaterRegulationsControl: Boolean? = null,
    override val isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
    override val isSeafarersControl: Boolean? = null,
    override val openBy: String? = null,
    override val observations: String? = null,
    override val observationsByUnit: String? = null,
    override val actionNumberOfControls: Int? = null,
    override val actionTargetType: ActionTargetTypeEnum? = null,
    override val vehicleType: VehicleTypeEnum? = null,
    override val infractions: List<InfractionsByVessel>? = listOf(),
    override val coverMissionZone: Boolean? = null,
    override val controlSecurity: ControlSecurityEntity? = null,
    override val controlGensDeMer: ControlGensDeMerEntity? = null,
    override val controlNavigation: ControlNavigationEntity? = null,
    override val controlAdministrative: ControlAdministrativeEntity? = null,
    override val formattedControlPlans: Any? = null,
    override val availableControlTypesForInfraction: List<ControlType>? = null
) : MissionActionDataOutput(
    startDateTimeUtc = startDateTimeUtc, endDateTimeUtc = endDateTimeUtc, controlSecurity = controlSecurity,
    controlGensDeMer = controlGensDeMer,
    controlNavigation = controlNavigation,
    controlAdministrative = controlAdministrative
),
    BaseMissionEnvActionDataOutput {}











