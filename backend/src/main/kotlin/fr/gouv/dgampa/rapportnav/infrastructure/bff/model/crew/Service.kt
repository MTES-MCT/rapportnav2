package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity

data class Service(
    val id: Int?,
    val name: String,
) {
    companion object {
        fun fromServiceEntity(service: ServiceEntity): Service {
            return Service(
                id = service.id,
                name = service.name,
            )
        }
    }
}
