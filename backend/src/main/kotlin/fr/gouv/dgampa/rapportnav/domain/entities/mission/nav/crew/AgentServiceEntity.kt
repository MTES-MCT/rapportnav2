package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentServiceModel
import java.time.Instant

data class AgentServiceEntity(
    val id: Int? = null,
    val agent: AgentEntity,
    val role: AgentRoleEntity? = null,
    val disabledAt: Instant? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val createdBy: Int? = null,
) {
    companion object {
        fun fromAgentServiceModel(model: AgentServiceModel): AgentServiceEntity {
            return AgentServiceEntity(
                id = null,
                agent = AgentEntity.fromAgentModel(model.agent),
                role = model.role?.let { AgentRoleEntity.fromAgentRoleModel(it) },
                disabledAt = model.disabledAt,
                createdAt = model.createdAt,
                updatedAt = model.updatedAt,
                createdBy = model.createdBy
            )
        }
    }
}
