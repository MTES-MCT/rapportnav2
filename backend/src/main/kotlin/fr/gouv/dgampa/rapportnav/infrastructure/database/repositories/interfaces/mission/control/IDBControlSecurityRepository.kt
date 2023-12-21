package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlSecurityModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBControlSecurityRepository : JpaRepository<ControlSecurityModel, UUID> {

    override fun existsById(id: UUID): Boolean

    override fun findById(id: UUID): Optional<ControlSecurityModel>

    fun existsByActionControlId(actionControlId: String): Boolean

    fun findByActionControlId(actionControlId: String): ControlSecurityModel

    fun deleteByActionControlId(actionControlId: String)
}
