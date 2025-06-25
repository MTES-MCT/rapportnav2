package fr.gouv.gmampa.rapportnav.mocks.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import java.util.*

object MissionCrewEntityMock {
    fun create(
        id: Int? = 1,
        agent: AgentEntity? = AgentEntity(
            id = 1,
            firstName = "",
            lastName = "",
        ),
        comment: String? = null,
        role: AgentRoleEntity? = AgentRoleEntity(
            id = 1,
            title = ""
        ),
        missionId: Int? = null,
        missionIdUUID: UUID? = null,
    ): MissionCrewEntity {
        return MissionCrewEntity(
            id = id,
            agent = agent!!,
            comment = comment,
            role = role,
            missionId = missionId,
            missionIdUUID = missionIdUUID
        )
    }
}
