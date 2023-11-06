package fr.gouv.dgampa.rapportnav.domain.repositories.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationModel
import java.util.*


interface IControlNavigationRepository {
    fun save(control: ControlNavigationEntity): ControlNavigationModel

    fun existsById(id: UUID): Boolean

    fun findById(id: UUID): Optional<ControlNavigationModel>

    fun existsByActionControlId(actionControlId: String): Boolean

    fun findByActionControlId(actionControlId: String): ControlNavigationModel

    fun deleteByActionControlId(actionControlId: String)
}
