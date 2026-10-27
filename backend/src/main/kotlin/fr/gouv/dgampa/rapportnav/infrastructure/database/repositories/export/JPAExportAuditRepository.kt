package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.export

import fr.gouv.dgampa.rapportnav.domain.entities.export.ExportAuditEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.export.IExportAuditRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.export.ExportAuditModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.export.IDBExportAuditRepository
import org.springframework.stereotype.Repository

@Repository
class JPAExportAuditRepository(
    private val repository: IDBExportAuditRepository,
) : IExportAuditRepository {

    override fun save(audit: ExportAuditEntity) {
        repository.save(ExportAuditModel.fromExportAuditEntity(audit))
    }
}
