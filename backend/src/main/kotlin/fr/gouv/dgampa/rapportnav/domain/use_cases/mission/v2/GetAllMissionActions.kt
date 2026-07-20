package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.util.UUID

@UseCase
class GetAllMissionActions(
    private val repository: INavMissionActionRepository,
) {
    fun execute(page: Int, size: Int, searchId: String?, searchOwnerId: String?): Page<MissionActionModel> {
        // Search by action id takes precedence over ownerId. Both columns are UUIDs, so an
        // unparseable (but non-blank) search value means "no match" -> return an empty page.
        if (!searchId.isNullOrBlank()) {
            return parseUuid(searchId)
                ?.let { repository.findByIdPaginated(it, page, size) }
                ?: Page.empty(PageRequest.of(page, size))
        }

        if (!searchOwnerId.isNullOrBlank()) {
            return parseUuid(searchOwnerId)
                ?.let { repository.findByOwnerIdPaginated(it, page, size) }
                ?: Page.empty(PageRequest.of(page, size))
        }

        return repository.findAllPaginated(page, size)
    }

    private fun parseUuid(value: String): UUID? {
        return try {
            UUID.fromString(value.trim())
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
