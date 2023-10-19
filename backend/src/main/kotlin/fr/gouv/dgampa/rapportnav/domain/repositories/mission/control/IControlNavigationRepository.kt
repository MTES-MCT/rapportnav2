package fr.gouv.dgampa.rapportnav.domain.repositories.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationModel


interface IControlNavigationRepository {
    fun save(control: ControlNavigation): ControlNavigationModel
}
