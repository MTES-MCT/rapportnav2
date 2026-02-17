package fr.gouv.dgampa.rapportnav.infrastructure.database.model.user

import fr.gouv.dgampa.rapportnav.domain.entities.user.AuthEventTypeEnum
import jakarta.persistence.Id
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(
    name = "authentication_audit",
    indexes = [
        Index(name = "idx_auth_audit_timestamp", columnList = "timestamp"),
        Index(name = "idx_auth_audit_user_id", columnList = "user_id"),
        Index(name = "idx_auth_audit_email", columnList = "email"),
        Index(name = "idx_auth_audit_event_type", columnList = "event_type")
    ]
)
data class AuthenticationAuditModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "user_id")
    val userId: Int?,

    @Column(name = "email", length = 255, nullable = false)
    val email: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    val eventType: AuthEventTypeEnum,

    @Column(name = "ip_address", length = 45)
    val ipAddress: String?,

    @Column(name = "user_agent", length = 500)
    val userAgent: String?,

    @Column(nullable = false)
    val success: Boolean,

    @Column(name = "failure_reason", length = 255)
    val failureReason: String? = null,

    @Column(nullable = false)
    val timestamp: Instant = Instant.now()
)