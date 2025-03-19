package fr.gouv.gmampa.rapportnav.mocks.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import java.util.Date

object AgentEntityMock {
    fun create(
        id: Int? = null,
        firstName: String = "Robin",
        lastName: String = "Hood",
        deletedAt: Date? = null,
        services: MutableSet<ServiceEntity?> = HashSet(),
    ) = AgentEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        deletedAt = deletedAt,
        services = services,
    )
}
