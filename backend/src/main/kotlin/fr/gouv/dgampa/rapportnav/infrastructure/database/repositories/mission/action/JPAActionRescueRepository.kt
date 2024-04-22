package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import ActionRescueEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionRescueModel
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionRescueRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionRescueRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAActionRescueRepository(
    private val dbActionRescueRepository: IDBActionRescueRepository,
    private val mapper: ObjectMapper
): INavActionRescueRepository {
    override fun findAllByMissionId(missionId: Int): List<ActionRescueModel> {
        return dbActionRescueRepository.findAllByMissionId(missionId)
    }

    override fun findById(id: UUID): Optional<ActionRescueModel> {
        return dbActionRescueRepository.findById(id)
    }

    @Transactional
    override fun save(rescueModel: ActionRescueEntity): ActionRescueModel {
        return dbActionRescueRepository.save(rescueModel)
    }

    @Transactional
    override fun deleteById(id: UUID) {
        dbActionRescueRepository.deleteById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbActionRescueRepository.existsById(id)
    }
}
