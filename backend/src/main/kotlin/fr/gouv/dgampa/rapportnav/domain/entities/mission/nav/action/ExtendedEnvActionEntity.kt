package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity

data class ExtendedEnvActionEntity(
    val controlAction: ExtendedEnvActionControlEntity? = null,
) {
    companion object {
        fun fromEnvActionEntity(
            action: EnvActionEntity?
        ): ExtendedEnvActionEntity? =
            when (action) {
                is EnvActionControlEntity -> ExtendedEnvActionEntity(
                    controlAction = ExtendedEnvActionControlEntity.fromEnvActionControlEntity(action)
                )

                else -> null
            }

    }
}