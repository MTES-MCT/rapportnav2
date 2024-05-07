package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.config.DependentFieldValue
import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.Completion
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator

data class ExtendedFishActionControlEntity(
    val action: MissionAction? = null,

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
        computeCompleteness()
    }

    fun computeCompleteness() {
        val sourcesOfMissingDataForStats = mutableListOf<MissionSourceEnum>()
        val rapportNavComplete = EntityCompletenessValidator.isCompleteForStats(this)
        val monitorFishComplete = this.action?.completion === Completion.COMPLETED

        if (!rapportNavComplete) {
            sourcesOfMissingDataForStats.add(MissionSourceEnum.RAPPORTNAV)
        }
        if (!monitorFishComplete) {
            sourcesOfMissingDataForStats.add(MissionSourceEnum.MONITORFISH)
        }

        this.isCompleteForStats = rapportNavComplete && monitorFishComplete
        this.sourcesOfMissingDataForStats = sourcesOfMissingDataForStats
    }

    companion object {
        fun fromFishMissionAction(
            action: MissionAction,
            controlAdministrative: ControlAdministrativeEntity? = null,
            controlGensDeMer: ControlGensDeMerEntity? = null,
            controlNavigation: ControlNavigationEntity? = null,
            controlSecurity: ControlSecurityEntity? = null
        ): ExtendedFishActionControlEntity =
            ExtendedFishActionControlEntity(
                action = action,
                controlAdministrative = controlAdministrative,
                controlGensDeMer = controlGensDeMer,
                controlNavigation = controlNavigation,
                controlSecurity = controlSecurity
            )

    }
}
