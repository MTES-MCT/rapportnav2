package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import jakarta.persistence.*
import java.time.Instant
import java.util.Date

@Entity
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
