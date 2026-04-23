package fr.gouv.dgampa.rapportnav.domain.repositories.mission.fish

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FishAuctionEntity

interface IFishAuctionRepository {
    fun findAll(): List<FishAuctionEntity>
    fun findById(id: Int): FishAuctionEntity?
    fun save(entity: FishAuctionEntity): FishAuctionEntity
    fun deleteById(id: Int)
}
