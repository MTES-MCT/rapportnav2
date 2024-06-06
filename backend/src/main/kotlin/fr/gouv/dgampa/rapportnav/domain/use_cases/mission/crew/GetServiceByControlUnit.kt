package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository

@UseCase
class GetServiceByControlUnit(private val serviceRepository: IServiceRepository) {

    fun execute(controlUnits: List<LegacyControlUnitEntity>? = null): List<ServiceEntity> {
        if (controlUnits == null) return listOf<ServiceEntity>();
        return this.serviceRepository.findByControlUnitId(controlUnits.map { controlUnit -> controlUnit.id })
            .map { it.toServiceEntity() }
            .sortedBy { it.id };
    }
}
