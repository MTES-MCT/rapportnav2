package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBServiceRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JPAServiceRepository(
    private val repo: IDBServiceRepository
) : IServiceRepository {

    override fun existsById(id: Int): Boolean {
        return repo.existsById(id)
    }

    override fun findById(id: Int): Optional<ServiceModel> {
        return repo.findById(id)
    }

    override fun findAll(): List<ServiceModel> {
        return repo.findAll()
    }

    override fun findByControlUnitId(controlUnitIds: List<Int>): List<ServiceModel> {
        return repo.findAll().filter { serviceModel ->
            serviceModel.controlUnits?.any { it in controlUnitIds } == true
        }
    }
}
