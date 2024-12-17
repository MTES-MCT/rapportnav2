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

    fun computeInfractionControlId() {
        controlSecurity?.infractions?.forEach { it.controlId = controlSecurity?.id }
        controlGensDeMer?.infractions?.forEach { it.controlId = controlGensDeMer?.id }
        controlNavigation?.infractions?.forEach { it.controlId = controlNavigation?.id }
        controlAdministrative?.infractions?.forEach { it.controlId = controlAdministrative?.id }
    }

    fun getControlInfractions(): List<InfractionEntity> {
        val genDeMerInfractions = controlGensDeMer?.infractions ?: listOf()
        val securityInfractions = controlSecurity?.infractions ?: listOf()
        val navigationInfractions = controlNavigation?.infractions ?: listOf()
        val administrativeInfractions = controlAdministrative?.infractions ?: listOf()
        return genDeMerInfractions + securityInfractions + navigationInfractions + administrativeInfractions
    }

    fun processInfractions(infractions: List<InfractionEntity>?) {
        controlSecurity?.infractions = infractions?.filter { it.controlId == controlSecurity?.id }
        controlGensDeMer?.infractions = infractions?.filter { it.controlId == controlGensDeMer?.id }
        controlNavigation?.infractions = infractions?.filter { it.controlId == controlNavigation?.id }
        controlAdministrative?.infractions = infractions?.filter { it.controlId == controlAdministrative?.id }
    }
}
