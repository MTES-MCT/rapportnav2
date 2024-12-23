package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity

data class ActionControlEntity(
    var controlAdministrative: ControlAdministrativeEntity? = null,
    var controlGensDeMer: ControlGensDeMerEntity? =  null,
    var controlSecurity: ControlSecurityEntity? = null,
    var controlNavigation: ControlNavigationEntity? = null
) {

    fun seInfractions(infractions: List<InfractionEntity>?) {
        controlSecurity?.infractions = infractions?.filter { it.controlId == controlSecurity?.id }
        controlGensDeMer?.infractions = infractions?.filter { it.controlId == controlGensDeMer?.id }
        controlNavigation?.infractions = infractions?.filter { it.controlId == controlNavigation?.id }
        controlAdministrative?.infractions = infractions?.filter { it.controlId == controlAdministrative?.id }
    }
}
