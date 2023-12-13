package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionRepository

@UseCase
class GetInfractionsForActionControlEnv(private val repo: IInfractionRepository) {
    fun execute(actionId: String): List<InfractionEntity> {
        return repo.findAllByActionId(actionId = actionId).map { it.toInfractionEntity() }
    }
}
