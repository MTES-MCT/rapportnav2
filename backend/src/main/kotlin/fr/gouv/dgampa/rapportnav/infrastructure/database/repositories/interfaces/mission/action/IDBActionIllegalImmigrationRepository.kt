package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionIllegalImmigrationEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionIllegalImmigrationModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBActionIllegalImmigrationRepository: JpaRepository<ActionIllegalImmigrationModel, UUID> {

    fun findAllByMissionId(missionId: String): List<ActionIllegalImmigrationModel>

    override fun findById(id: UUID): Optional<ActionIllegalImmigrationModel>

    fun save(illegalImmigrationEntity: ActionIllegalImmigrationEntity): ActionIllegalImmigrationModel

    override fun deleteById(id: UUID)

    override fun existsById(id: UUID): Boolean
}
