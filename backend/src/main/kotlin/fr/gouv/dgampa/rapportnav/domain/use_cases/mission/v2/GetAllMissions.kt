package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.springframework.data.domain.Page
import java.util.UUID

@UseCase
class GetAllMissions(
    private val repository: IMissionNavRepository,
) {
    fun execute(page: Int, size: Int): Page<MissionModel> {
        return repository.findAllPaginated(page, size)
    }

    fun execute(page: Int, size: Int, searchId: String?): Page<MissionModel> {
        if (searchId.isNullOrBlank()) {
            return repository.findAllPaginated(page, size)
        }

        val trimmed = searchId.trim()

        // A UUID matches the mission id (primary key); anything else is treated as an
        // external id (mostly an integer, stored as a string).
        return try {
            val uuid = UUID.fromString(trimmed)
            repository.findByIdPaginated(uuid, page, size)
        } catch (e: IllegalArgumentException) {
            repository.findByExternalIdPaginated(trimmed, page, size)
        }
    }
}
