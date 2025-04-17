package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionRescueModel
import java.util.*

interface INavActionRescueRepository {

    fun findAllByMissionId(missionId: String): List<ActionRescueModel>

    fun findById(id: UUID): Optional<ActionRescueModel>

    fun save(rescueAction: ActionRescueEntity): ActionRescueModel

    fun deleteById(id: UUID)

    fun existsById(id: UUID): Boolean
}
