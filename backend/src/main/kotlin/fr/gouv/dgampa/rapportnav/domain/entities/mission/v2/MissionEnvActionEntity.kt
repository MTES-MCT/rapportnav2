package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.FormattedEnvActionControlPlan
import org.locationtech.jts.geom.Geometry
import java.time.Instant
import java.util.*

data class MissionEnvActionEntity(
    override val id: UUID,
    override val missionId: Int,
    override val envActionType: ActionTypeEnum,
    override val completedBy: String? = null,
    override val completion: ActionCompletionEnum? = null,
    override val controlPlans: List<EnvActionControlPlanEntity>? = listOf(),
    override val geom: Geometry? = null,
    override val facade: String? = null,
    override val department: String? = null,
    override var startDateTimeUtc: Instant? = null,
    override var endDateTimeUtc: Instant? = null,
    override val openBy: String? = null,
    override val observations: String? = null,
    override val observationsByUnit: String? = null,
    override val actionNumberOfControls: Int? = null,
    override val actionTargetType: ActionTargetTypeEnum? = null,
    override val vehicleType: VehicleTypeEnum? = null,
    override val envInfractions: List<InfractionEntity>? = listOf(),
    override var navInfractions: List<fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity>? = listOf(),
    override val coverMissionZone: Boolean? = null,
    override var formattedControlPlans: List<FormattedEnvActionControlPlan>? = listOf(),
    override var controlsToComplete: List<ControlType>? = listOf(),
    override var availableControlTypesForInfraction: List<ControlType>? = listOf(),
    override val isAdministrativeControl: Boolean? = null,
    override val isComplianceWithWaterRegulationsControl: Boolean? = null,
    override val isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
    override val isSeafarersControl: Boolean? = null,

    ) : MissionActionEntity(
    missionId = missionId,
    isCompleteForStats = false,
    source = MissionSourceEnum.MONITORENV,
    startDateTimeUtc = startDateTimeUtc,
    endDateTimeUtc = endDateTimeUtc,
    actionType = ActionType.valueOf(envActionType.toString()),
    isSeafarersControl = isSeafarersControl,
    isAdministrativeControl = isAdministrativeControl,
    isComplianceWithWaterRegulationsControl = isComplianceWithWaterRegulationsControl,
    isSafetyEquipmentAndStandardsComplianceControl = isSafetyEquipmentAndStandardsComplianceControl
),
    BaseMissionEnvAction {

    override fun getActionId(): String {
        return id.toString()
    }

    override fun computeCompleteness() {
        val sourcesOfMissingDataForStats = mutableListOf<MissionSourceEnum>()
        val rapportNavComplete = EntityCompletenessValidator.isCompleteForStats(this)  && this.isStartDateEndDateOK()
        val monitorFishComplete = this.completion === ActionCompletionEnum.COMPLETED
        if (!rapportNavComplete) {
            sourcesOfMissingDataForStats.add(MissionSourceEnum.RAPPORTNAV)
        }
        if (!monitorFishComplete) {
            sourcesOfMissingDataForStats.add(MissionSourceEnum.MONITORENV)
        }
        this.isCompleteForStats = rapportNavComplete && monitorFishComplete
        this.sourcesOfMissingDataForStats = sourcesOfMissingDataForStats
        this.computeSummaryTags()
        this.computeControlsToComplete()
        this.computeAvailableControlTypesForInfraction()
        this.computeCompletenessForStats()
    }

    private fun computeAvailableControlTypesForInfraction() {
        this.availableControlTypesForInfraction = listOf(
            ControlType.ADMINISTRATIVE.takeIf {
                this.controlAdministrative != null || this.isAdministrativeControl == true
            },
            ControlType.NAVIGATION.takeIf {
                this.controlNavigation != null || this.isComplianceWithWaterRegulationsControl == true
            },
            ControlType.SECURITY.takeIf {
                this.controlSecurity != null || this.isSafetyEquipmentAndStandardsComplianceControl == true
            },
            ControlType.GENS_DE_MER.takeIf {
                this.controlGensDeMer != null || this.isSeafarersControl == true
            }
        ).mapNotNull { it }
    }

    override fun computeControlsToComplete() {
        this.controlsToComplete = listOf(
            ControlType.ADMINISTRATIVE.takeIf {
                this.isAdministrativeControl == true &&
                    (this.controlAdministrative == null || this.controlAdministrative?.amountOfControls == 0)
            },
            ControlType.NAVIGATION.takeIf {
                this.isComplianceWithWaterRegulationsControl == true &&
                    (this.controlNavigation == null || this.controlNavigation?.amountOfControls == 0)
            },
            ControlType.SECURITY.takeIf {
                this.isSafetyEquipmentAndStandardsComplianceControl == true &&
                    (this.controlSecurity == null || this.controlSecurity?.amountOfControls == 0)
            },
            ControlType.GENS_DE_MER.takeIf {
                this.isSeafarersControl == true &&
                    (this.controlGensDeMer == null || this.controlGensDeMer?.amountOfControls == 0)
            }
        ).mapNotNull { it }
    }

    companion object {
        fun fromEnvAction(missionId: Int, action: EnvActionEntity) = MissionEnvActionEntity(
            id = action.id,
            missionId = missionId,
            envActionType = action.actionType,
            startDateTimeUtc = action.actionStartDateTimeUtc,
            endDateTimeUtc = action.actionEndDateTimeUtc,
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
            envInfractions = action.infractions,
            coverMissionZone = action.coverMissionZone
        )
    }
}

