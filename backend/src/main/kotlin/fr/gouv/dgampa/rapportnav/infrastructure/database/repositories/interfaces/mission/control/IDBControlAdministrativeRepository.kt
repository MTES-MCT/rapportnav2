package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlAdministrativeModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBControlAdministrativeRepository: JpaRepository<ControlAdministrativeModel, UUID> {

    override fun existsById(id: UUID): Boolean

    override fun findById(id: UUID): Optional<ControlAdministrativeModel>

    fun existsByActionControlId(actionControlId: String): Boolean

    fun findByActionControlId(actionControlId: String): ControlAdministrativeModel
}
