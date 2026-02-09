package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.PassengerEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionPassengerRepository
import java.util.*

@UseCase
class GetMissionPassengers(
    private val repo: IMissionPassengerRepository
) {

    fun execute(missionId: Int?): List<PassengerEntity> {
        requireNotNull(missionId) { "missionId cannot be null" }

        return repo.findByMissionId(missionId)
            .map { PassengerEntity.fromPassengerModel(it) }
    }

    fun execute(missionIdUUID: UUID?): List<PassengerEntity> {
        requireNotNull(missionIdUUID) { "missionIdUUID cannot be null" }

        return repo.findByMissionIdUUID(missionIdUUID)
            .map { PassengerEntity.fromPassengerModel(it) }
    }
}
