package fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import java.util.*

interface IServiceRepository {
    fun existsById(id: Int): Boolean
    fun findById(id: Int): Optional<ServiceModel>
    fun findAll(): List<ServiceModel>
}
