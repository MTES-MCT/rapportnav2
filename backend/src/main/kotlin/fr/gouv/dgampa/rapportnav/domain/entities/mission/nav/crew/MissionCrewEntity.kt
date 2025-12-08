package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import java.util.*
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.MissionCrewModel

data class MissionCrewEntity(
    val id: Int? = null,
    val agent: AgentEntity? = null,
    val comment: String? = null,
    val role: AgentRoleEntity? = null,
    val missionId: Int? = null,
    val missionIdUUID: UUID? = null,
    var fullName: String? = null,
    val absences: List<MissionCrewAbsenceEntity>? = null
){

    fun toMissionCrewModel(commentDefaultsToString: Boolean? = false): MissionCrewModel {
        return MissionCrewModel(
            id = id,
            missionId = missionId,
            agent = agent?.toAgentModel(),
            role = role?.toAgentRoleModel(),
            comment = if (comment == null && commentDefaultsToString == true) "" else comment,
            missionIdUUID = missionIdUUID,
            absences = absences?.map { it.toMissionCrewAbsenceModel() }?.toMutableList() ?: mutableListOf(),
            fullName = fullName,
        )
    }

    companion object {
        fun fromMissionCrewModel(crew: MissionCrewModel): MissionCrewEntity {
            return MissionCrewEntity(
                id = crew.id,
                missionId = crew.missionId,
                comment = crew.comment,
                missionIdUUID = crew.missionIdUUID,
                agent = crew.agent?.let { AgentEntity.fromAgentModel(it) },
                role = crew.role?.let { AgentRoleEntity.fromAgentRoleModel(it) },
                absences = crew.absences.map { MissionCrewAbsenceEntity.fromMissionCrewAbsenceModel(it) },
                fullName = crew.fullName,
            )
        }
    }
}



