package fr.gouv.gmampa.rapportnav.mocks.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import java.time.Instant

object AgentEntityMock {
    fun create(
        id: Int? = null,
        firstName: String = "Robin",
        lastName: String = "Hood",
        disabledAt: Instant? = null
    ) = AgentEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        disabledAt = disabledAt,
        service = ServiceEntityMock.create()
    )
}
