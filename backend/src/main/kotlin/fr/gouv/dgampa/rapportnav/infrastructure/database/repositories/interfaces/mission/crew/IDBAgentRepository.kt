package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface IDBAgentRepository: JpaRepository<AgentModel, UUID> {
}