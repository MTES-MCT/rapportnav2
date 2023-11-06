package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionControlModel
import java.util.*

interface INavActionControlRepository {

    fun findById(id: UUID): Optional<ActionControlModel>
    
    fun findAllByMissionId( missionId: Int): List<ActionControlEntity>

    fun existsById(id: UUID): Boolean

    fun deleteById(id: UUID)

    fun save(controlAction: ActionControlEntity): ActionControlModel

}
