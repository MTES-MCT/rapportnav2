package fr.gouv.gmampa.rapportnav.mocks.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum

object ServiceEntityMock {
    fun create(
        id: Int? = null,
        name: String = "firstService",
        serviceType: ServiceTypeEnum = ServiceTypeEnum.PAM,
        agents: MutableSet<AgentEntity?> = HashSet(),
        serviceLinked: ServiceEntity? = null,
        controlUnits: List<Int>? = null,
    ) = ServiceEntity(
        id = id,
        name = name,
        serviceType = serviceType,
        agents = agents,
        serviceLinked = serviceLinked,
        controlUnits = controlUnits,
    )
}
