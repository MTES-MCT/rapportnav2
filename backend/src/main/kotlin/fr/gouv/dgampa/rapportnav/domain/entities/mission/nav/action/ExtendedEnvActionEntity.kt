package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionSurveillanceEntity

data class ExtendedEnvActionEntity(
    val controlAction: ExtendedEnvActionControlEntity? = null,
    val surveillanceAction: ExtendedEnvActionSurveillanceEntity? = null
) {
    companion object {
        fun fromEnvActionEntity(
            action: EnvActionEntity?
        ): ExtendedEnvActionEntity? =
            when (action) {
                is EnvActionControlEntity -> ExtendedEnvActionEntity(
                    controlAction = ExtendedEnvActionControlEntity.fromEnvActionControlEntity(action)
                )
                is EnvActionSurveillanceEntity -> ExtendedEnvActionEntity(
                    surveillanceAction = ExtendedEnvActionSurveillanceEntity.fromEnvActionSurveillanceEntity(action)
                )

                else -> null
            }

    }
}
