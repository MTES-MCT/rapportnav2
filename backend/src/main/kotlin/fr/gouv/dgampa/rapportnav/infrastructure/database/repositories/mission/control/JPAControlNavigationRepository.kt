package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionControlRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlNavigationRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@Repository
class JPAControlNavigationRepository(
    private val dbControlNavigationRepository: IDBControlNavigationRepository,
    private val actionControlRepository: IDBActionControlRepository,
    private val mapper: ObjectMapper,
) : IControlNavigationRepository {

    @Transactional
    override fun save(control: ControlNavigation): ControlNavigationModel {
        return try {
            val actionControl = actionControlRepository.findById(control.actionControlId)
            val controlNavigationModel = ControlNavigationModel.fromControlNavigation(control, actionControl)
            dbControlNavigationRepository.save(controlNavigationModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating Navigation Control", e)
        }
    }

}
