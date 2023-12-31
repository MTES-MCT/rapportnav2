package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import jakarta.persistence.*

@Entity
@Table(name = "agent_role")
class AgentRoleModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int?,


    @Column(name = "title", unique = true, nullable = false)
    var title: String,
) {

    fun toAgentRoleEntity(): AgentRoleEntity {
        return AgentRoleEntity(
            id = id,
            title = title,
        )
    }

    companion object {
        fun fromAgentRoleEntity(role: AgentRoleEntity): AgentRoleModel {
            return AgentRoleModel(
                id = role.id,
                title = role.title,
            )
        }
    }
}
