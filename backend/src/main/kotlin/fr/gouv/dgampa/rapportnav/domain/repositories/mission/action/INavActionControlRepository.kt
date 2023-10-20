package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControl
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionControlModel
import java.util.*

interface INavActionControlRepository {

    fun findById(id: UUID): ActionControlModel
    
    fun findAllByMissionId( missionId: Int): List<ActionControl>

    fun existsById(id: UUID): Boolean

    fun save(controlAction: ActionControl): ActionControl

}
