package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import java.time.Instant

data class ServiceEntity(
    val id: Int? = null,
    val name: String,
    val serviceType: ServiceTypeEnum,
    val serviceLinked: ServiceEntity? = null,
    val controlUnits: List<Int>? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    var deletedAt: Instant? = null
){
    fun toServiceModel(): ServiceModel {
        return ServiceModel(
            id = id,
            name = name,
            serviceType = serviceType,
            controlUnits = controlUnits,
            deletedAt = deletedAt,
        )
    }

    companion object {
        fun fromServiceModel(model: ServiceModel): ServiceEntity {
            return ServiceEntity(
                id = model.id,
                name = model.name,
                serviceType = model.serviceType,
                controlUnits = model.controlUnits,
                updatedAt = model.updatedAt,
                createdAt = model.createdAt,
                deletedAt = model.deletedAt
            )
        }
    }
}
