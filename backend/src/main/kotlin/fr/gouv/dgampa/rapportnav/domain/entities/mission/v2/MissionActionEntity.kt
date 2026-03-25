package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.config.DependentFieldValue
import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateThrowsBeforeSave
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateWhenMissionFinished
import java.time.Instant


abstract class MissionActionEntity(
    override val missionId: Int,
    override val actionType: ActionType,
    override var isCompleteForStats: Boolean? = null,
    override val source: MissionSourceEnum,
    override var startDateTimeUtc: Instant? = null,
    override var endDateTimeUtc: Instant? = null,
    override var sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
    override var completenessForStats: CompletenessForStatsEntity? = null,
    override var summaryTags: List<String>? = listOf(),
    override var status: ActionStatusType? = null,
    override var controlsToComplete: List<ControlType>? = null,
    override val isAdministrativeControl: Boolean? = null,
    override val isComplianceWithWaterRegulationsControl: Boolean? = null,
    override val isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
    override val isSeafarersControl: Boolean? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["CONTROL"])
        ]
    )
    override var targets: List<TargetEntity>? = null

) : BaseMissionActionEntity {

    /**
     * Unified validation method - validates both validity (dates) and completeness (required fields).
     * Uses Jakarta Bean Validation with validation groups.
     *
     * Required fields are always checked for completeness status.
     *
     * @param isMissionFinished Currently unused, kept for API compatibility
     */
    open fun computeValidityForStats(isMissionFinished: Boolean = false) {
        // Always check both date constraints and required fields for completeness
        val groups = arrayOf(
            ValidateThrowsBeforeSave::class.java,
            ValidateWhenMissionFinished::class.java
        )

        this.completenessForStats = EntityValidityValidator.validateStatic(this, *groups)

        // Update sources of missing data based on completeness
        if (this.completenessForStats?.status == CompletenessForStatsStatusEnum.INCOMPLETE) {
            val sources = listOf(this.source)
            this.completenessForStats = this.completenessForStats?.copy(sources = sources)
            this.sourcesOfMissingDataForStats = sources
        } else {
            this.sourcesOfMissingDataForStats = emptyList()
        }

        // Update isCompleteForStats based on completeness
        this.isCompleteForStats = this.completenessForStats?.isComplete == true && this.controlsToComplete.isNullOrEmpty()
    }

    fun isControl(): Boolean {
        return listOf(
            ActionType.CONTROL,
            ActionType.INQUIRY,
            ActionType.OTHER_CONTROL,
            ActionType.CONTROL_SECTOR,
            ActionType.CONTROL_NAUTICAL_LEISURE,
            ActionType.CONTROL_SLEEPING_FISHING_GEAR
        ).contains(actionType)
    }

    fun getInfractionTag(withReport: Int): String {
        return if(withReport == 0) "Sans PV" else "$withReport PV"
    }

    fun getNatinfTag(withNatInf: Int) :String{
        return if(withNatInf == 0) "Sans infraction" else "$withNatInf NATINF"
    }

    open fun computeSummaryTags() {
        val summaryTag = this.getNavSummaryTags()
        this.summaryTags = listOf(getInfractionTag(summaryTag.withReport), getNatinfTag(summaryTag.natInfSize))
    }

    open fun getNavSummaryTags(): SummaryTag {
        return this.targets?.map { getSummaryTags(it) }?.fold(SummaryTag(0, 0)) { accumulator, p ->
            accumulator.add(withReport = p.withReport, natInfSize = p.natInfSize)
        } ?: SummaryTag(0, 0)
    }

    private fun getSummaryTags(target: TargetEntity): SummaryTag {
        val infractions = target.controls?.flatMap { it.infractions ?: emptyList() } ?: emptyList()
        val natInfSize = infractions.flatMap { it.natinfs }.size
        val withReport = infractions.count { it.infractionType == InfractionTypeEnum.WITH_REPORT }


        return SummaryTag(withReport = withReport, natInfSize = natInfSize)
    }

    fun computeControlsToComplete() {
        this.controlsToComplete = this.targets?.flatMap { computeControlsToComplete2(it) }
    }

    fun getInfractionByControlType(controlType: ControlType): List<InfractionEntity> {
        return this.targets?.flatMap { it.controls?: listOf() }
            ?.filter { it.controlType == controlType }
            ?.flatMap { it.infractions?: listOf() }?: listOf()
    }

    fun getAllInfractions(): List<InfractionEntity> {
        return this.targets?.flatMap { it.controls?: listOf() }
            ?.flatMap { it.infractions?: listOf() }?: listOf()
    }

    private fun computeControlsToComplete2(target: TargetEntity): List<ControlType> {
        return listOf(
            ControlType.ADMINISTRATIVE.takeIf {
                val control = target.getControlByType(ControlType.ADMINISTRATIVE)
                this.isAdministrativeControl == true && this.isControlInValid(control)
            },
            ControlType.GENS_DE_MER.takeIf {
                val control = target.getControlByType(ControlType.GENS_DE_MER)
                this.isSeafarersControl == true && this.isControlInValid(control)
            },
            ControlType.NAVIGATION.takeIf {
                val control = target.getControlByType(ControlType.NAVIGATION)
                this.isComplianceWithWaterRegulationsControl == true && this.isControlInValid(control)
            },
            ControlType.SECURITY.takeIf {
                val control = target.getControlByType(ControlType.SECURITY)
                this.isSafetyEquipmentAndStandardsComplianceControl == true && this.isControlInValid(control)
            }
        ).mapNotNull { it }
    }

    abstract fun getActionId(): String

    /**
     * Computes validity for statistics using the new unified validation system.
     * @param isMissionFinished When true, also checks required fields (ValidateWhenMissionFinished group)
     */
    abstract fun computeValidity(isMissionFinished: Boolean = false)

    abstract fun isControlInValid(control: ControlEntity?): Boolean
}

class SummaryTag(var withReport: Int, var natInfSize: Int) {
    fun add(withReport: Int, natInfSize: Int): SummaryTag {
        this.natInfSize += natInfSize
        this.withReport += withReport
        return this
    }
}

