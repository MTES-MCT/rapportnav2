package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel2
import org.springframework.data.jpa.repository.JpaRepository

interface IDBAgent2Repository : JpaRepository<AgentModel2, Int> {
    fun findByServiceId(serviceId: Int): List<AgentModel2>
}
