package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRepresentationEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionRepresentationModel
import java.util.*

interface INavActionRepresentationRepository {
    fun findAllByMissionId(missionId: String): List<ActionRepresentationModel>
    fun findById(id: UUID): Optional<ActionRepresentationModel>

    fun save(representationEntity: ActionRepresentationEntity): ActionRepresentationModel

    fun deleteById(id: UUID)

    fun existsById(id: UUID): Boolean
}
