package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.PortEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Port

@UseCase
class GetPorts(
    private val fishActionRepo: IFishActionRepository,
) {
    fun execute(): List<PortEntity> {
        return fishActionRepo.getPorts()
    }
    fun execute(name: String?): List<PortEntity> {
        if (name.isNullOrBlank()) return emptyList()

        return fishActionRepo.getPorts()
            .filter {
                it.name?.startsWith(name, ignoreCase = true) == true ||
                    it.locode.equals(name, ignoreCase = true)
            }
            .sortedBy { it.name }
    }
}
