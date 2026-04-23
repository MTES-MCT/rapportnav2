package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.fish

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FishAuctionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.fish.IFishAuctionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.fish.FishAuctionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.fish.IDBFishAuctionRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

@Repository
class JPAFishAuctionRepository(
    private val dbFishAuctionRepository: IDBFishAuctionRepository
) : IFishAuctionRepository {

    override fun findAll(): List<FishAuctionEntity> {
        return try {
            dbFishAuctionRepository.findAll().map { it.toFishAuctionEntity() }
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAFishAuctionRepository.findAll failed",
                originalException = e
            )
        }
    }

    override fun findById(id: Int): FishAuctionEntity? {
        return try {
            dbFishAuctionRepository.findById(id).getOrNull()?.toFishAuctionEntity()
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAFishAuctionRepository.findById failed for id=$id",
                originalException = e
            )
        }
    }

    override fun save(entity: FishAuctionEntity): FishAuctionEntity {
        return try {
            val model = FishAuctionModel.fromFishAuctionEntity(entity)
            dbFishAuctionRepository.save(model).toFishAuctionEntity()
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "JPAFishAuctionRepository.save failed for id=${entity.id}",
                e
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAFishAuctionRepository.save failed for id=${entity.id}",
                originalException = e
            )
        }
    }

    override fun deleteById(id: Int) {
        try {
            val fishAuction = dbFishAuctionRepository.findById(id).getOrNull() ?: return
            fishAuction.deletedAt = Instant.now()
            dbFishAuctionRepository.save(fishAuction)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "JPAFishAuctionRepository.deleteById failed for id=$id",
                e
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAFishAuctionRepository.deleteById failed for id=$id",
                originalException = e
            )
        }
    }
}
