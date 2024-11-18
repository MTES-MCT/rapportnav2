package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity

data class ActionControlEntity(
    var controlAdministrative: ControlAdministrativeEntity? = null,
    var controlGensDeMer: ControlGensDeMerEntity? =  null,
    var controlSecurity: ControlSecurityEntity? = null,
    var controlNavigation: ControlNavigationEntity? = null
) {
}
