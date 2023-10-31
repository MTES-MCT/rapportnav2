package fr.gouv.dgampa.rapportnav.domain.repositories.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationModel


interface IControlNavigationRepository {
    fun save(control: ControlNavigationEntity): ControlNavigationModel

    fun existsByActionControlId(actionControlId: String): Boolean

    fun findByActionControlId(actionControlId: String): ControlNavigationModel
}
