package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.v2.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity


class ActionControl(
    var controlAdministrative: ControlAdministrative? = null,
    var controlGensDeMer: ControlGensDeMer? = null,
    var controlSecurity: ControlSecurity? = null,
    var controlNavigation: ControlNavigation? = null
) {

    fun getAllInfractions(): List<Infraction> {
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
