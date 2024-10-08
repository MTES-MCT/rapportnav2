package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity

object ExtendedFishActionControlEntityMock {
    fun create(
        action: MissionAction? = null,
        isCompleteForStats: Boolean? = null,
        sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
        controlAdministrative: ControlAdministrativeEntity? = null,
        controlGensDeMer: ControlGensDeMerEntity? = null,
        controlNavigation: ControlNavigationEntity? = null,
        controlSecurity: ControlSecurityEntity? = null,
    ): ExtendedFishActionControlEntity {
        return ExtendedFishActionControlEntity(
            action = action,
            isCompleteForStats = isCompleteForStats,
            sourcesOfMissingDataForStats = sourcesOfMissingDataForStats,
            controlAdministrative = controlAdministrative,
            controlGensDeMer = controlGensDeMer,
            controlNavigation = controlNavigation,
            controlSecurity = controlSecurity,
        );
    }
}
