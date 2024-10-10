package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity

object ExtendedEnvActionControlEntityMock {
    fun create(
        action: EnvActionControlEntity? = null,
        isCompleteForStats: Boolean? = null,
        sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
        controlAdministrative: ControlAdministrativeEntity? = null,
        controlGensDeMer: ControlGensDeMerEntity? = null,
        controlNavigation: ControlNavigationEntity? = null,
        controlSecurity: ControlSecurityEntity? = null
    ): ExtendedEnvActionControlEntity {
        return ExtendedEnvActionControlEntity(
            action = action,
            isCompleteForStats = isCompleteForStats,
            sourcesOfMissingDataForStats = sourcesOfMissingDataForStats,
            controlAdministrative = controlAdministrative,
            controlGensDeMer = controlGensDeMer,
            controlNavigation = controlNavigation,
            controlSecurity = controlSecurity,
        )
    }
}
