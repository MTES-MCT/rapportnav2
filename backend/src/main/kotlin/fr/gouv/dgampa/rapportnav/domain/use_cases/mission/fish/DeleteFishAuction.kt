package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.fish

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.fish.IFishAuctionRepository

@UseCase
class DeleteFishAuction(
    private val repo: IFishAuctionRepository
) {
    fun execute(id: Int) {
        return repo.deleteById(id)
    }
}
