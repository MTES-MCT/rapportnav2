package fr.gouv.dgampa.rapportnav.domain.repositories.v2.export

import fr.gouv.dgampa.rapportnav.domain.entities.export.ExportAuditEntity

interface IExportAuditRepository {
    fun save(audit: ExportAuditEntity)
}
