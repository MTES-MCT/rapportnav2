package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import org.springframework.data.domain.Page
import java.util.*

interface INavMissionActionRepository {
    fun findByMissionId(missionId: Int): List<MissionActionModel>

    fun findByOwnerId(ownerId: UUID): List<MissionActionModel>

    fun findAllPaginated(page: Int, size: Int): Page<MissionActionModel>

    fun findByIdPaginated(id: UUID, page: Int, size: Int): Page<MissionActionModel>

    fun findByOwnerIdPaginated(ownerId: UUID, page: Int, size: Int): Page<MissionActionModel>

    fun findById(id: UUID): Optional<MissionActionModel>

    fun save(action: MissionActionModel): MissionActionModel

    fun deleteById(id: UUID)

    fun existsById(id: UUID): Boolean
}
