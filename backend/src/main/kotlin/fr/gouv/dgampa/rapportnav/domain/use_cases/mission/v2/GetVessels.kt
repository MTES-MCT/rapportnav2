package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.VesselEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import org.springframework.cache.annotation.Cacheable

@UseCase
class GetVessels(
    private val fishActionRepo: IFishActionRepository,
) {
    @Cacheable(value = ["vessels"])
    fun execute(): List<VesselEntity> {
        return fishActionRepo.getVessels().map { VesselEntity.fromVesselIdentityDataOutput(it) }
    }
}
