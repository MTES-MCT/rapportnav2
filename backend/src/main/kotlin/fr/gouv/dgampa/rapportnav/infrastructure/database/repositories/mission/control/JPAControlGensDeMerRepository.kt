package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlGensDeMerModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionControlRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlGensDeMerRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Repository
class JPAControlGensDeMerRepository(
    private val dbControlGensDeMerRepository: IDBControlGensDeMerRepository,
    private val actionControlRepository: IDBActionControlRepository,
    private val mapper: ObjectMapper,
) : IControlGensDeMerRepository {

    override fun existsById(id: UUID): Boolean {
        return dbControlGensDeMerRepository.existsById(id)
    }

    override fun findById(id: UUID): Optional<ControlGensDeMerModel> {
        val control = dbControlGensDeMerRepository.findById(id)
        return control
    }

    override fun existsByActionControlId(actionControlId: String): Boolean {
        return dbControlGensDeMerRepository.existsByActionControlId(actionControlId)
    }

    override fun findByActionControlId(actionControlId: String): ControlGensDeMerModel {
        val control = dbControlGensDeMerRepository.findByActionControlId(actionControlId)
        return control
    }

    @Transactional
    override fun save(control: ControlGensDeMerEntity): ControlGensDeMerModel {
        return try {
            val controlGensDeMerModel = ControlGensDeMerModel.fromControlGensDeMerEntity(control)
            dbControlGensDeMerRepository.save(controlGensDeMerModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating GensDeMer Control", e)
        }
    }

    @Transactional
    override fun deleteByActionControlId(actionControlId: String) {
        return dbControlGensDeMerRepository.deleteByActionControlId(actionControlId)
    }

}
