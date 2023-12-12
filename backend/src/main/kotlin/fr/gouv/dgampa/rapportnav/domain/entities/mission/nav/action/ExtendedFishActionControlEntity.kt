package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity

data class ExtendedFishActionControlEntity(
    val action: MissionAction? = null,
    var controlAdministrative: ControlAdministrativeEntity? = null,
    var controlGensDeMer: ControlGensDeMerEntity? = null,
    var controlNavigation: ControlNavigationEntity? = null,
    var controlSecurity: ControlSecurityEntity? = null
) {
    companion object {
        fun fromFishMissionAction(
            action: MissionAction,
            controlAdministrative: ControlAdministrativeEntity? = null,
            controlGensDeMer: ControlGensDeMerEntity? = null,
            controlNavigation: ControlNavigationEntity? = null,
            controlSecurity: ControlSecurityEntity? = null
        ): ExtendedFishActionControlEntity =
            ExtendedFishActionControlEntity(
                action = action,
                controlAdministrative = controlAdministrative,
                controlGensDeMer = controlGensDeMer,
                controlNavigation = controlNavigation,
                controlSecurity = controlSecurity
            )

    }
}