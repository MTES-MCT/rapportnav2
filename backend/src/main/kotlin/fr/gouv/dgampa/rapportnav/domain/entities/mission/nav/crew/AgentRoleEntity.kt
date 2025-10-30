package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import java.time.Instant

data class AgentRoleEntity(
    val id: Int? = null,
    val title: String,
    val priority: Int? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val createdBy: Int? = null,
    var deletedAt: Instant? = null
) {
    fun toAgentRoleModel(): AgentRoleModel {
        return AgentRoleModel(
            id = id,
            title = title,
            priority = priority,
            createdBy = createdBy,
            deletedAt = deletedAt
        )
    }

    companion object {
        fun fromAgentRoleModel(model: AgentRoleModel): AgentRoleEntity {
            return AgentRoleEntity(
                id = model.id,
                title = model.title,
                priority = model.priority,
                createdAt = model.createdAt,
                updatedAt = model.updatedAt,
                createdBy = model.createdBy,
                deletedAt = model.deletedAt
            )
        }
    }
}
