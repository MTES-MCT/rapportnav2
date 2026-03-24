package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "contact")
data class ContactModel(

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    val id: UUID? = null,

    @Column(name = "full_name", length = 255)
    val fullName: String? = null,

    @Column(length = 3)
    val nationality: String? = null,

    @Column(length = 255)
    val email: String? = null,

    @Column(length = 50)
    val phone: String? = null,

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    val address: AddressModel? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: OffsetDateTime? = null
)
