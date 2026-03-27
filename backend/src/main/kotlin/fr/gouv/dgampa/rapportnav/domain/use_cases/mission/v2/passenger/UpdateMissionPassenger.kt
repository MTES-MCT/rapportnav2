package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionPassengerRepository
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateThrowsBeforeSave

@UseCase
class UpdateMissionPassenger(
    private val repo: IMissionPassengerRepository,
    private val entityValidityValidator: EntityValidityValidator
) {
    fun execute(passenger: MissionPassengerEntity): MissionPassengerEntity {
        entityValidityValidator.validateAndThrow(passenger, ValidateThrowsBeforeSave::class.java)
        val saved = repo.save(passenger)
        return MissionPassengerEntity.fromMissionPassengerModel(saved)
    }
}
