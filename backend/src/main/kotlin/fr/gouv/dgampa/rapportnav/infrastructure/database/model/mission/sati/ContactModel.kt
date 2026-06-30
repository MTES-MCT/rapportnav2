package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.Objects

@Entity
@Table(name = "contact")
@EntityListeners(AuditingEntityListener::class)
class ContactModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    var id: Int? = null,

    @Column(name = "full_name", length = 255)
    var fullName: String? = null,

    @Column(name = "first_name", length = 255)
    var firstName: String? = null,

    @Column(name = "last_name", length = 255)
    var lastName: String? = null,

    @Column(name = "nationality", length = 16)
    var nationality: String? = null,

    @Column(name = "email", length = 255)
    var email: String? = null,

    @Column(name = "phone", length = 50)
    var phone: String? = null,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    var addresses: MutableList<AddressModel> = mutableListOf(),

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null
) {
    override fun hashCode(): Int {
        return Objects.hash(id, fullName, firstName, lastName, nationality, email, phone, addresses)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ContactModel
        return id == other.id
            && fullName == other.fullName
            && firstName == other.firstName
            && lastName == other.lastName
            && nationality == other.nationality
            && email == other.email
            && phone == other.phone
            && addresses == other.addresses
    }
}
