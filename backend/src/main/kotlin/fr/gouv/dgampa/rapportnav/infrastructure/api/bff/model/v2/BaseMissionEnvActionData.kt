package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTargetTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import org.locationtech.jts.geom.Geometry

interface BaseMissionEnvActionData {
    val completedBy: String?
    val geom: Geometry?
    val facade: String?
    val department: String?
    val isAdministrativeControl: Boolean?
    val isComplianceWithWaterRegulationsControl: Boolean?
    val isSafetyEquipmentAndStandardsComplianceControl: Boolean?
    val isSeafarersControl: Boolean?
    val openBy: String?
    val observations: String?
    val observationsByUnit: String?
    val actionNumberOfControls: Int?
    val actionTargetType: ActionTargetTypeEnum?
    val vehicleType: VehicleTypeEnum?
    val coverMissionZone: Boolean?
    val formattedControlPlans: Any?
    val availableControlTypesForInfraction: List<ControlType>?
}
