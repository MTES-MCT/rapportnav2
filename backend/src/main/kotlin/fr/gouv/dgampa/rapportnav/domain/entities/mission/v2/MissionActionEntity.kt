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
) : BaseMissionActionEntity {

    fun computeCompletenessForStats() {
        this.completenessForStats = CompletenessForStatsEntity(
            sources = this.sourcesOfMissingDataForStats,
            status = if (this.isCompleteForStats == true) CompletenessForStatsStatusEnum.COMPLETE else CompletenessForStatsStatusEnum.INCOMPLETE
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

    fun computeControls(controls: ActionControlEntity?){
        this.controlSecurity = controls?.controlSecurity
        this.controlGensDeMer = controls?.controlGensDeMer
        this.controlNavigation = controls?.controlNavigation
        this.controlAdministrative = controls?.controlAdministrative
    }

    fun computeControlsToComplete() {
        this.controlsToComplete = listOf(
            ControlType.ADMINISTRATIVE.takeIf {
                this.isAdministrativeControl == true &&
                    (this.controlAdministrative == null || this.controlAdministrative?.hasBeenDone == null)
            },
            ControlType.NAVIGATION.takeIf {
                this.isComplianceWithWaterRegulationsControl == true &&
                    (this.controlNavigation == null || this.controlNavigation?.hasBeenDone == null)
            },
            ControlType.SECURITY.takeIf {
                this.isSafetyEquipmentAndStandardsComplianceControl == true &&
                    (this.controlSecurity == null || this.controlSecurity?.hasBeenDone == null)
            },
            ControlType.GENS_DE_MER.takeIf {
                this.isSeafarersControl == true &&
                    (this.controlGensDeMer == null || this.controlGensDeMer?.hasBeenDone == null)
            }
        ).mapNotNull { it }
    }
    abstract fun getActionId(): String
    abstract fun computeCompleteness()

}

