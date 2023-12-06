package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBServiceRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JPAServiceRepository (
    private val repo: IDBServiceRepository
): IServiceRepository{

    override fun findAll(): List<ServiceModel> {
        return repo.findAll()
    }
}
