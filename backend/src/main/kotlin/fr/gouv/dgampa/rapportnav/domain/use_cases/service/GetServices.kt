package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository

@UseCase
class GetServices(
    private val repo: IServiceRepository,
) {
    fun execute(): List<ServiceEntity> {
        val services = repo.findAll().map{ it.toServiceEntity() }
        return services
    }
}
