package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionVigimerEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionVigimerRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionVigimerModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionVigimerRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAActionVigimerRepository(
    private val dbActionVigimerRepository: IDBActionVigimerRepository,
) : INavActionVigimerRepository {
    override fun findAllByMissionId(missionId: Int): List<ActionVigimerModel> {
        return dbActionVigimerRepository.findAllByMissionId(missionId)
    }

    override fun findById(id: UUID): Optional<ActionVigimerModel> {
        return dbActionVigimerRepository.findById(id)
    }

    @Transactional
    override fun save(vigimerEntity: ActionVigimerEntity): ActionVigimerModel {
        return try {
            val vigimerModel = ActionVigimerModel.fromVigimerEntity(vigimerEntity)
            dbActionVigimerRepository.save(vigimerModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating action vigimer", e)
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        dbActionVigimerRepository.deleteById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbActionVigimerRepository.existsById(id)
    }
}
