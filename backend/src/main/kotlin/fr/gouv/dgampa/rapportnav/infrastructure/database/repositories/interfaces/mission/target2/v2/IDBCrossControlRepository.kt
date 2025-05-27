package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.target2.v2

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.CrossControlModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBCrossControlRepository : JpaRepository<CrossControlModel, UUID> {

    override fun findById(id: UUID): Optional< CrossControlModel>

    fun findByServiceId(serviceId: Int): List<CrossControlModel>
}
