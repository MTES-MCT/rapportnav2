package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBControlNavigationRepository: JpaRepository<ControlNavigationModel, UUID> {

    override fun existsById(id: UUID): Boolean

    override fun findById(id: UUID): Optional<ControlNavigationModel>

    fun existsByActionControlId(actionControlId: String): Boolean

    fun findByActionControlId(actionControlId: String): ControlNavigationModel

    fun deleteByActionControlId(actionControlId: String)
}
