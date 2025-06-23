package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import java.util.*


data class MissionCrewEntity(
    val id: Int? = null,
    val agent: AgentEntity,
    val comment: String? = null,
    val role: AgentRoleEntity? = null,
    val missionId: Int? = null,
    val missionIdUUID: UUID? = null
)
