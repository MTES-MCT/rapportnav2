package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionAEMExportEntity
import fr.gouv.dgampa.rapportnav.infrastructure.excel.ExportExcelFile
import fr.gouv.dgampa.rapportnav.infrastructure.factories.ExportExcelFactory
import java.util.*
@UseCase
class ExportAEMExcel {

    fun execute(rowIndex: Int, cellIndex: Int, value: String): MissionAEMExportEntity {
        val excelFile: ExportExcelFile = ExportExcelFactory.create("/test.xlsx")
        excelFile.writeToCell("Avril", rowIndex, cellIndex, value)
        val file = excelFile.save()
        val base64 = Base64.getEncoder().encodeToString(file)
        return MissionAEMExportEntity(
            fileContent = base64,
            fileName = "test_poc.xlsx"
        )
    }
}
