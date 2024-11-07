package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import org.locationtech.jts.geom.Geometry
import java.time.Instant
import java.util.*

interface BaseMissionEnvAction {
    val id: UUID
    val missionId: Int
    val actionType: ActionTypeEnum
    val actionStartDateTimeUtc: Instant?
    val actionEndDateTimeUtc: Instant?
    val completedBy: String?
    val completion: ActionCompletionEnum?
    val controlPlans: List<EnvActionControlPlanEntity>?
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
    val infractions: List<InfractionEntity>?
    val coverMissionZone: Boolean?
}
