package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionAntiPollutionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionAntiPollutionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionAntiPollutionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionAntiPollutionRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAActionAntiPollutionRepository(
    private val dbActionAntipollutionRepository: IDBActionAntiPollutionRepository,
    private val mapper: ObjectMapper
): INavActionAntiPollutionRepository {
    override fun findAllByMissionId(missionId: Int): List<ActionAntiPollutionModel> {
        return dbActionAntipollutionRepository.findAllByMissionId(missionId)
    }

    override fun findById(id: UUID): Optional<ActionAntiPollutionModel> {
        return dbActionAntipollutionRepository.findById(id)
    }

    @Transactional
    override fun save(antiPollutionEntity: ActionAntiPollutionEntity): ActionAntiPollutionModel {
        return try {
            val antiPollutionModel = ActionAntiPollutionModel.fromAntiPollution(antiPollutionEntity.toNavActionAntiPollution(), mapper)
            dbActionAntipollutionRepository.save(antiPollutionModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating action vigimer", e)
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        dbActionAntipollutionRepository.deleteById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbActionAntipollutionRepository.existsById(id)
    }
}
