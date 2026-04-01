package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AddressEntity
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Entity
@Table(name = "address")
@EntityListeners(AuditingEntityListener::class)
data class AddressModel(

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    val id: UUID? = null,

    @Column(name = "street", nullable = true, length = 255)
    val street: String? = null,

    @Column(name = "zipcode", nullable = true, length = 20)
    val zipcode: String? = null,

    @Column(name = "town", nullable = true, length = 100)
    val town: String? = null,

    @Column(name = "country", nullable = true, length = 3)
    val country: String? = null,

    @Column(name = "longitude", nullable = true)
    val lng: Double? = null,

    @Column(name = "latitude", nullable = true)
    val lat: Double? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant? = null
) {
    fun toAddressEntity(): AddressEntity = AddressEntity(
        id = this.id,
        street = this.street,
        zipcode = this.zipcode,
        town = this.town,
        country = CountryCode.getByCode(this.country),
        lat = this.lat,
        lng = this.lng,
        createdAt = this.createdAt
    )
}
