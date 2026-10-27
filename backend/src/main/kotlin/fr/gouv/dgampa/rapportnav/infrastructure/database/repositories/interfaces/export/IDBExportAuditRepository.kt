package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.export

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.export.ExportAuditModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface IDBExportAuditRepository : JpaRepository<ExportAuditModel, UUID>
