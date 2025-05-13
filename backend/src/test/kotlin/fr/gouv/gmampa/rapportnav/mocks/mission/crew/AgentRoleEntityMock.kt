package fr.gouv.gmampa.rapportnav.mocks.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity

object AgentRoleEntityMock {
    fun create(
        id: Int? = null,
        title: String = "Commandant"
    ) = AgentRoleEntity(
        id = id,
        title = title
    )
}
