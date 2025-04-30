package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import jakarta.persistence.*

@Entity
@Table(name = "mission_crew")
class MissionCrewModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int?,

    @ManyToOne
    @JoinColumn(name = "agent_id")
    var agent: AgentModel,

    @Column(name = "mission_id", nullable = true)
    var missionId: Int? = null,

    @Column(name = "comment", nullable = true)
    var comment: String? = null,

    @ManyToOne
    @JoinColumn(name = "agent_role_id", nullable = true)
    var role: AgentRoleModel?,

    @Column(name = "mission_id_string", nullable = true)
    var missionIdString: String? = null,


    ) {

    fun toMissionCrewEntity(commentDefaultsToString: Boolean? = false): MissionCrewEntity {
        return MissionCrewEntity(
            id = id,
            missionId = missionId,
            agent = agent.toAgentEntity(),
            role = role?.toAgentRoleEntity(),
            comment = if (comment == null && commentDefaultsToString == true) "" else comment
        )
    }


    companion object {
        fun fromMissionCrewEntity(crew: MissionCrewEntity): MissionCrewModel {
            return MissionCrewModel(
                id = crew.id,
                missionId = crew.missionId,
                agent = AgentModel.fromAgentEntity(crew.agent),
                role = crew.role?.let { AgentRoleModel.fromAgentRoleEntity(it) },
                comment = crew.comment
            )
        }
    }
}
