package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity

data class ExtendedEnvActionControlEntity(
    val action: EnvActionControlEntity? = null,
    var controlAdministrative: ControlAdministrativeEntity? = null,
    var controlGensDeMer: ControlGensDeMerEntity? = null,
    var controlNavigation: ControlNavigationEntity? = null,
    var controlSecurity: ControlSecurityEntity? = null
) {
    companion object {
        fun fromEnvActionControlEntity(
            action: EnvActionControlEntity,
            controlAdministrative: ControlAdministrativeEntity? = null,
            controlGensDeMer: ControlGensDeMerEntity? = null,
            controlNavigation: ControlNavigationEntity? = null,
            controlSecurity: ControlSecurityEntity? = null
        ): ExtendedEnvActionControlEntity =
            ExtendedEnvActionControlEntity(
                action = action,
                controlAdministrative = controlAdministrative,
                controlGensDeMer = controlGensDeMer,
                controlNavigation = controlNavigation,
                controlSecurity = controlSecurity
            )

    }
}