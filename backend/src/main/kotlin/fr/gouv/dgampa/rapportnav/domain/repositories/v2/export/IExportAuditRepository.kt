package fr.gouv.dgampa.rapportnav.domain.repositories.v2.export

import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.ExportAuditEntity

interface IExportAuditRepository {
    fun save(audit: ExportAuditEntity)
}
