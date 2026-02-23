package fr.gouv.gmampa.rapportnav.mocks.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import java.time.Instant

object ServiceEntityMock {
    fun create(
        id: Int? = null,
        name: String = "firstService",
        serviceType: ServiceTypeEnum = ServiceTypeEnum.PAM,
        serviceLinked: ServiceEntity? = null,
        controlUnits: List<Int>? = null,
        deletedAt: Instant? = null,
    ) = ServiceEntity(
        id = id,
        name = name,
        serviceType = serviceType,
        serviceLinked = serviceLinked,
        controlUnits = controlUnits,
        deletedAt = deletedAt,
    )
}
