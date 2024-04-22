package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionNauticalEventEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionNauticalEventRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionNauticalEventModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionNauticalEventRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAActionNauticalEventRepository(
    private val dbActionNauticalEventRepository: IDBActionNauticalEventRepository,
    private val mapper: ObjectMapper
): INavActionNauticalEventRepository {
    override fun findAllByMissionId(missionId: Int): List<ActionNauticalEventModel> {
        return dbActionNauticalEventRepository.findAllByMissionId(missionId)
    }

    override fun findById(id: UUID): Optional<ActionNauticalEventModel> {
        return dbActionNauticalEventRepository.findById(id)
    }

    @Transactional
    override fun save(nauticalEvent: ActionNauticalEventEntity): ActionNauticalEventModel {
       return dbActionNauticalEventRepository.save(nauticalEvent)
    }

    @Transactional
    override fun deleteById(id: UUID) {
        dbActionNauticalEventRepository.deleteById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbActionNauticalEventRepository.existsById(id)
    }
}