package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControl
import java.util.*

interface INavActionControlRepository {
    fun findAllByMissionId( missionId: Int): List<ActionControl>
    fun existsById(id: UUID): Boolean
    fun save(controlAction: ActionControl): ActionControl
    fun deleteById(id: UUID)
}
