package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionSurveillanceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionSurveillanceEntity

object ExtendedEnvActionSurveillanceEntityMock {
    fun create(
        action: EnvActionSurveillanceEntity,
        isCompleteForStats: Boolean? = null,
        sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
    ): ExtendedEnvActionSurveillanceEntity {
        return ExtendedEnvActionSurveillanceEntity(
            action = action,
            isCompleteForStats = isCompleteForStats,
            sourcesOfMissingDataForStats = sourcesOfMissingDataForStats,
        )
    }
}
