package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import java.time.Instant

data class Service(
    val id: Int?,
    val name: String,
    val controlUnits: List<Int>? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val deletedAt: Instant? = null,
) {
    fun toServiceEntity(): ServiceEntity {
        return ServiceEntity(
            id = id,
            name = name,
            controlUnits = controlUnits,
            deletedAt = deletedAt
        )
    }
    companion object {
        fun fromServiceEntity(service: ServiceEntity): Service {
            return Service(
                id = service.id,
                name = service.name,
                controlUnits = service.controlUnits,
                updatedAt = service.updatedAt,
                createdAt = service.createdAt,
                deletedAt = service.deletedAt
            )
        }
    }
}
