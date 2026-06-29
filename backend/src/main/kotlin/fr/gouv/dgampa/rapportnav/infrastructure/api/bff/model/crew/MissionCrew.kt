package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import java.util.UUID
import kotlin.collections.map
import kotlin.collections.orEmpty

data class MissionCrew(
    val id: Int? = null,
    val agent: Agent? = null,
    val missionId: UUID? = null,
    val comment: String? = null,
    val role: AgentRole? = null,
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
                absences = crew.absences.orEmpty().map { MissionCrewAbsence.fromMissionCrewAbsenceEntity(it) },
                fullName = crew.fullName,
            )
        }
    }

    fun toMissionCrewEntity(missionId: UUID? = null): MissionCrewEntity {
        return MissionCrewEntity(
            id = if (id == 0 || id == null) null else id,
            missionId = missionId,
            agent = agent?.toAgentEntity(),
            comment = comment,
            role = role?.toAgentRoleEntity(),
            absences = absences.orEmpty().map { it.toMissionCrewAbsenceEntity() },
            fullName = fullName,
        )
    }
}
