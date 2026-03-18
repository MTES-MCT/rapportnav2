package fr.gouv.dgampa.rapportnav.infrastructure.database.model.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(
    name = "impersonation_audit",
    indexes = [
        Index(name = "idx_impersonation_audit_admin_user_id", columnList = "admin_user_id"),
        Index(name = "idx_impersonation_audit_timestamp", columnList = "timestamp")
    ]
)
data class ImpersonationAuditModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "admin_user_id", nullable = false)
    val adminUserId: Int,

    @Column(name = "target_service_id", nullable = false)
    val targetServiceId: Int,

    @Column(name = "ip_address", length = 45)
    val ipAddress: String?,

    @Column(nullable = false)
    val timestamp: Instant = Instant.now()
)
