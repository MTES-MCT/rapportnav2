package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBAgentRoleRepository : JpaRepository<AgentRoleModel, Int> {
}
