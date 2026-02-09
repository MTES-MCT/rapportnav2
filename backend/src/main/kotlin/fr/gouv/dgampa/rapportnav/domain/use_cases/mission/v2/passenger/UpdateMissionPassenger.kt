package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.PassengerEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionPassengerRepository
import org.slf4j.LoggerFactory

@UseCase
class UpdateMissionPassenger(
    private val repo: IMissionPassengerRepository
) {
    private val logger = LoggerFactory.getLogger(UpdateMissionPassenger::class.java)

    fun execute(
        passenger: PassengerEntity
    ): PassengerEntity {
        val saved = repo.save(passenger)
        return PassengerEntity.fromPassengerModel(saved)
    }
}
