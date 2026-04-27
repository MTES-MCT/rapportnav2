package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.fish

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.fish.FishAuctionModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBFishAuctionRepository : JpaRepository<FishAuctionModel, Int>
