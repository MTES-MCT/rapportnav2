package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository

@UseCase
class GetServiceById(
    private val repo: IServiceRepository,
) {
    fun execute(id: Int?): ServiceEntity? {
        return id?.let {
            val service = repo.findById(id)
            return service.orElse(null)?.let { ServiceEntity.fromServiceModel(it) }
        }
    }
}
