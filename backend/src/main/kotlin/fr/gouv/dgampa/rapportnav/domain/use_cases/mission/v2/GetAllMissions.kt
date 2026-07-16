package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.springframework.data.domain.Page

@UseCase
class GetAllMissions(
    private val repository: IMissionNavRepository,
) {
    fun execute(page: Int, size: Int): Page<MissionModel> {
        return repository.findAllPaginated(page, size)
    }
}
