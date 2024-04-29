package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRepresentationEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionRepresentationModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBActionRepresentationRepository: JpaRepository<ActionRepresentationModel, UUID> {

    fun findAllByMissionId(missionId: Int): List<ActionRepresentationModel>

    override fun findById(id: UUID): Optional<ActionRepresentationModel>

    fun save(representationEntity: ActionRepresentationEntity): ActionRepresentationModel

    override fun deleteById(id: UUID)

    override fun existsById(id: UUID): Boolean
}
