package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import java.time.Instant

data class Service(
    val id: Int?,
    val name: String,
    val serviceType: ServiceTypeEnum,
    val controlUnits: List<Int>? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val deletedAt: Instant? = null,
) {
    fun toServiceEntity(): ServiceEntity {
        return ServiceEntity(
            id = id,
            serviceType = serviceType,
            name = name,
            controlUnits = controlUnits,
            deletedAt = deletedAt
        )
    }
    companion object {
        fun fromServiceEntity(service: ServiceEntity): Service {
            return Service(
                id = service.id,
                serviceType = service.serviceType,
                name = service.name,
                controlUnits = service.controlUnits,
                updatedAt = service.updatedAt,
                createdAt = service.createdAt,
                deletedAt = service.deletedAt
            )
        }
    }
}
