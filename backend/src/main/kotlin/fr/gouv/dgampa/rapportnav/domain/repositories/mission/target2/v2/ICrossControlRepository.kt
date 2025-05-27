package fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.CrossControlModel
import java.util.*

interface ICrossControlRepository {
    fun save(model: CrossControlModel): CrossControlModel

    fun findById(id: UUID): Optional<CrossControlModel>

    fun findByServiceId(serviceId: Int): List<CrossControlModel>

    fun deleteById(id: UUID)
}
