package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlGensDeMerModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBControlGensDeMerRepository: JpaRepository<ControlGensDeMerModel, UUID> {

    override fun existsById(id: UUID): Boolean

    override fun findById(id: UUID): Optional<ControlGensDeMerModel>

    fun existsByActionControlId(actionControlId: String): Boolean

    fun findByActionControlId(actionControlId: String): ControlGensDeMerModel

    fun deleteByActionControlId(actionControlId: String)
}
