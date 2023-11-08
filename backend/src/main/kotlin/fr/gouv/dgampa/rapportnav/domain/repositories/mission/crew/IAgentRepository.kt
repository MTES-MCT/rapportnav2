package fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel

interface IAgentRepository {

    fun findAll(): List<AgentModel>
    fun findByServiceId(serviceId: Int): List<AgentModel>

    fun findByMissionId(missionId: Int): List<AgentModel>
}
