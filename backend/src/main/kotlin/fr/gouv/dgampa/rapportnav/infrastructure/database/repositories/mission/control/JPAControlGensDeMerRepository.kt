package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlGensDeMerModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionControlRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlGensDeMerRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@Repository
class JPAControlGensDeMerRepository(
    private val dbControlGensDeMerRepository: IDBControlGensDeMerRepository,
    private val actionControlRepository: IDBActionControlRepository,
    private val mapper: ObjectMapper,
) : IControlGensDeMerRepository {
    @Transactional
    override fun save(control: ControlGensDeMer): ControlGensDeMerModel {
        return try {
            val actionControl = actionControlRepository.findById(control.actionControlId)
            val controlGensDeMerModel = ControlGensDeMerModel.fromControlGensDeMer(control, actionControl)
            dbControlGensDeMerRepository.save(controlGensDeMerModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating GensDeMer Control", e)
        }
    }

}
