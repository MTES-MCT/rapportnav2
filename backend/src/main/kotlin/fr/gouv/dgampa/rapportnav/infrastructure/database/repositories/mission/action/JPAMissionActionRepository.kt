package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBMissionActionRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAMissionActionRepository(
    private val dbMissionActionRepository: IDBMissionActionRepository,
) : INavMissionActionRepository {
    override fun findByMissionId(missionId: Int): List<MissionActionModel> {
        return dbMissionActionRepository.findAllByMissionId(missionId)
    }

    override fun findById(id: UUID): Optional<MissionActionModel> {
        return dbMissionActionRepository.findById(id)
    }

    @Transactional
    override fun save(action: MissionNavActionEntity): MissionActionModel {
        return try {
            val missionActionModel = MissionActionModel.fromMissionActionEntity(action)
            dbMissionActionRepository.save(missionActionModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save MissionAction='${action.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving MissionAction='${action.id}'",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        dbMissionActionRepository.deleteById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbMissionActionRepository.existsById(id)
    }
}
