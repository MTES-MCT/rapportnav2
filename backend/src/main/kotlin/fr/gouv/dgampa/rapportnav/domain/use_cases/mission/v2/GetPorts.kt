package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.PortEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository

@UseCase
class GetPorts(
    private val fishActionRepo: IFishActionRepository,
) {
    fun execute(): List<PortEntity> {
        return fishActionRepo.getPorts()
    }
}
