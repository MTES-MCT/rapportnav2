package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.config.DependentFieldValue
import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
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

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["CONTROL"]),
            DependentFieldValue(
                field = "sourcesOfMissingDataForStats",
                value = ["MONITORENV", "MONITORFISH"]
            ),
            DependentFieldValue(
                field = "isAdministrativeControl",
                value = arrayOf("true")
            ),
        ]
    )
    override var controlAdministrative: ControlAdministrativeEntity? = null,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["CONTROL"]),
            DependentFieldValue(
                field = "sourcesOfMissingDataForStats",
                value = ["MONITORENV", "MONITORFISH"]
            ),
            DependentFieldValue(
                field = "isSeafarersControl",
                value = arrayOf("true")
            ),
        ]
    )
    override var controlGensDeMer: ControlGensDeMerEntity? = null,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["CONTROL"]),
            DependentFieldValue(
                field = "sourcesOfMissingDataForStats",
                value = ["MONITORENV", "MONITORFISH"]
            ),
            DependentFieldValue(
                field = "isComplianceWithWaterRegulationsControl",
                value = arrayOf("true")
            ),
        ]
    )
    override var controlNavigation: ControlNavigationEntity? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["CONTROL"]),
            DependentFieldValue(
                field = "sourcesOfMissingDataForStats",
                value = ["MONITORENV", "MONITORFISH"]
            ),
            DependentFieldValue(
                field = "isSafetyEquipmentAndStandardsComplianceControl",
                value = arrayOf("true")
            ),
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

    abstract fun getActionId(): String
    abstract fun computeCompleteness()
}

