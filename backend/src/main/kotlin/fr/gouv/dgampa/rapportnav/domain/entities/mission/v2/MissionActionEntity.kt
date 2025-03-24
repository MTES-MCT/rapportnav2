package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.config.DependentFieldValue
import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.v2.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
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
            DependentFieldValue(
                field = "isAdministrativeControl",
                value = arrayOf("true")
            ),
            DependentFieldValue(
                field = "sourcesOfMissingDataForStats",
                value = ["MONITORENV", "MONITORFISH"]
            ),
            DependentFieldValue(field = "actionType", value = ["CONTROL"])
        ]
    )
    override var controlAdministrative: ControlAdministrativeEntity? = null,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "isSeafarersControl",
                value = arrayOf("true")
            ),
            DependentFieldValue(
                field = "sourcesOfMissingDataForStats",
                value = ["MONITORENV", "MONITORFISH"]
            ),
            DependentFieldValue(field = "actionType", value = ["CONTROL"])
        ]
    )
    override var controlGensDeMer: ControlGensDeMerEntity? = null,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "isComplianceWithWaterRegulationsControl",
                value = arrayOf("true")
            ),
            DependentFieldValue(
                field = "sourcesOfMissingDataForStats",
                value = ["MONITORENV", "MONITORFISH"]
            ),
            DependentFieldValue(field = "actionType", value = ["CONTROL"])
        ]
    )
    override var controlNavigation: ControlNavigationEntity? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "isSafetyEquipmentAndStandardsComplianceControl",
                value = arrayOf("true")
            ),
            DependentFieldValue(
                field = "sourcesOfMissingDataForStats",
                value = ["MONITORENV", "MONITORFISH"]
            ),
            DependentFieldValue(field = "actionType", value = ["CONTROL"])
        ]
    )
    override var controlSecurity: ControlSecurityEntity? = null,

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

    fun getControlInfractions(): List<InfractionEntity> {
        val genDeMerInfractions = controlGensDeMer?.infractions ?: listOf()
        val securityInfractions = controlSecurity?.infractions ?: listOf()
        val navigationInfractions = controlNavigation?.infractions ?: listOf()
        val administrativeInfractions = controlAdministrative?.infractions ?: listOf()
        return genDeMerInfractions + securityInfractions + navigationInfractions + administrativeInfractions
    }

    fun getInfractionTag(withReport: Int): String {
        return if(withReport == 0) "Sans PV" else "$withReport PV"
    }

    fun getNatinfTag(withNatInf: Int) :String{
        return if(withNatInf == 0) "Sans infraction" else "$withNatInf NATINF"
    }

    open fun computeSummaryTags() {
        val infractions = this.getControlInfractions()
        val withReport = infractions.count { it.infractionType == InfractionTypeEnum.WITH_REPORT }
        val infractionTag = getInfractionTag(withReport)
        val natinfs = infractions.flatMap { it.natinfs ?: emptyList() }
        val natinfTag = getNatinfTag(natinfs.size)
        this.summaryTags = listOf(infractionTag, natinfTag)
    }

    open fun computeSummaryTags2() {
        val nbr = this.getNavSummaryTags()
        this.summaryTags = listOf(getInfractionTag(nbr.first), getNatinfTag(nbr.second))
    }

    open fun getNavSummaryTags(): Pair<Int, Int> {
        return this.targets?.map { getSummaryTags(it) }?.fold(Pair(0, 0)) { accumulator, p ->
            accumulator.apply {
                accumulator.first.plus(p.first)
                accumulator.second.plus(p.second)
            }
        }?: Pair(0, 0)
    }

    private fun getSummaryTags(target: TargetEntity2): Pair<Int, Int> {
        val infractions = target.controls?.flatMap { it.infractions ?: emptyList() }
        val natInfSize = infractions?.flatMap { it.natinfs }?.size ?: 0
        val withReport = infractions?.count { it.infractionType == InfractionTypeEnum.WITH_REPORT } ?: 0
        return Pair(withReport, natInfSize)
    }

    fun computeControls(controls: ActionControlEntity?){
        this.controlSecurity = controls?.controlSecurity
        this.controlGensDeMer = controls?.controlGensDeMer
        this.controlNavigation = controls?.controlNavigation
        this.controlAdministrative = controls?.controlAdministrative
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

    fun computeControlsToComplete2() {
        this.controlsToComplete = this.targets?.flatMap { computeControlsToComplete2(it) }
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

    abstract fun computeControlsToComplete()
    abstract fun getActionId(): String
    abstract fun computeCompleteness()
    abstract fun isControlInValid(control: ControlEntity2?): Boolean
}

