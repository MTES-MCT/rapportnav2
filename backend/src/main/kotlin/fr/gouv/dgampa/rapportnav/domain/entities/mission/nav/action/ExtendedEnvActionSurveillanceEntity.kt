package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionSurveillanceEntity
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator

data class ExtendedEnvActionSurveillanceEntity(
    val action: EnvActionSurveillanceEntity,

    var isCompleteForStats: Boolean? = null,
    var sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
) {
    init {
        this.computeCompleteness()
    }

    fun computeCompleteness() {
        val sourcesOfMissingDataForStats = mutableListOf<MissionSourceEnum>()
        val rapportNavComplete = EntityCompletenessValidator.isCompleteForStats(this)
        val monitorEnvComplete = this.action.completion === ActionCompletionEnum.COMPLETED

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
        fun fromEnvActionSurveillanceEntity(
            action: EnvActionSurveillanceEntity
        ): ExtendedEnvActionSurveillanceEntity = ExtendedEnvActionSurveillanceEntity(action = action)
    }
}
