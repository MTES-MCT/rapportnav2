package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator
import org.locationtech.jts.geom.Geometry
import java.time.Instant
import java.util.*

data class MissionEnvActionEntity(
    override val id: UUID,
    override val missionId: Int,
    override val envActionType: ActionTypeEnum,
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
    override val coverMissionZone: Boolean? = null,

    ) : MissionActionEntity(
    missionId = missionId,
    isCompleteForStats = false,
    source = MissionSourceEnum.MONITORENV,
    startDateTimeUtc = actionStartDateTimeUtc,
    endDateTimeUtc = actionEndDateTimeUtc,
    actionType = ActionType.valueOf(envActionType.toString()),
),
    BaseMissionEnvAction {

    override fun getActionId(): String {
        return id.toString()
    }

    override fun computeCompleteness() {
        val sourcesOfMissingDataForStats = mutableListOf<MissionSourceEnum>()
        val rapportNavComplete = EntityCompletenessValidator.isCompleteForStats(this)
        val monitorFishComplete = this.completion === ActionCompletionEnum.COMPLETED
        if (!rapportNavComplete) {
            sourcesOfMissingDataForStats.add(MissionSourceEnum.RAPPORTNAV)
        }
        if (!monitorFishComplete) {
            sourcesOfMissingDataForStats.add(MissionSourceEnum.MONITORENV)
        }
        this.isCompleteForStats = rapportNavComplete && monitorFishComplete
        this.sourcesOfMissingDataForStats = sourcesOfMissingDataForStats
        this.computeCompletenessForStats()
    }

    companion object {
        fun fromEnvAction(missionId: Int, action: EnvActionEntity) = MissionEnvActionEntity(
            id = action.id,
            missionId = missionId,
            envActionType = action.actionType,
            actionStartDateTimeUtc = action.actionStartDateTimeUtc,
            actionEndDateTimeUtc = action.actionEndDateTimeUtc,
            completedBy = action.completedBy,
            completion = action.completion,
            controlPlans = action.controlPlans,
            geom = action.geom,
            facade = action.facade,
            department = action.department,
            isAdministrativeControl = action.isAdministrativeControl,
            isComplianceWithWaterRegulationsControl = action.isComplianceWithWaterRegulationsControl,
            isSafetyEquipmentAndStandardsComplianceControl = action.isSafetyEquipmentAndStandardsComplianceControl,
            isSeafarersControl = action.isSeafarersControl,
            openBy = action.openBy,
            observations = action.observations,
            observationsByUnit = action.observationsByUnit,
            actionNumberOfControls = action.actionNumberOfControls,
            actionTargetType = action.actionTargetType,
            vehicleType = action.vehicleType,
            infractions = action.infractions,
            coverMissionZone = action.coverMissionZone
        )
    }
}

