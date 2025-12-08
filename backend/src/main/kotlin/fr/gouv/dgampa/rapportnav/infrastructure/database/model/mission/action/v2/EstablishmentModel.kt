package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Table(name = "establishment")
@Entity
@EntityListeners(AuditingEntityListener::class)
class EstablishmentModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null,

    @Column(name = "name", unique = false, nullable = false)
    var name: String? = null,

    @Column(name = "siren", nullable = true)
    var siren: String? = null,

    @Column(name = "siret", nullable = true)
    var siret: String? = null,

    @Column(name = "address", nullable = true)
    var address: String? = null,

    @Column(name = "city", nullable = true)
    var city: String? = null,

    @Column(name = "country", nullable = true)
    var country: String? = null,

    @Column(name = "zipcode", nullable = true)
    var zipcode: String? = null,

    @Column(name = "is_foreign", nullable = true)
    var isForeign: Boolean? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null,
)
