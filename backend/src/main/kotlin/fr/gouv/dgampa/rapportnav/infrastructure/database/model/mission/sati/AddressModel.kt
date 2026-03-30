package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "address")
@EntityListeners(AuditingEntityListener::class)
data class AddressModel(

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    val id: UUID? = null,

    @Column(length = 255)
    val street: String? = null,

    @Column(length = 20)
    val zipcode: String? = null,

    @Column(length = 100)
    val town: String? = null,

    @Column(length = 3)
    val country: String? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant? = null
)
