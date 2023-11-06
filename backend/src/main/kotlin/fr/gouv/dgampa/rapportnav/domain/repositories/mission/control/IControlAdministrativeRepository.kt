package fr.gouv.dgampa.rapportnav.domain.repositories.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlAdministrativeModel
import java.util.*


interface IControlAdministrativeRepository {
    fun save(control: ControlAdministrativeEntity): ControlAdministrativeModel

    fun existsById(id: UUID): Boolean

    fun findById(id: UUID): Optional<ControlAdministrativeModel>

    fun existsByActionControlId(actionControlId: String): Boolean

    fun findByActionControlId(actionControlId: String): ControlAdministrativeModel

    fun deleteByActionControlId(actionControlId: String)

}
