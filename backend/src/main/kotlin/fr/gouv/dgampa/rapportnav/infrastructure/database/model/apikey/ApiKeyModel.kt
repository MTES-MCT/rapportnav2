package fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey

import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "api_key", uniqueConstraints = [
    UniqueConstraint(columnNames = ["public_id"])
])
class ApiKeyModel(
    @Id
    @Column(columnDefinition = "uuid", nullable = false, updatable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "public_id", nullable = false, length = 100, unique = true)
    val publicId: String,

    @Column(name = "hashed_key", nullable = false, length = 255)
    val hashedKey: String,

    @Column(name = "owner", length = 100)
    val owner: String? = null,

    @Column(name = "disabled_at")
    val disabledAt: Instant? = null,

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
    var updatedBy: Int? = null
) {
    fun toApiKeyEntity(): ApiKeyEntity = ApiKeyEntity(
        id = id,
        owner = owner,
        publicId = publicId,
        hashedKey = hashedKey,
        disabledAt = disabledAt,
    )

    companion object {
        fun fromApiKeyEntity(user: ApiKeyEntity) = ApiKeyModel(
            id = user.id,
            owner = user.owner,
            publicId = user.publicId,
            hashedKey = user.hashedKey,
            disabledAt = user.disabledAt,
        )
    }
}
