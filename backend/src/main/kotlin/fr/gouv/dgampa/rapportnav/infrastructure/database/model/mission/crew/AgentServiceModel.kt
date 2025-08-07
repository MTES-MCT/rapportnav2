package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.Date

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "agent_service")
class AgentServiceModel
    (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(
        name = "id",
        unique = true,
        nullable = false
    ) var id: Int?,

    @Column(name = "service_id", nullable = false) var serviceId: Int?,

    @OneToOne @JoinColumn(name = "agent_id", referencedColumnName = "id") var agent: AgentModel,

    @OneToOne @JoinColumn(
        name = "agent_role_id", referencedColumnName = "id"
    ) var role: AgentRoleModel,

    @Column(name = "disabled_at", nullable = true)
    var disabledAt: Instant? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null
) {

    fun toMissionCrewModel(missionId: Int): MissionCrewModel? {
        return MissionCrewModel(
            id = null, missionId = missionId, agent = agent, role = role, comment = ""
        );
    }

    fun toAgentServiceEntity(): AgentServiceEntity? {
        return AgentServiceEntity(
            id = null, agent = agent.toAgentEntity(), role = role?.toAgentRoleEntity(), disabledAt = disabledAt
        );
    }
}
