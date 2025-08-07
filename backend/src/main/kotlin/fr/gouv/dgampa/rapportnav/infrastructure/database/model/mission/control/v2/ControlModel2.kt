package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.v2

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.v2.InfractionModel2
import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EntityListeners(AuditingEntityListener::class)
@Table(name = "control_2")
data class ControlModel2(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    var id: UUID = UUID.randomUUID(),

    @Enumerated(EnumType.STRING)
    @Column(name = "control_type", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    var controlType: ControlType,

    @Column(name = "amount_of_controls", nullable = false)
    var amountOfControls: Int,

    @Column(name = "observations", nullable = true)
    var observations: String? = null,

    @Column(name = "staff_outnumbered", nullable = true)
    var staffOutnumbered: String? = null,

    @Column(name = "up_to_date_medical_check", nullable = true)
    var upToDateMedicalCheck: String? = null,

    @Column(name = "knowledge_of_french_law_and_language", nullable = true)
    var knowledgeOfFrenchLawAndLanguage: String? = null,

    @Column(name = "compliant_operating_permit", nullable = true)
    var compliantOperatingPermit: String? = null,

    @Column(name = "up_to_date_navigation_permit", nullable = true)
    var upToDateNavigationPermit: String? = null,

    @Column(name = "compliant_security_documents", nullable = true)
    var compliantSecurityDocuments: String? = null,

    @Column(name = "has_been_done", nullable = true)
    var hasBeenDone: Boolean? = null,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "control_id")
    @JsonIgnore
    var infractions: List<InfractionModel2>? = mutableListOf(),

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null
) {
    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ControlModel2
        return id == other.id
    }
}
