package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity

data class MissionCrewInput(
    val id: Int?,
    val agent: AgentInput,
    val missionId: String,
    val comment: String?,
    val role: AgentRoleInput
) {
    fun toMissionCrewEntity(): MissionCrewEntity {
        return MissionCrewEntity(
            id = id,
            agent = agent.toAgentEntity(),
            missionId = missionId,
            comment = comment,
            role = role.toAgentRoleEntity()
        )
    }
}
