package fr.gouv.dgampa.rapportnav.domain.entities.satiExport

/**
 * Trace of a document exported for a single action (which document, for which action).
 * Who and when are recorded by the persistence layer on save. Append-only.
 */
data class ExportAuditEntity(
    val ownerId: String,
    val actionId: String,
    val documentType: ActionDocumentTypeEnum,
)
