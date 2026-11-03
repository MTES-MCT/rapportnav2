package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.export

import fr.gouv.dgampa.rapportnav.domain.entities.export.ActionDocumentTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.export.ExportAuditEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.export.ExportAuditModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.export.JPAExportAuditRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.export.IDBExportAuditRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPAExportAuditRepository::class])
class JPAExportAuditRepositoryTest {

    @MockitoBean
    private lateinit var dbRepository: IDBExportAuditRepository

    private lateinit var jpaRepository: JPAExportAuditRepository

    @BeforeEach
    fun setUp() {
        jpaRepository = JPAExportAuditRepository(dbRepository)
    }

    @Test
    fun `save should persist the mapped model`() {
        jpaRepository.save(
            ExportAuditEntity(ownerId = "761", actionId = "12345", documentType = ActionDocumentTypeEnum.SATI)
        )

        verify(dbRepository).save(
            ExportAuditModel(ownerId = "761", actionId = "12345", documentType = ActionDocumentTypeEnum.SATI)
        )
    }
}
