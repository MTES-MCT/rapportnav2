package fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.Agent
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel

interface IAgentRepository {

    fun findAll(): List<AgentModel>
}