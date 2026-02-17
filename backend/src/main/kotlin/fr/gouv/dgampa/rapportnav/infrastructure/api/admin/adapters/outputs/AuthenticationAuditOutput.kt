package fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.user.AuthEventTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.AuthenticationAuditModel
import java.time.Instant

data class AuthenticationAuditOutput(
    val id: Int?,
    val userId: Int?,
    val email: String,
    val eventType: AuthEventTypeEnum,
    val ipAddress: String?,
    val userAgent: String?,
    val success: Boolean,
    val failureReason: String?,
    val timestamp: Instant
) {
    companion object {
        fun fromModel(model: AuthenticationAuditModel): AuthenticationAuditOutput {
            return AuthenticationAuditOutput(
                id = model.id,
                userId = model.userId,
                email = model.email,
                eventType = model.eventType,
                ipAddress = model.ipAddress,
                userAgent = model.userAgent,
                success = model.success,
                failureReason = model.failureReason,
                timestamp = model.timestamp
            )
        }
    }
}