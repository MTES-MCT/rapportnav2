package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionIllegalImmigrationEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionIllegalImmigrationModel
import java.util.*

interface INavActionIllegalImmigrationRepository {
    fun findAllByMissionId(missionId: String): List<ActionIllegalImmigrationModel>
    fun findById(id: UUID): Optional<ActionIllegalImmigrationModel>

    fun save(illegalImmigrationEntity: ActionIllegalImmigrationEntity): ActionIllegalImmigrationModel

    fun deleteById(id: UUID)

    fun existsById(id: UUID): Boolean
}
