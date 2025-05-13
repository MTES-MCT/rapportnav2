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
    override var targets: List<TargetEntity2>? = null

) : BaseMissionActionEntity {

    fun computeCompletenessForStats() {
        val isControlCompleted = this.controlsToComplete == null || this.controlsToComplete?.isEmpty() == true
        val status = if(this.isCompleteForStats == true && isControlCompleted) CompletenessForStatsStatusEnum.COMPLETE else CompletenessForStatsStatusEnum.INCOMPLETE
        this.completenessForStats = CompletenessForStatsEntity(
            sources = this.sourcesOfMissingDataForStats,
            status = status
        )
    }

    fun isControl(): Boolean {
        return actionType == ActionType.CONTROL
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

    private fun getSummaryTags(target: TargetEntity2): SummaryTag {
        val infractions = target.controls?.flatMap { it.infractions ?: emptyList() }
        val natInfSize = infractions?.flatMap { it.natinfs }?.size ?: 0
        val withReport = infractions?.count { it.infractionType == InfractionTypeEnum.WITH_REPORT } ?: 0
        return SummaryTag(withReport = withReport, natInfSize = natInfSize)
    }

    fun isStartDateEndDateOK(): Boolean {
        if(this.endDateTimeUtc  == null ||  this.startDateTimeUtc == null) return false
        try{
            val endDateTime = Instant.parse(endDateTimeUtc.toString())
            val startDateTime = Instant.parse(startDateTimeUtc.toString())
            return endDateTime.isAfter(startDateTime)
        }catch(e:Exception){
            return false
        }
    }

    fun computeControlsToComplete() {
        this.controlsToComplete = this.targets?.flatMap { computeControlsToComplete2(it) }
    }

    fun getInfractionByControlType(controlType: ControlType): List<InfractionEntity2> {
        return this.targets?.flatMap { it.controls?: listOf() }
            ?.filter { it.controlType == controlType }
            ?.flatMap { it.infractions?: listOf() }?: listOf()
    }

    fun getInfractions(): List<InfractionEntity2> {
        return this.targets?.flatMap { it.controls?: listOf() }
            ?.flatMap { it.infractions?: listOf() }?: listOf()
    }

    private fun computeControlsToComplete2(target: TargetEntity2): List<ControlType> {
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
    abstract fun computeCompleteness()
    abstract fun isControlInValid(control: ControlEntity2?): Boolean
}

class SummaryTag(var withReport: Int, var natInfSize: Int) {
    fun add(withReport: Int, natInfSize: Int): SummaryTag {
        this.natInfSize += natInfSize
        this.withReport += withReport
        return this
    }
}

