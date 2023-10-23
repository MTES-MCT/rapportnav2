package fr.gouv.dgampa.rapportnav.domain.repositories.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlSecurityModel
import java.util.*


interface IControlSecurityRepository {
    fun save(control: ControlSecurityEntity): ControlSecurityModel

    fun existsByActionControlId(actionControlId: UUID): Boolean

    fun findByActionControlId(actionControlId: UUID): ControlSecurityModel
}
