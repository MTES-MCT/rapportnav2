package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.util.UUID

@UseCase
class GetAllGeneralInfos(
    private val repository: IMissionGeneralInfoRepository,
    private val missionNavRepository: IMissionNavRepository,
) {
    fun execute(): List<MissionGeneralInfoModel> {
        return repository.findAll()
    }

    fun execute(page: Int, size: Int): Page<MissionGeneralInfoModel> {
        return repository.findAllPaginated(page, size)
    }

    fun execute(page: Int, size: Int, search: String?): Page<MissionGeneralInfoModel> {
        if (search.isNullOrBlank()) {
            return repository.findAllPaginated(page, size)
        }

        // The search term may be a mission UUID...
        try {
            val uuid = UUID.fromString(search)
            return repository.findByMissionIdPaginated(uuid, page, size)
        } catch (e: IllegalArgumentException) {
            // ...not a UUID, fall through to the legacy external-id path
        }

        // ...or the legacy MonitorEnv external id (Int), still displayed in the admin UI.
        // Resolve it to the mission UUID, then filter general infos by that mission.
        val externalId = search.trim().toIntOrNull()
        if (externalId != null) {
            val missionId = missionNavRepository.findByExternalId(externalId.toString())
                .orElse(null)?.id
                ?: return Page.empty(PageRequest.of(page, size))
            return repository.findByMissionIdPaginated(missionId, page, size)
        }

        // If the search doesn't match any known format, return all results
        return repository.findAllPaginated(page, size)
    }
}
