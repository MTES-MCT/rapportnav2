package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.MissionCrewModel
import java.time.Instant
import java.util.*


data class MissionCrewEntity(
    val id: Int? = null,
    val agent: AgentEntity,
    val comment: String? = null,
    val role: AgentRoleEntity? = null,
    val missionId: Int? = null,
    val missionIdUUID: UUID? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
){

    fun toMissionCrewModel(commentDefaultsToString: Boolean? = false): MissionCrewModel {
        return MissionCrewModel(
            id = id,
            missionId = missionId,
            agent = agent.toAgentModel(),
            role = role?.toAgentRoleModel(),
            comment = if (comment == null && commentDefaultsToString == true) "" else comment,
            missionIdUUID = missionIdUUID,
        )
    }

    companion object {
        fun fromMissionCrewModel(crew: MissionCrewModel): MissionCrewEntity {
            return MissionCrewEntity(
                id = crew.id,
                missionId = crew.missionId,
                comment = crew.comment,
                missionIdUUID = crew.missionIdUUID,
                agent = crew.agent.let { AgentEntity.fromAgentModel(it) },
                role = crew.role?.let { AgentRoleEntity.fromAgentRoleModel(it) },
                createdAt = crew.createdAt,
                updatedAt = crew.updatedAt
            )
        }
    }
}
