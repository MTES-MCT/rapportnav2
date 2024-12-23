package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.v2.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity


class ActionControlInput(
    var controlAdministrative: ControlAdministrativeInput2? = null,
    var controlGensDeMer: ControlGensDeMerInput2? = null,
    var controlSecurity: ControlSecurityInput2? = null,
    var controlNavigation: ControlNavigationInput2? = null
) {

    fun getAllInfractions(): List<InfractionInput2> {
        this.computeInfractionByControlId()
        val genDeMerInfractions = controlGensDeMer?.infractions ?: listOf()
        val securityInfractions = controlSecurity?.infractions ?: listOf()
        val navigationInfractions = controlNavigation?.infractions ?: listOf()
        val administrativeInfractions = controlAdministrative?.infractions ?: listOf()
        return genDeMerInfractions + securityInfractions + navigationInfractions + administrativeInfractions
    }

    fun toActionControlEntity(infractions: List<InfractionEntity>? = null): ActionControlEntity {
        val entity = ActionControlEntity(
            controlSecurity = controlSecurity?.toEntity(),
            controlGensDeMer = controlGensDeMer?.toEntity(),
            controlNavigation = controlNavigation?.toEntity(),
            controlAdministrative = controlAdministrative?.toEntity(),
        )
        if (infractions != null) entity.seInfractions(infractions)
        return entity
    }

    private fun computeInfractionByControlId() {
        controlSecurity?.infractions?.forEach { it.controlId = controlSecurity?.id?.toString() }
        controlGensDeMer?.infractions?.forEach { it.controlId = controlGensDeMer?.id?.toString() }
        controlNavigation?.infractions?.forEach { it.controlId = controlNavigation?.id?.toString() }
        controlAdministrative?.infractions?.forEach { it.controlId = controlAdministrative?.id?.toString() }
    }
}
