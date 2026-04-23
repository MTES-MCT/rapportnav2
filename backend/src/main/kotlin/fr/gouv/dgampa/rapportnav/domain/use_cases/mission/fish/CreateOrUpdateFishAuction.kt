package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.fish

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FishAuctionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.fish.IFishAuctionRepository

@UseCase
class CreateOrUpdateFishAuction(
    private val repo: IFishAuctionRepository
) {
    fun execute(entity: FishAuctionEntity): FishAuctionEntity {
        return repo.save(entity)
    }
}
