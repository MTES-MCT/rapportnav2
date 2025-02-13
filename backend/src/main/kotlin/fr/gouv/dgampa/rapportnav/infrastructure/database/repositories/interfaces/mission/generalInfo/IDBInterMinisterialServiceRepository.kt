package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.InterMinisterialServiceEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.InterMinisterialServiceModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBInterMinisterialServiceRepository: JpaRepository<InterMinisterialServiceModel, UUID> {

    fun save(service: InterMinisterialServiceEntity): InterMinisterialServiceModel
}
