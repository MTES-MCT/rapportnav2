package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentServiceModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBAgentServiceRepository: JpaRepository<AgentServiceModel, Int> {
    fun findByServiceId(serviceId: Int): List<AgentServiceModel>
}
