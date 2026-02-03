package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.target2.v2

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.v2.ControlModel
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "target_2")
data class TargetModel2(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    var id: UUID = UUID.randomUUID(),

    @Column(name = "action_Id", nullable = false)
    var actionId: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    var targetType: TargetType,

    @Column(name = "status", nullable = true)
    var status: String? = null,

    @Column(name = "main_agent", nullable = true)
    var agent: String? = null,

    @Column(name = "vessel_name", nullable = true)
    var vesselName: String? = null,

    @Column(name = "vessel_identifier", nullable = true)
    var vesselIdentifier: String? = null,

    @Column(name = "identity_controlled_person", nullable = true)
    var identityControlledPerson: String? = null,

    @Column(name = "vessel_type", nullable = true)
    var vesselType: String? = null,

    @Column(name = "vessel_size", nullable = true)
    var vesselSize: String? = null,

    @Column(name = "start_datetime_utc", nullable = true)
    var startDateTimeUtc: Instant? = null,

    @Column(name = "end_datetime_utc", nullable = true)
    var endDateTimeUtc: Instant? = null,

    @Column(name = "source", nullable = true)
    var source: String? = null,

    @Column(name = "external_id", nullable = true)
    var externalId: String? = null,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "target_id")
    @JsonIgnore
    var controls: List<ControlModel>? = mutableListOf(),

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
    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TargetModel2
        return id == other.id
    }
}
