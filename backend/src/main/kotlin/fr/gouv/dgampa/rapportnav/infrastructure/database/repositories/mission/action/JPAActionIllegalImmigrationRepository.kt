package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionIllegalImmigrationEntity
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
    private val mapper: ObjectMapper
): INavActionIllegalImmigrationRepository {
    override fun findAllByMissionId(missionId: Int): List<ActionIllegalImmigrationModel> {
        return dbActionIllegalImmigrationRepository.findAllByMissionId(missionId)
    }

    override fun findById(id: UUID): Optional<ActionIllegalImmigrationModel> {
        return dbActionIllegalImmigrationRepository.findById(id)
    }

    @Transactional
    override fun save(illegalImmigrationEntity: ActionIllegalImmigrationEntity): ActionIllegalImmigrationModel {
        return try {
            val illegalImmigrationModel = ActionIllegalImmigrationModel.fromIllegalImmigration(illegalImmigrationEntity.toNavActionIllegalImmigration(), mapper)
            dbActionIllegalImmigrationRepository.save(illegalImmigrationModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating action illegal immigration", e)
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
