package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.infraction

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionRepository

@UseCase
class AddOrUpdateInfraction(private val infractionRepo: IInfractionRepository) {
    fun execute(infraction: InfractionEntity): InfractionEntity {
        val savedData = this.infractionRepo.save(infraction).toInfractionEntity()
        return savedData
    }
}
