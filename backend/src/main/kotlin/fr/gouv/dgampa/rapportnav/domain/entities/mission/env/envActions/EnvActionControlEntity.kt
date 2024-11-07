package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import org.locationtech.jts.geom.Geometry
import java.time.Instant
import java.util.*

data class EnvActionControlEntity(
    override val id: UUID,
    override val actionStartDateTimeUtc: Instant? = null,
    override val actionEndDateTimeUtc: Instant? = null,
    override val completedBy: String? = null,
    override val completion: ActionCompletionEnum? = null,
    override val controlPlans: List<EnvActionControlPlanEntity>? = listOf(),
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
    override val infractions: List<InfractionEntity>? = listOf(),
) : EnvActionEntity(
    id = id,
    actionType = ActionTypeEnum.CONTROL,
)
