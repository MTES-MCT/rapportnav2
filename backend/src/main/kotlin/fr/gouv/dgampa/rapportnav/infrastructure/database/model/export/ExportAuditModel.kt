package fr.gouv.dgampa.rapportnav.infrastructure.database.model.export

import fr.gouv.dgampa.rapportnav.domain.entities.export.ActionDocumentTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.export.ExportAuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "export_audit")
data class ExportAuditModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    val id: UUID? = null,

    @Column(name = "owner_id", length = 50, nullable = false, updatable = false)
    val ownerId: String,

    @Column(name = "action_id", length = 50, nullable = false, updatable = false)
    val actionId: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", length = 50, nullable = false, updatable = false)
    val documentType: ActionDocumentTypeEnum,

    @CreatedBy
    @Column(name = "exported_by", nullable = false, updatable = false)
    var exportedBy: Int? = null,

    @CreatedDate
    @Column(name = "exported_at", nullable = false, updatable = false)
    var exportedAt: Instant? = null,
) {
    companion object {
        // exportedBy / exportedAt are filled by Spring Data auditing (AuditorAwareImpl) on insert
        fun fromExportAuditEntity(entity: ExportAuditEntity): ExportAuditModel {
            return ExportAuditModel(
                ownerId = entity.ownerId,
                actionId = entity.actionId,
                documentType = entity.documentType,
            )
        }
    }
}
