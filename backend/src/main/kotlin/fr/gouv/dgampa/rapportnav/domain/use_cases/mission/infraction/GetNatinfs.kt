package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.INatinfRepository

@UseCase
class GetNatinfs(
    private val repository: INatinfRepository
) {

    fun execute(): List<NatinfEntity> {
        return repository.findAll()
    }
}
