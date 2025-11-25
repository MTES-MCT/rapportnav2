package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel2
import java.time.Instant

data class AgentEntity2(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val userId: Int? = null,
    val role: AgentRoleEntity? = null,
    val disabledAt: Instant? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val createdBy: Int? = null,
    val service: ServiceEntity
) {
    fun toAgentModel(): AgentModel2 {
        return AgentModel2(
            id = id,
            userId = userId,
            lastName = lastName,
            firstName = firstName,
            disabledAt = disabledAt,
            role = role?.toAgentRoleModel(),
            service = service.toServiceModel()
        )
    }
    companion object {
        fun fromAgentModel(model: AgentModel2): AgentEntity2 {
            return AgentEntity2(
                id = model.id,
                userId = model.userId,
                lastName = model.lastName,
                firstName = model.firstName,
                disabledAt = model.disabledAt,
                createdAt = model.createdAt,
                updatedAt = model.updatedAt,
                createdBy = model.createdBy,
                role = model.role?.let { AgentRoleEntity.fromAgentRoleModel(it) },
                service = model.service.let { ServiceEntity.fromServiceModel(it) },
            )
        }
    }
}
