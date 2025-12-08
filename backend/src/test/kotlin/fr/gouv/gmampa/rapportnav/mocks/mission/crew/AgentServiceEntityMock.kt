package fr.gouv.gmampa.rapportnav.mocks.mission.crew


import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import java.time.Instant

object AgentServiceEntityMock {
    fun create(
        id: Int? = null,
        agent: AgentEntity,
        role: AgentRoleEntity,
        disabledAt: Instant? = null,
        serviceType: ServiceTypeEnum = ServiceTypeEnum.PAM,
    ) = AgentServiceEntity(
        id = id,
        agent = agent,
        role = role,
        disabledAt = disabledAt,
        service = ServiceEntity(id = 1, name = "Service1", serviceType = serviceType)
    )
}
