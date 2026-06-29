package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionPassengerRepository
import java.util.*

@UseCase
class GetMissionPassengers(
    private val repo: IMissionPassengerRepository
) {

    fun execute(missionId: UUID?): List<MissionPassengerEntity> {
        requireNotNull(missionId) { "missionId cannot be null" }

        return repo.findByMissionId(missionId)
            .map { MissionPassengerEntity.fromMissionPassengerModel(it) }
    }
}
