package fr.gouv.dgampa.rapportnav.domain.entities.export

/**
 * A generated document together with the audit trace to record once it has been delivered.
 */
class ExportedDocumentEntity(
    val content: ByteArray,
    val audit: ExportAuditEntity,
)
