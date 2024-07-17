package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionIllegalImmigrationEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionIllegalImmigrationRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionIllegalImmigrationModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionIllegalImmigrationRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAActionIllegalImmigrationRepository(
    private val dbActionIllegalImmigrationRepository: IDBActionIllegalImmigrationRepository,
) : INavActionIllegalImmigrationRepository {
    override fun findAllByMissionId(missionId: Int): List<ActionIllegalImmigrationModel> {
        return dbActionIllegalImmigrationRepository.findAllByMissionId(missionId)
    }

    override fun findById(id: UUID): Optional<ActionIllegalImmigrationModel> {
        return dbActionIllegalImmigrationRepository.findById(id)
    }

    @Transactional
    override fun save(illegalImmigrationEntity: ActionIllegalImmigrationEntity): ActionIllegalImmigrationModel {
        return try {
            val illegalImmigrationModel =
                ActionIllegalImmigrationModel.fromIllegalImmigrationEntity(illegalImmigrationEntity)
            dbActionIllegalImmigrationRepository.save(illegalImmigrationModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save ActionIllegalImmigration='${illegalImmigrationEntity.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving ActionIllegalImmigration='${illegalImmigrationEntity.id}'",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        dbActionIllegalImmigrationRepository.deleteById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbActionIllegalImmigrationRepository.existsById(id)
    }
}
