package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.tags.TagEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.themes.ThemeEntity
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
    override val coverMissionZone: Boolean? = null,
    override var formattedControlPlans: List<FormattedEnvActionControlPlan>? = listOf(),
    override var controlsToComplete: List<ControlType>? = listOf(),
    override var availableControlTypesForInfraction: List<ControlType>? = listOf(),
    override val isAdministrativeControl: Boolean? = null,
    override val isComplianceWithWaterRegulationsControl: Boolean? = null,
    override val isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
    override val isSeafarersControl: Boolean? = null,
    override var targets: List<TargetEntity2>? = null,
    override val tags: List<TagEntity>? = listOf(),
    override var themes: List<ThemeEntity>? = listOf(),
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
    isSafetyEquipmentAndStandardsComplianceControl = isSafetyEquipmentAndStandardsComplianceControl,
    targets = targets
),
    BaseMissionEnvAction {

    override fun getActionId(): String {
        return id.toString()
    }

    override fun computeSummaryTags() {
        val nav = this.getNavSummaryTags()
        val env = this.getEnvSummaryTags()
        this.summaryTags = listOf(
            getInfractionTag(nav.withReport + env.withReport),
            getNatinfTag(nav.natInfSize + env.natInfSize)
        )
    }

    private fun getEnvSummaryTags(): SummaryTag {
        val withReport = this.envInfractions?.count { it.infractionType == InfractionTypeEnum.WITH_REPORT } ?: 0
        val natInfSize = this.envInfractions?.sumOf { it.natinf?.size ?: 0 } ?: 0
        return SummaryTag(withReport = withReport, natInfSize = natInfSize)
    }

    override fun computeCompleteness() {
        val sourcesOfMissingDataForStats = mutableListOf<MissionSourceEnum>()
        val rapportNavComplete =
            EntityCompletenessValidator.isCompleteForStats(this)
            && this.isStartDateEndDateOK()
            && this.controlsToComplete.isNullOrEmpty() == true

        val monitorEnvComplete = this.completion === ActionCompletionEnum.COMPLETED
        if (!rapportNavComplete) {
            sourcesOfMissingDataForStats.add(MissionSourceEnum.RAPPORTNAV)
        }
        if (!monitorEnvComplete) {
            sourcesOfMissingDataForStats.add(MissionSourceEnum.MONITORENV)
        }
        this.isCompleteForStats = rapportNavComplete && monitorEnvComplete
        this.sourcesOfMissingDataForStats = sourcesOfMissingDataForStats
        this.computeSummaryTags()
        this.computeControlsToComplete()
        this.computeAvailableControlTypesForInfraction()
        this.computeCompletenessForStats()
    }


    override fun isControlInValid(control: ControlEntity2?): Boolean {
        return control == null || control.amountOfControls == 0
    }

    private fun computeAvailableControlTypesForInfraction() {
        this.availableControlTypesForInfraction =
            this.targets?.flatMap { computeAvailableControlTypesForInfraction(it) }
    }

    private fun computeAvailableControlTypesForInfraction(target: TargetEntity2): List<ControlType> {
        return listOf(
            ControlType.ADMINISTRATIVE.takeIf { isAdministrativeControlOK(target) },
            ControlType.NAVIGATION.takeIf { isNavigationControlOK(target) },
            ControlType.SECURITY.takeIf { isSecurityControlOK(target) },
            ControlType.GENS_DE_MER.takeIf { isGensDeMerControlOK(target) }
        ).mapNotNull { it }
    }

    private fun isAdministrativeControlOK(target: TargetEntity2): Boolean {
        return this.isAdministrativeControl == true || target.getControlByType(ControlType.ADMINISTRATIVE) != null
    }

    private fun isNavigationControlOK(target: TargetEntity2): Boolean {
        return this.isComplianceWithWaterRegulationsControl == true || target.getControlByType(ControlType.NAVIGATION) != null
    }

    private fun isSecurityControlOK(target: TargetEntity2): Boolean {
        return this.isSafetyEquipmentAndStandardsComplianceControl == true || target.getControlByType(ControlType.SECURITY) != null
    }

    private fun isGensDeMerControlOK(target: TargetEntity2): Boolean {
        return this.isSeafarersControl == true || target.getControlByType(ControlType.GENS_DE_MER) != null
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
            coverMissionZone = action.coverMissionZone,
            envInfractions = action.infractions,
            tags = action.tags,
            themes = action.themes,
        )
    }
}

