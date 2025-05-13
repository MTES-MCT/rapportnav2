package fr.gouv.gmampa.rapportnav.mocks.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import java.time.Instant
import java.util.Date

object AgentServiceEntityMock {
    fun create(
        id: Int? = null,
        agent: AgentEntity,
        role: AgentRoleEntity,
        disabledAt: Instant? = null
    ) = AgentServiceEntity(
        id = id,
        agent = agent,
        role = role,
        disabledAt = disabledAt,
    )
}
