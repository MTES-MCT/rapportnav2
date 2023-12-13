package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionRepository
import java.util.*

@UseCase
class GetInfractionById(private val repo: IInfractionRepository) {
    fun execute(id: UUID): InfractionEntity? {
        val optionalInfractionModel = repo.findById(id)
        return optionalInfractionModel.map { it.toInfractionEntity() }.orElse(null)
    }
}
