package fr.gouv.dgampa.rapportnav.domain.repositories.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlSecurityModel
import java.util.*


interface IControlSecurityRepository {
    fun save(control: ControlSecurityEntity): ControlSecurityModel

    fun existsById(id: UUID): Boolean

    fun findById(id: UUID): Optional<ControlSecurityModel>

    fun existsByActionControlId(actionControlId: String): Boolean

    fun findByActionControlId(actionControlId: String): ControlSecurityModel

    fun deleteByActionControlId(actionControlId: String)
}
