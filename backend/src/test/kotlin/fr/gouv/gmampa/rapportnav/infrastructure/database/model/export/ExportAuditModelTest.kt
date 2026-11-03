package fr.gouv.gmampa.rapportnav.infrastructure.database.model.export

import fr.gouv.dgampa.rapportnav.domain.entities.export.ActionDocumentTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.export.ExportAuditEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.export.ExportAuditModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExportAuditModelTest {

    @Test
    fun `fromExportAuditEntity should map fields and leave id and auditing fields to persistence`() {
        val entity = ExportAuditEntity(ownerId = "761", actionId = "12345", documentType = ActionDocumentTypeEnum.SATI)

        assertEquals(
            ExportAuditModel(ownerId = "761", actionId = "12345", documentType = ActionDocumentTypeEnum.SATI),
            ExportAuditModel.fromExportAuditEntity(entity),
        )
    }
}
