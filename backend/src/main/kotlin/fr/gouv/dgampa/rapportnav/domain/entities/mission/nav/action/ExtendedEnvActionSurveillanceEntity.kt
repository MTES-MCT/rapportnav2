package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionSurveillanceEntity

data class ExtendedEnvActionSurveillanceEntity(
    val action: EnvActionSurveillanceEntity,

) {

    companion object {
        fun fromEnvActionSurveillanceEntity(
            action: EnvActionSurveillanceEntity
        ): ExtendedEnvActionSurveillanceEntity = ExtendedEnvActionSurveillanceEntity(action = action)
    }
}
