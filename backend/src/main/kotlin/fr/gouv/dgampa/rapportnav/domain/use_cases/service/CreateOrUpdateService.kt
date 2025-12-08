package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository

@UseCase
class CreateOrUpdateService(
    private val repo: IServiceRepository
) {
    fun execute(entity: ServiceEntity): ServiceEntity {
        return repo.save(entity.toServiceModel()).let { ServiceEntity.fromServiceModel(it) }
    }
}
