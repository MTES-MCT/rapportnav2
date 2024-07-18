package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionSurveillanceEntity

object ExtendedEnvActionEntityMock {

    fun create(
        controlAction: ExtendedEnvActionControlEntity? = null,
        surveillanceAction: ExtendedEnvActionSurveillanceEntity? = null
    ): ExtendedEnvActionEntity {
        return ExtendedEnvActionEntity(
            controlAction = controlAction,
            surveillanceAction = surveillanceAction
        );
    }
}
