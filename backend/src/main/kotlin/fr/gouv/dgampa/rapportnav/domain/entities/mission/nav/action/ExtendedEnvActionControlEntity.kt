package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.config.DependentFieldValue
import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator

data class ExtendedEnvActionControlEntity(
    val action: EnvActionControlEntity? = null,

    var isCompleteForStats: Boolean? = null,
    var sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "action.isAdministrativeControl",
                value = arrayOf("true")
            ),
        ]
    )
    var controlAdministrative: ControlAdministrativeEntity? = null,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "action.isSeafarersControl",
                value = arrayOf("true")
            ),
        ]
    )
    var controlGensDeMer: ControlGensDeMerEntity? = null,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "action.isComplianceWithWaterRegulationsControl",
                value = arrayOf("true")
            ),
        ]
    )
    var controlNavigation: ControlNavigationEntity? = null,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "action.isSafetyEquipmentAndStandardsComplianceControl",
                value = arrayOf("true")
            ),
        ]
    )
    var controlSecurity: ControlSecurityEntity? = null
) {

    init {
        // Completeness for stats being computed at class instantiation
        // but if control related fields are not attached to the action, you will need to call this function again where needed
        this.computeCompleteness()
    }

    fun computeCompleteness() {
        val sourcesOfMissingDataForStats = mutableListOf<MissionSourceEnum>()
        val rapportNavComplete = EntityCompletenessValidator.isCompleteForStats(this)
        val monitorEnvComplete = this.action?.completion === ActionCompletionEnum.COMPLETED

        if (!rapportNavComplete) {
            sourcesOfMissingDataForStats.add(MissionSourceEnum.RAPPORTNAV)
        }
        if (!monitorEnvComplete) {
            sourcesOfMissingDataForStats.add(MissionSourceEnum.MONITORENV)
        }

        this.isCompleteForStats = rapportNavComplete && monitorEnvComplete
        this.sourcesOfMissingDataForStats = sourcesOfMissingDataForStats
    }

    companion object {
        fun fromEnvActionControlEntity(
            action: EnvActionControlEntity,
            controlAdministrative: ControlAdministrativeEntity? = null,
            controlGensDeMer: ControlGensDeMerEntity? = null,
            controlNavigation: ControlNavigationEntity? = null,
            controlSecurity: ControlSecurityEntity? = null
        ): ExtendedEnvActionControlEntity =
            ExtendedEnvActionControlEntity(
                action = action,
                controlAdministrative = controlAdministrative,
                controlGensDeMer = controlGensDeMer,
                controlNavigation = controlNavigation,
                controlSecurity = controlSecurity
            )

    }
}
