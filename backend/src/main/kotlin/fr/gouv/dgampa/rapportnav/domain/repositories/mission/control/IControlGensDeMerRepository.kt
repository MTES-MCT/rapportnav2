package fr.gouv.dgampa.rapportnav.domain.repositories.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlGensDeMerModel
import java.util.*


interface IControlGensDeMerRepository {
    fun save(control: ControlGensDeMerEntity): ControlGensDeMerModel

    fun existsById(id: UUID): Boolean

    fun findById(id: UUID): Optional<ControlGensDeMerModel>

    fun existsByActionControlId(actionControlId: String): Boolean

    fun findByActionControlId(actionControlId: String): ControlGensDeMerModel

    fun deleteByActionControlId(actionControlId: String)
}
