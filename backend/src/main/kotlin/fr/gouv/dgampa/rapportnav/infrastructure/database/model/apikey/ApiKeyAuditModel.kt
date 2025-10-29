package fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey

import jakarta.persistence.Id
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "api_key_audit", indexes = [
    Index(name = "idx_audit_timestamp", columnList = "timestamp"),
    Index(name = "idx_audit_api_key_id", columnList = "apiKeyId")
])
data class ApiKeyAuditModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "api_key_id")
    val apiKeyId: UUID?,

    @Column(name = "ip_address", length = 45)
    val ipAddress: String?,

    @Column(name = "request_path", length = 500)
    val requestPath: String?,

    @Column(nullable = false)
    val success: Boolean,

    @Column(name = "failure_reason", length = 255)
    val failureReason: String? = null,

    @Column(nullable = false)
    val timestamp: Instant = Instant.now()
)
