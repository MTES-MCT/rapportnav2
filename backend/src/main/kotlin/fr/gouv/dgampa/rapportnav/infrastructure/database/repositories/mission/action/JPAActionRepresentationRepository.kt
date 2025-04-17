package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRepresentationEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
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
) : INavActionRepresentationRepository {
    override fun findAllByMissionId(missionId: String): List<ActionRepresentationModel> {
        return dbActionRepresentationRepository.findAllByMissionId(missionId)
    }

    override fun findById(id: UUID): Optional<ActionRepresentationModel> {
        return dbActionRepresentationRepository.findById(id)
    }

    @Transactional
    override fun save(representationEntity: ActionRepresentationEntity): ActionRepresentationModel {
        return try {
            val representationModel = ActionRepresentationModel.fromRepresentationEntity(representationEntity)
            dbActionRepresentationRepository.save(representationModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save ActionRepresentation='${representationEntity.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving ActionRepresentation='${representationEntity.id}'",
                originalException = e
            )
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
