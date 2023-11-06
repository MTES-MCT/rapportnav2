package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionControlRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlNavigationRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Repository
class JPAControlNavigationRepository(
    private val dbControlNavigationRepository: IDBControlNavigationRepository,
    private val actionControlRepository: IDBActionControlRepository,
    private val mapper: ObjectMapper,
) : IControlNavigationRepository {

    override fun existsById(id: UUID): Boolean {
        return dbControlNavigationRepository.existsById(id)
    }

    override fun findById(id: UUID): Optional<ControlNavigationModel> {
        val control = dbControlNavigationRepository.findById(id)
        return control
    }

    override fun existsByActionControlId(actionControlId: String): Boolean {
        return dbControlNavigationRepository.existsByActionControlId(actionControlId)
    }

    override fun findByActionControlId(actionControlId: String): ControlNavigationModel {
        val control = dbControlNavigationRepository.findByActionControlId(actionControlId)
        return control
    }
    @Transactional
    override fun save(control: ControlNavigationEntity): ControlNavigationModel {
        return try {
            val controlNavigationModel = ControlNavigationModel.fromControlNavigationEntity(control)
            dbControlNavigationRepository.save(controlNavigationModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating Navigation Control", e)
        }
    }

    @Transactional
    override fun deleteByActionControlId(actionControlId: String) {
        return dbControlNavigationRepository.deleteByActionControlId(actionControlId)
    }

}
