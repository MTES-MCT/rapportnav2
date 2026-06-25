package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(name = "address")
@EntityListeners(AuditingEntityListener::class)
 class AddressModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    var id: Int? = null,

    @Column(name = "street", length = 255)
    var street: String? = null,

    @Column(name = "full_address", length = 400)
    var fullAddress: String? = null,

    @Column(name = "zipcode", length = 20)
    var zipcode: String? = null,

    @Column(name = "town", length = 100)
    var town: String? = null,

    @Column(name = "country", length = 3)
    var country: String? = null,

    @Column(name = "longitude")
    var lng: Double? = null,

    @Column(name = "latitude")
    var lat: Double? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant? = null
)
