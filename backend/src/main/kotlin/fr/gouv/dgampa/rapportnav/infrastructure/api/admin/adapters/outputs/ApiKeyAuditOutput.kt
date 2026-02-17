package fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.outputs

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyAuditModel
import java.time.Instant
import java.util.UUID

data class ApiKeyAuditOutput(
    val id: Int?,
    val apiKeyId: UUID?,
    val ipAddress: String?,
    val requestPath: String?,
    val success: Boolean,
    val failureReason: String?,
    val timestamp: Instant
) {
    companion object {
        fun fromModel(model: ApiKeyAuditModel): ApiKeyAuditOutput {
            return ApiKeyAuditOutput(
                id = model.id,
                apiKeyId = model.apiKeyId,
                ipAddress = model.ipAddress,
                requestPath = model.requestPath,
                success = model.success,
                failureReason = model.failureReason,
                timestamp = model.timestamp
            )
        }
    }
}