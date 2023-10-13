package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlNavigationRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@Repository
class JPAControlNavigationRepository(
    private val dbControlNavigationRepository: IDBControlNavigationRepository,
    private val mapper: ObjectMapper,
) : IControlNavigationRepository {

    override fun findAllByMissionId(missionId: Int): List<ControlNavigation> {
        // TODO call correct function filtering by mission id
        TODO("Not yet implemented")
//        return dbControlNavigationRulesRepository.findAll().map { it.toControlNavigationRules() }

    }

    @Transactional
    override fun save(control: ControlNavigation): ControlNavigation {
        return try {
            val controlModel = ControlNavigationModel.fromControlNavigation(control, mapper)
            dbControlNavigationRepository.save(controlModel).toControlNavigation()
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating Navigation Control", e)
        }
    }

}
