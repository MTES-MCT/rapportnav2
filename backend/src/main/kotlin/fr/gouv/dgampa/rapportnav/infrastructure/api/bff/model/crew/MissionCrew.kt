package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import java.util.UUID
import kotlin.collections.map
import kotlin.collections.orEmpty

data class MissionCrew(
    val id: Int? = null,
    val agent: Agent? = null,
    val missionId: Int? = null,
    val comment: String? = null,
    val role: AgentRole? = null,
    val missionIdUUID: UUID? = null,
    val absences: List<MissionCrewAbsence>? = null,
    var fullName: String? = null,
) {

    companion object {
        fun fromMissionCrewEntity(crew: MissionCrewEntity): MissionCrew {
            return MissionCrew(
                id = crew.id,
                missionId = crew.missionId,
                agent = crew.agent?.let { Agent.fromAgentEntity(it) },
                role = crew.role?.let { AgentRole.fromAgentRoleEntity(it) },
                comment = crew.comment,
                missionIdUUID = crew.missionIdUUID,
                absences = crew.absences.orEmpty().map { MissionCrewAbsence.fromMissionCrewAbsenceEntity(it) },
                fullName = crew.fullName,
            )
        }
    }

    fun toMissionCrewEntity(missionIdUUID: UUID?= null, missionId: Int? = null): MissionCrewEntity {
        return MissionCrewEntity(
            id = if (id == 0 || id == null) null else id,
            missionIdUUID = missionIdUUID,
            agent = agent?.toAgentEntity(),
            missionId = missionId,
            comment = comment,
            role = role?.toAgentRoleEntity(),
            absences = absences.orEmpty().map { it.toMissionCrewAbsenceEntity() },
            fullName = fullName,
        )
    }
}
