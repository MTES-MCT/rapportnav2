package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity

data class MissionCrew(
    val id: Int?,
    val agent: Agent,
    val missionId: Int,
    val comment: String?,
    val role: AgentRole?
) {

    companion object {
        fun fromMissionCrewEntity(crew: MissionCrewEntity): MissionCrew {
            return MissionCrew(
                id = crew.id,
                missionId = crew.missionId,
                agent = Agent.fromAgentEntity(crew.agent),
                role = crew.role?.let { AgentRole.fromAgentRoleEntity(it) },
                comment = crew.comment
            )
        }
    }

    fun toMissionCrewEntity(): MissionCrewEntity {
        return MissionCrewEntity(
            id = id,
            agent = agent.toAgentEntity(),
            missionId = missionId,
            comment = comment,
            role = role?.toAgentRoleEntity()
        )
    }

}
