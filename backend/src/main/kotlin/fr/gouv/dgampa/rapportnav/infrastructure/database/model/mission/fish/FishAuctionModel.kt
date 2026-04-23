package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.fish

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FacadeTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FishAuctionEntity
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "fish_auction")
class FishAuctionModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int?,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "facade", nullable = false)
    var facade: String,

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null,

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    var createdBy: Int? = null,

    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: Int? = null,

    @Column(name = "deleted_at", nullable = true)
    var deletedAt: Instant? = null
) {
    fun toFishAuctionEntity(): FishAuctionEntity {
        return FishAuctionEntity(
            id = id,
            name = name,
            facade = FacadeTypeEnum.valueOf(facade),
            createdAt = createdAt,
            updatedAt = updatedAt,
            createdBy = createdBy,
            updatedBy = updatedBy,
            deletedAt = deletedAt
        )
    }

    companion object {
        fun fromFishAuctionEntity(entity: FishAuctionEntity): FishAuctionModel {
            return FishAuctionModel(
                id = entity.id,
                name = entity.name,
                facade = entity.facade.name,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                createdBy = entity.createdBy,
                updatedBy = entity.updatedBy,
                deletedAt = entity.deletedAt
            )
        }
    }
}
