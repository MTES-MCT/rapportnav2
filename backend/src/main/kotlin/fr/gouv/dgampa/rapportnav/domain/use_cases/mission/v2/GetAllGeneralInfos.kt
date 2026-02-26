package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import org.springframework.data.domain.Page
import java.util.UUID

@UseCase
class GetAllGeneralInfos(
    private val repository: IMissionGeneralInfoRepository,
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

        // Try to parse as UUID first
        try {
            val uuid = UUID.fromString(search)
            return repository.findByMissionIdUUIDPaginated(uuid, page, size)
        } catch (e: IllegalArgumentException) {
            // Not a UUID, try as missionId (integer)
        }

        // Try to parse as integer (missionId)
        try {
            val missionId = search.toInt()
            return repository.findByMissionIdPaginated(missionId, page, size)
        } catch (e: NumberFormatException) {
            // Not a valid integer, return empty results
        }

        // If search doesn't match any valid format, return all results
        return repository.findAllPaginated(page, size)
    }
}
