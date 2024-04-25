package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRepresentationEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionRepresentationRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionRepresentationModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionRepresentationRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAActionRepresentationRepository(
    private val dbActionRepresentationRepository: IDBActionRepresentationRepository,
    private val mapper: ObjectMapper
): INavActionRepresentationRepository {
    override fun findAllByMissionId(missionId: Int): List<ActionRepresentationModel> {
        return dbActionRepresentationRepository.findAllByMissionId(missionId)
    }

    override fun findById(id: UUID): Optional<ActionRepresentationModel> {
        return dbActionRepresentationRepository.findById(id)
    }

    @Transactional
    override fun save(representationEntity: ActionRepresentationEntity): ActionRepresentationModel {
        return try {
            val representationModel = ActionRepresentationModel.fromRepresentation(representationEntity.toNavActionRepresentation(), mapper)
            dbActionRepresentationRepository.save(representationModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating action representation", e)
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        dbActionRepresentationRepository.deleteById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbActionRepresentationRepository.existsById(id)
    }
}
