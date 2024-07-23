package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*

object ExtendedFishActionEntityMock {
    fun create(
        controlAction: ExtendedFishActionControlEntity? = null
    ): ExtendedFishActionEntity {
        return ExtendedFishActionEntity(
            controlAction = controlAction
        );
    }
}
