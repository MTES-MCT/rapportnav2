package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FacadeTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FishAuctionEntity

class FishAuction(
    val id: Int? = null,
    val name: String,
    val facade: FacadeTypeEnum
) {
    fun toFishAuctionEntity(): FishAuctionEntity {
        return FishAuctionEntity(
            id = id,
            name = name,
            facade = facade
        )
    }

    companion object {
        fun fromFishAuctionEntity(entity: FishAuctionEntity): FishAuction {
            return FishAuction(
                id = entity.id,
                name = entity.name,
                facade = entity.facade
            )
        }
    }
}
