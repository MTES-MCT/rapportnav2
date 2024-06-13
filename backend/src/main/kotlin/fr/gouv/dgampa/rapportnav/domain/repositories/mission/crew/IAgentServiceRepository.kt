package fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentServiceModel

interface IAgentServiceRepository {
    fun findAll(): List<AgentServiceModel>
    fun findByServiceId(serviceId: Int): List<AgentServiceModel>
}
