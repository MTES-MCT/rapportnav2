package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.fish

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FishAuctionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.fish.IFishAuctionRepository

@UseCase
class GetFishAuctions(private val fishAuctionRepository: IFishAuctionRepository) {

    fun execute(includeDeleted: Boolean = false): List<FishAuctionEntity> {
        val all = fishAuctionRepository.findAll()
        return if (includeDeleted) all else all.filter { it.deletedAt == null }
    }
}
