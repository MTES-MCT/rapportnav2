package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionBAAEMPermanenceEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionBAAEMRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionBAAEMPermanenceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionBAAEMPermanenceRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAActionBAAEMPermanenceRepository(
    private val dbActionBAAEMPermanenceRepository: IDBActionBAAEMPermanenceRepository,
    private val mapper: ObjectMapper
): INavActionBAAEMRepository {
    override fun findAllByMissionId(missionId: Int): List<ActionBAAEMPermanenceModel> {
        return dbActionBAAEMPermanenceRepository.findAllByMissionId(missionId = missionId)
    }

    override fun findById(id: UUID): Optional<ActionBAAEMPermanenceModel> {
        return dbActionBAAEMPermanenceRepository.findById(id = id)
    }

    @Transactional
    override fun save(permanenceBAAEM: ActionBAAEMPermanenceEntity): ActionBAAEMPermanenceModel {
        return try {
            val baaemModel = ActionBAAEMPermanenceModel.fromBAAEMPermanence(permanenceBAAEM.toNavActionBAAEMPermanence(), mapper)
            dbActionBAAEMPermanenceRepository.save(baaemModel)
        } catch (e: InvalidDataAccessApiUsageException) {

            throw Exception("Error saving or updating action BAAEM", e)
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        dbActionBAAEMPermanenceRepository.deleteById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbActionBAAEMPermanenceRepository.existsById(id)
    }
}
