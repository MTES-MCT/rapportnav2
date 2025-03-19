package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity

data class MissionCrew(
    val id: Int? = null,
    val agent: Agent,
    val missionId: Int,
    val comment: String? = null,
    val role: AgentRole? = null
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
            id = if (id == 0 || id == null) null else id,
            agent = agent.toAgentEntity(),
            missionId = missionId,
            comment = comment,
            role = role?.toAgentRoleEntity()
        )
    }

}
