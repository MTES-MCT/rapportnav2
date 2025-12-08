package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
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
    val service: ServiceEntity
) {
    fun toAgentServiceModel(): AgentServiceModel {
        return AgentServiceModel(
            id = id,
            disabledAt = disabledAt,
            agent = agent.toAgentModel(),
            role = role?.toAgentRoleModel(),
            service = service.toServiceModel()
        )
    }
    companion object {
        fun fromAgentServiceModel(model: AgentServiceModel): AgentServiceEntity {
            return AgentServiceEntity(
                id = model.id,
                agent = model.agent.let { AgentEntity.fromAgentModel(it) },
                role = model.role?.let { AgentRoleEntity.fromAgentRoleModel(it) },
                service = model.service.let { ServiceEntity.fromServiceModel(it) },
                disabledAt = model.disabledAt,
                createdAt = model.createdAt,
                updatedAt = model.updatedAt,
                createdBy = model.createdBy
            )
        }
    }
}
