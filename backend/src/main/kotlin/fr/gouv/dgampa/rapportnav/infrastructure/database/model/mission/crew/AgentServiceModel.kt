package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew

import jakarta.persistence.*

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
) {

    fun toMissionCrewModel(missionId: Int): MissionCrewModel? {
        return MissionCrewModel(
            id = null, missionId = missionId, agent = agent, role = role, comment = ""
        );
    }
}
