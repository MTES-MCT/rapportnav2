package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import org.slf4j.LoggerFactory

@UseCase
class GetServiceById(private val serviceRepo: IServiceRepository) {

    private val logger = LoggerFactory.getLogger(GetServiceById::class.java)
    fun execute(id: Int?): ServiceEntity? {
        return if (id == null ) {
            null
        } else if (serviceRepo.existsById(id)) {
            val service = serviceRepo.findById(id)
            if (service.isPresent) {
                service.get().toServiceEntity()
            } else {
                null
            }
        } else {
            null
        }
    }
}
