package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlAdministrativeModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBControlAdministrativeRepository: JpaRepository<ControlAdministrativeModel, UUID> {

    fun existsByActionControlId(actionControlId: String): Boolean

    fun findByActionControlId(actionControlId: String): ControlAdministrativeModel
}
